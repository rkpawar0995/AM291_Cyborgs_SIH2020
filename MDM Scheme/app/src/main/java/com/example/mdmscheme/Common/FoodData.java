package com.example.mdmscheme.Common;


public class FoodData {

    private String key;
    private String location;
    private String photo;
    private String name;
    private String dec;


    public FoodData() {
    }

    public FoodData(String location,String photo, String name, String dec) {

        this.location = location;
        this.photo = photo;
        this.name = name;
        this.dec = dec;
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
}
