package com.androidexam.fashionshop.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CartItem implements Parcelable {

    public CartItem(int quantity, int productId, String size) {
        this.quantity = quantity;
        this.productId = productId;
        this.size = size;

    }
    public CartItem(int quantity, int productId, String size, long unitPrice,String url) {
        this.quantity = quantity;
        this.productId = productId;
        this.size = size;
        this.unitPrice=unitPrice;
        this.url =url;
    }
    public CartItem(int id,int quantity, int productId, String size) {
        this.quantity = quantity;
        this.productId = productId;
        this.size = size;
        this.id = id;
    }

    private int id;

    private String url;
    private int quantity;
    private long unitPrice;
    private int productId;

    private List<String>sizeProduct;

    private String productName;

    private int userId;
    private long disPrice;
    private String size;
    private int remain;

    private boolean apiCallInProgress; // Thêm biến này để theo dõi trạng thái của cuộc gọi API

    private boolean isSelected;

    public CartItem() {

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CartItem(boolean apiCallInProgress) {
        // Khởi tạo các giá trị cho các biến thành viên khác
        this.apiCallInProgress = apiCallInProgress;
    }

    // Các phương thức getter và setter cho apiCallInProgress
    public boolean isApiCallInProgress() {
        return apiCallInProgress;
    }

    public void setApiCallInProgress(boolean apiCallInProgress) {
        this.apiCallInProgress = apiCallInProgress;
    }

    public List<String> getSizeProduct() {
        return sizeProduct;
    }

    public void setSizeProduct(List<String> sizeProduct) {
        this.sizeProduct = sizeProduct;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

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

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }


    public long getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(long disPrice) {
        this.disPrice = disPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setProductName(String productName){this.productName=productName; }

    public String getProductName(){return productName;}
    public void setUrl(String url){this.url=url;}
    public String getUrl(){return url;}


    protected CartItem(Parcel in) {
        // Đọc dữ liệu từ Parcel và khởi tạo đối tượng
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Ghi dữ liệu vào Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
