package com.androidexam.fashionshop.Model;


import java.util.List;

public class ProductResponse {
    List<Product> items;
    int page;
    int items_per_page;


    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItems_per_page() {
        return items_per_page;
    }

    public void setItems_per_page(int items_per_page) {
        this.items_per_page = items_per_page;
    }
}