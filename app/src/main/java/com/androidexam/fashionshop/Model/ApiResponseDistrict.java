package com.androidexam.fashionshop.Model;

import java.util.List;

public class ApiResponseDistrict {
    private int code;
    private String message;
    private List<District> data;


    public ApiResponseDistrict () {
    }

    public List<District> getData() {
        return data;
    }

    public void setData(List<District> data) {
        this.data = data;
    }
}
