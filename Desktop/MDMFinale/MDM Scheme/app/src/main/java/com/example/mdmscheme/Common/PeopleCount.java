package com.example.mdmscheme.Common;

import android.provider.Contacts;

public class PeopleCount {

    private String tcounts;
    private String Tvehicle;
    private String key;

    public PeopleCount() {
    }

    public PeopleCount(String tcounts,String tvehicle) {
        this.tcounts =tcounts;
        this.Tvehicle = tvehicle;

    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTcounts(String tcounts) {
        this.tcounts = tcounts;
    }

    public String getTcounts() {
        return tcounts;
    }

    public void setTvehicle(String tvehicle) {
        Tvehicle = tvehicle;
    }

    public String getTvehicle() {
        return Tvehicle;
    }
}

