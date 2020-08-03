package com.example.mdmscheme.Common;


public class FoodData {

    private String key;
    private String location;
    private String photo;
    private String name;
    private String dec;
    private String type;
    private String time;


    public FoodData() {
    }

    public FoodData(String location,String photo, String name, String dec,String type,String time) {

        this.location = location;
        this.photo = photo;
        this.name = name;
        this.dec = dec;
        this.time =time;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
