package com.androidexam.fashionshop.Model;

import java.util.List;

public class ApiResponse  {
    private int code;
    private String message;
    private List<Province> data;


    public ApiResponse () {
    }

    public List<Province> getData() {
        return data;
    }

    public void setData(List<Province> data) {
        this.data = data;
    }
}
