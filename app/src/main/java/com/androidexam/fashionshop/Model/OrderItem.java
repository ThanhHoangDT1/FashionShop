package com.androidexam.fashionshop.Model;

public class OrderItem {
    private int id;
    private int quantity;
    private double unitPrice;
    private String sizeType;
    private int productId;
    private int orderId;
    private boolean rate;

    public OrderItem() {
        // Constructor mặc định
    }

    public OrderItem(int id, int quantity, double unitPrice, String sizeType, int productId, int orderId, boolean rate) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sizeType = sizeType;
        this.productId = productId;
        this.orderId = orderId;
        this.rate = rate;
    }


    // Các phương thức getter và setter cho từng thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isRate() {
        return rate;
    }

    public void setRate(boolean rate) {
        this.rate = rate;
    }


}
