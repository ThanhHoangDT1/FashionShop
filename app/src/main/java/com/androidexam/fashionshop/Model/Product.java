package com.androidexam.fashionshop.Model;

import java.util.List;

public class Product {


    private String product_name;
    private int price;

    private int quantity;
    private int quantity_sold;
    private int product_id;
    private List<String> product_image ;

    private int price_promote;
    public int getPrice_promote() {
        return price_promote;
    }

    public void setPrice_promote(int price_promote) {
        this.price_promote = price_promote;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantitySold() {
        return quantity_sold;
    }

    public void setQuantitySold(int quantity_sold) {
        this.quantity_sold = quantity_sold;
    }

    public int getId() {
        return product_id;
    }

    public void setId(int id) {
        this.product_id = id;
    }

    public List<String> getProductImage() {
        return product_image;
    }

    public void setProductImage(List<String> product_image) {
        this.product_image = product_image;
    }



    public Product(String name, int price, List<String> url,int id, int quantity, int quantity_sold,int price_promote) {
        this.product_name = name;
        this.price = price;
        this.product_image = url;
        this.product_id = id;
        this.quantity = quantity;
        this.quantity_sold =  quantity_sold;
        this.price_promote = price_promote;
    }



}