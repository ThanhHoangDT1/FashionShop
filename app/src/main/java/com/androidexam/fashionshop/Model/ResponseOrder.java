package com.androidexam.fashionshop.Model;

import java.util.List;

public class ResponseOrder {
        private String id;
        private String orderDate;
        private String orderStatus;
        private String paymentMethod;
        private String name;
        private String shippingAddress;
        private String phoneNumber;
        private String note;
        private double totalPayment;
        private double totalProductAmount;
        private double shippingFee;
        private double discountAmount;
        private double discountShippingFee;
        private List<OrderItem> orderItems;
        private int userId;
        private String urlPayment;

        // Các phương thức getter và setter cho từng thuộc tính

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public double getTotalPayment() {
            return totalPayment;
        }

        public void setTotalPayment(double totalPayment) {
            this.totalPayment = totalPayment;
        }

        public double getTotalProductAmount() {
            return totalProductAmount;
        }

        public void setTotalProductAmount(double totalProductAmount) {
            this.totalProductAmount = totalProductAmount;
        }

        public double getShippingFee() {
            return shippingFee;
        }

        public void setShippingFee(double shippingFee) {
            this.shippingFee = shippingFee;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(double discountAmount) {
            this.discountAmount = discountAmount;
        }

        public double getDiscountShippingFee() {
            return discountShippingFee;
        }

        public void setDiscountShippingFee(double discountShippingFee) {
            this.discountShippingFee = discountShippingFee;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUrlPayment() {
            return urlPayment;
        }

        public void setUrlPayment(String urlPayment) {
            this.urlPayment = urlPayment;
        }

    }

