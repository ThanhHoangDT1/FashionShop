package com.androidexam.fashionshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetOrdered {
    @SerializedName("content")
    private List<ResponseOrder> content;

    public List<ResponseOrder> getContent() {
        return content;
    }

    public void setContent(List<ResponseOrder> content) {
        this.content = content;
    }
}
