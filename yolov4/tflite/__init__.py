"""
MIT License

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
from os import path
import time
from typing import Union

import cv2
import numpy as np
import tflite_runtime.interpreter as tflite

from ..utility import media, predict


class YOLOv4:
    def __init__(self):
        """
        Default configuration
        """
        self._classes = None
        self.input_index = None
        self.input_size = None
        self.interpreter = None
        self.output_index = None

    @property
    def classes(self):
        """
        Usage:
            yolo.classes = {0: 'person', 1: 'bicycle', 2: 'car', ...}
            yolo.classes = "path/classes"
            print(len(yolo.classes))
        """
        return self._classes

    @classes.setter
    def classes(self, data: Union[str, dict]):
        if isinstance(data, str):
            self._classes = media.read_classes_names(data)
        elif isinstance(data, dict):
            self._classes = data
        else:
            raise TypeError("YOLOv4: Set classes path or dictionary")

    def load_tflite(self, tflite_path):
        self.interpreter = tflite.Interpreter(model_path=tflite_path)
        self.interpreter.allocate_tensors()
        input_details = self.interpreter.get_input_details()[0]
        self.input_size = input_details["shape"][1]
        self.input_index = input_details["index"]
        output_details = self.interpreter.get_output_details()
        self.output_index = [output_details[i]["index"] for i in range(3)]

    def resize_image(self, image, ground_truth=None):
        """
        @param image:        Dim(height, width, channels)
        @param ground_truth: [[center_x, center_y, w, h, class_id], ...]

        @return resized_image or (resized_image, resized_ground_truth)

        Usage:
            image = yolo.resize_image(image)
            image, ground_truth = yolo.resize_image(image, ground_truth)
        """
        return media.resize_image(
            image, target_size=self.input_size, ground_truth=ground_truth
        )

    def candidates_to_pred_bboxes(self, candidates):
        """
        @param candidates: Dim(-1, (x, y, w, h, conf, prob_0, prob_1, ...))

        @return Dim(-1, (x, y, w, h, class_id, probability))
        """
        return predict.candidates_to_pred_bboxes(candidates, self.input_size)

    def fit_pred_bboxes_to_original(self, pred_bboxes, original_shape):
        """
        @param pred_bboxes:    Dim(-1, (x, y, w, h, class_id, probability))
        @param original_shape: (height, width, channels)
        """
        # pylint: disable=no-self-use
        return predict.fit_pred_bboxes_to_original(pred_bboxes, original_shape)

    def draw_bboxes(self, image, bboxes):
        """
        @parma image:  Dim(height, width, channel)
        @param bboxes: (candidates, 4) or (candidates, 5)
                [[center_x, center_y, w, h, class_id], ...]
                [[center_x, center_y, w, h, class_id, propability], ...]

        @return drawn_image

        Usage:
            image = yolo.draw_bboxes(image, bboxes)
        """
        return media.draw_bboxes(image, bboxes, self.classes)

    #############
    # Inference #
    #############

    def predict(self, frame: np.ndarray):
        """
        Predict one frame

        @param frame: Dim(height, width, channels)

        @return pred_bboxes == Dim(-1, (x, y, w, h, class_id, probability))
        """
        # image_data == Dim(1, input_szie, input_size, channels)
        image_data = self.resize_image(frame)
        image_data = image_data / 255
        image_data = image_data[np.newaxis, ...].astype(np.float32)

        # s_pred, m_pred, l_pred
        # x_pred == Dim(1, output_size, output_size, anchors, (bbox))
        self.interpreter.set_tensor(self.input_index, image_data)
        self.interpreter.invoke()
        candidates = [
            self.interpreter.get_tensor(self.output_index[i]) for i in range(3)
        ]
        _candidates = []
        for candidate in candidates:
            grid_size = candidate.shape[1]
            _candidates.append(
                np.reshape(candidate, (1, grid_size * grid_size * 3, -1))
            )
        candidates = np.concatenate(_candidates, axis=1)

        pred_bboxes = self.candidates_to_pred_bboxes(candidates[0])
        pred_bboxes = self.fit_pred_bboxes_to_original(pred_bboxes, frame.shape)
        return pred_bboxes

    def inference(self, media_path, is_image=True, cv_waitKey_delay=10):
        if not path.exists(media_path):
            raise FileNotFoundError("{} does not exist".format(media_path))
        if is_image:
            frame = cv2.imread(media_path)
            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

            start_time = time.time()
            bboxes = self.predict(frame)
            exec_time = time.time() - start_time
            print("time: {:.2f} ms".format(exec_time * 1000))

            frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
            image = self.draw_bboxes(frame, bboxes)
            cv2.namedWindow("result", cv2.WINDOW_AUTOSIZE)
            cv2.imshow("result", image)
        else:
            vid = cv2.VideoCapture(media_path)
            while True:
                return_value, frame = vid.read()
                if return_value:
                    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                else:
                    break

                start_time = time.time()
                bboxes = self.predict(frame)
                curr_time = time.time()
                exec_time = curr_time - start_time
                info = "time: %.2f ms" % (1000 * exec_time)
                print(info)

                frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
                image = self.draw_bboxes(frame, bboxes)
                cv2.namedWindow("result", cv2.WINDOW_AUTOSIZE)
                cv2.imshow("result", image)
                if cv2.waitKey(cv_waitKey_delay) & 0xFF == ord("q"):
                    break

        print("YOLOv4: Inference is finished")
        while cv2.waitKey(10) & 0xFF != ord("q"):
            pass
        cv2.destroyWindow("result")
