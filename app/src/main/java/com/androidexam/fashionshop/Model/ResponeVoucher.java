package com.androidexam.fashionshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponeVoucher {
    @SerializedName("content")
    private List<Voucher> content;

    public List<Voucher> getContent() {
        return content;
    }

    public void setContent(List<Voucher> content) {
        this.content = content;
    }
}
