package com.androidexam.fashionshop.Model;

import java.util.List;

public class ApiResponseWard {
    private int code;
    private String message;
    private List<Ward> data;


    public ApiResponseWard () {
    }

    public List<Ward> getData() {
        return data;
    }

    public void setData(List<Ward> data) {
        this.data = data;
    }
}
