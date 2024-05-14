package com.androidexam.fashionshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetOrderItemDelivered {
    @SerializedName("content")
    private List<OrderItem> content;
    private int numberOfElements;

    public List<OrderItem> getContent() {
        return content;
    }

    public void setContent(List<OrderItem> content) {
        this.content = content;
    }

    public int getNumberOfElement() {
        return numberOfElements;
    }

    public void setNumberOfElement(int numberOfElement) {
        this.numberOfElements = numberOfElement;
    }
}
