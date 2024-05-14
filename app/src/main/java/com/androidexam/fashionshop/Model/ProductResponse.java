package com.androidexam.fashionshop.Model;


import java.util.List;

public class ProductResponse {
    List<Product> items;

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }
}