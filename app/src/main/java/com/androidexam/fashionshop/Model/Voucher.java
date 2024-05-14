package com.androidexam.fashionshop.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Voucher {
        private int id;
        private String code;
        private String  expiryDate;
        private String description;
        private String voucherType;
        private double discountValue;
        private boolean active;
        private String discountType;

        private double maxDiscountValue;
        private double minimumPurchaseAmount;
        private int usageLimit;
        private int usageCount;



    public Voucher() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiryDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        return LocalDateTime.parse(expiryDate, formatter);
       // return expiryDate;
    }

    public void setExpiryDate(String  expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getMaxDiscountValue() {
        return maxDiscountValue;
    }

    public void setMaxDiscountValue(double maxDiscountValue) {
        this.maxDiscountValue = maxDiscountValue;
    }

    public double getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(double minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
}
