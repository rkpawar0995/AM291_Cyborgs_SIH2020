"""
MIT License

Copyright (c) 2019 YangYun
Copyright (c) 2020 Việt Hùng
Copyright (c) 2020 Hyeonki Hong <hhk7734@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
"""
import os
import cv2
import numpy as np

from . import train
from ..utility import media


class Dataset:
    def __init__(
        self,
        anchors: np.ndarray = None,
        batch_size: int = 2,
        dataset_path: str = None,
        dataset_type: str = "converted_coco",
        data_augmentation: bool = True,
        input_size: int = 416,
        num_classes: int = None,
        strides: np.ndarray = None,
        xyscales: np.ndarray = None,
    ):
        self.anchors_ratio = anchors / input_size
        self.batch_size = batch_size
        self.dataset_path = dataset_path
        self.dataset_type = dataset_type
        self.data_augmentation = data_augmentation
        self.grid_size = input_size // strides
        self.input_size = input_size
        self.num_classes = num_classes
        self.xysclaes = xyscales

        self.grid = [
            np.tile(
                np.reshape(
                    np.stack(
                        np.meshgrid(
                            (np.arange(self.grid_size[i]) + 0.5)
                            / self.grid_size[i],
                            (np.arange(self.grid_size[i]) + 0.5)
                            / self.grid_size[i],
                        ),
                        axis=-1,
                    ),
                    (1, self.grid_size[i], self.grid_size[i], 1, 2),
                ),
                (1, 1, 1, 3, 1),
            ).astype(np.float32)
            for i in range(3)
        ]

        self.dataset = self.load_dataset()

        self.count = 0
        np.random.shuffle(self.dataset)

    def load_dataset(self):
        """
        @return
            yolo: [[image_path, [[x, y, w, h, class_id], ...]], ...]
            converted_coco: unit=> pixel
                [[image_path, [[x, y, w, h, class_id], ...]], ...]
        """
        _dataset = []

        with open(self.dataset_path, "r") as fd:
            txt = fd.readlines()
            if self.dataset_type == "converted_coco":
                for line in txt:
                    # line: "<image_path> xmin,ymin,xmax,ymax,class_id ..."
                    bboxes = line.strip().split()
                    image_path = bboxes[0]
                    xywhc_s = np.zeros((len(bboxes) - 1, 5))
                    for i, bbox in enumerate(bboxes[1:]):
                        # bbox = "xmin,ymin,xmax,ymax,class_id"
                        bbox = list(map(int, bbox.split(",")))
                        xywhc_s[i, :] = (
                            (bbox[0] + bbox[2]) / 2,
                            (bbox[1] + bbox[3]) / 2,
                            bbox[2] - bbox[0],
                            bbox[3] - bbox[1],
                            bbox[4],
                        )
                    _dataset.append([image_path, xywhc_s])

            elif self.dataset_type == "yolo":
                for line in txt:
                    # line: "<image_path>"
                    image_path = line.strip()
                    root, _ = os.path.splitext(image_path)
                    with open(root + ".txt") as fd2:
                        bboxes = fd2.readlines()
                        xywhc_s = np.zeros((len(bboxes), 5))
                        for i, bbox in enumerate(bboxes):
                            # bbox = class_id, x, y, w, h
                            bbox = bbox.strip()
                            bbox = list(map(float, bbox.split(" ")))
                            xywhc_s[i, :] = (
                                *bbox[1:],
                                bbox[0],
                            )
                        _dataset.append([image_path, xywhc_s])
        return _dataset

    def bboxes_to_ground_truth(self, bboxes):
        """
        @param bboxes: [[b_x, b_y, b_w, b_h, class_id], ...]

        @return [s, m, l]
            Dim(1, grid_y, grid_x, anchors,
                                (b_x, b_y, b_w, b_h, conf, prob_0, prob_1, ...))
        """
        ground_truth = [
            np.zeros(
                (
                    1,
                    self.grid_size[i],
                    self.grid_size[i],
                    3,
                    5 + self.num_classes,
                ),
                dtype=np.float32,
            )
            for i in range(3)
        ]
        ground_truth[0][..., 0:2] = self.grid[0]
        ground_truth[1][..., 0:2] = self.grid[1]
        ground_truth[2][..., 0:2] = self.grid[2]

        for bbox in bboxes:
            # [b_x, b_y, b_w, b_h, class_id]
            xywh = np.array(bbox[:4], dtype=np.float32)
            class_id = int(bbox[4])

            # smooth_onehot = [0.xx, ... , 1-(0.xx*(n-1)), 0.xx, ...]
            onehot = np.zeros(self.num_classes, dtype=np.float32)
            onehot[class_id] = 1.0
            uniform_distribution = np.full(
                self.num_classes, 1.0 / self.num_classes, dtype=np.float32
            )
            delta = 0.01
            smooth_onehot = (1 - delta) * onehot + delta * uniform_distribution

            ious = []
            exist_positive = False
            for i in range(3):
                # Dim(anchors, xywh)
                anchors_xywh = np.zeros((3, 4), dtype=np.float32)
                anchors_xywh[:, 0:2] = xywh[0:2]
                anchors_xywh[:, 2:4] = self.anchors_ratio[i]
                iou = train.bbox_iou(xywh, anchors_xywh)
                ious.append(iou)
                iou_mask = iou > 0.3

                if np.any(iou_mask):
                    xy_grid = xywh[0:2] * self.grid_size[i]
                    xy_index = np.floor(xy_grid)

                    exist_positive = True
                    for j, mask in enumerate(iou_mask):
                        if mask:
                            _x, _y = int(xy_index[0]), int(xy_index[1])
                            ground_truth[i][0, _y, _x, j, 0:4] = xywh
                            ground_truth[i][0, _y, _x, j, 4:5] = 1.0
                            ground_truth[i][0, _y, _x, j, 5:] = smooth_onehot

            if not exist_positive:
                index = np.argmax(np.array(ious))
                i = index // 3
                j = index % 3

                xy_grid = xywh[0:2] * self.grid_size[i]
                xy_index = np.floor(xy_grid)

                _x, _y = int(xy_index[0]), int(xy_index[1])
                ground_truth[i][0, _y, _x, j, 0:4] = xywh
                ground_truth[i][0, _y, _x, j, 4:5] = 1.0
                ground_truth[i][0, _y, _x, j, 5:] = smooth_onehot

        return ground_truth

    def preprocess_dataset(self, dataset):
        """
        @param dataset:
            yolo: [image_path, [[x, y, w, h, class_id], ...]]
            converted_coco: unit=> pixel
                [image_path, [[x, y, w, h, class_id], ...]]

        @return image / 255, ground_truth
        """
        image_path = dataset[0]
        if not os.path.exists(image_path):
            raise KeyError("{} does not exist".format(image_path))

        image = cv2.imread(image_path)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        if self.dataset_type == "converted_coco":
            height, width, _ = image.shape
            dataset[1] = dataset[1] / np.array(
                [width, height, width, height, 1]
            )

        resized_image, resized_bboxes = media.resize_image(
            image, self.input_size, dataset[1]
        )

        if self.data_augmentation:
            # TODO
            # BoF functions
            pass

        resized_image = np.expand_dims(resized_image / 255.0, axis=0)
        ground_truth = self.bboxes_to_ground_truth(resized_bboxes)

        return resized_image, ground_truth

    def __iter__(self):
        self.count = 0
        np.random.shuffle(self.dataset)
        return self

    def __next__(self):
        """
        @return image, ground_truth
            ground_truth == (s_truth, m_truth, l_truth)
        """
        if self.batch_size > 1:
            batch_x = []
            batch_y_s = []
            batch_y_l = []
            batch_y_m = []
            for _ in range(self.batch_size):
                x, y = self.preprocess_dataset(self.dataset[self.count])
                batch_x.append(x)
                batch_y_s.append(y[0])
                batch_y_m.append(y[1])
                batch_y_l.append(y[2])
            batch_x = np.concatenate(batch_x, axis=0)
            batch_y_s = np.concatenate(batch_y_s, axis=0)
            batch_y_m = np.concatenate(batch_y_m, axis=0)
            batch_y_l = np.concatenate(batch_y_l, axis=0)
            batch_y = (batch_y_s, batch_y_m, batch_y_l)
        else:
            batch_x, batch_y = self.preprocess_dataset(self.dataset[self.count])

        self.count += 1
        if self.count == len(self.dataset):
            np.random.shuffle(self.dataset)
            self.count = 0

        return batch_x, batch_y

    def __len__(self):
        return len(self.dataset)
