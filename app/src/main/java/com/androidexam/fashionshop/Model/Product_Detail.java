package com.androidexam.fashionshop.Model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product_Detail {


    @SerializedName("price")
    private int price;

    @SerializedName("price_promote")
    private int price_promote;

    @SerializedName("description")
    private String description;

    @SerializedName("productUrls")
    private List<String> productUrls;

    @SerializedName("commentCreatedAts")
    private List<String> commentCreatedAts;

    @SerializedName("commentUsers")
    private List<String> commentUsers;

    @SerializedName("sizeNames")
    private List<String> sizeNames;

    @SerializedName("sizeQuantity")
    private List<String> sizeQuantity;

    @SerializedName("sizeTypes")
    private List<String> sizeTypes;

    @SerializedName("commentContents")
    private List<String> commentContents;
    private List<String > avatarUsers;

    private List<Float> rate;

    @SerializedName("productId")
    private int productId;
    private int quantity;
    private int quantity_sold;

    public float getAvgRate() {
        return avgRate;
    }

    private float avgRate;

    @SerializedName("productName")
    private String productName;


    public Product_Detail( int productId ,String productName, List<String> productUrls,String description,float avgRate,int quantity,int quantity_sold, int price ,int price_promote,List<String> sizeNames, List<String> sizeQuantity, String brandName, List<String> commentUsers, List<String> commentContents, List<String>commentCreatedAts ,List<String> avatarUsers, List<Float>rate) {
        this.productId = productId;
        this.productName = productName;
        this.productUrls = productUrls;
        this.description = description;
        this.price = price;
        this.price_promote=price_promote;
        this.sizeNames = sizeNames;
        this.sizeQuantity = sizeQuantity;
        this.quantity = quantity;
        this.quantity_sold =  quantity_sold;
        this.commentContents = commentContents;
        this.commentUsers = commentUsers;
        this.commentCreatedAts =  commentCreatedAts;
        this.avatarUsers = avatarUsers;
        this.avgRate = avgRate;
        this.rate = rate;

    }

    // Các phương thức getter và setter ở đây


    public int getPrice() {
        return price;
    }
    public int getPrice_promote() {
        return price_promote;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getQuantity_sold() {
        return quantity_sold;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getProductUrls() {
        return productUrls;
    }

    public void setProductUrls(List<String> productUrls) {
        this.productUrls = productUrls;
    }

    public List<String> getCommentCreatedAts() {
        return commentCreatedAts;
    }

    public void setCommentCreatedAts(List<String> commentCreatedAts) {
        this.commentCreatedAts = commentCreatedAts;
    }

    public List<String> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(List<String> commentUsers) {
        this.commentUsers = commentUsers;
    }

    public List<String> getSizeNames() {
        return sizeNames;
    }

    public void setSizeNames(List<String> sizeNames) {
        this.sizeNames = sizeNames;
    }

    public List<String> getSizeQuantity() {
        return sizeQuantity;
    }

    public void setSizeQuantity(List<String> sizeQuantity) {
        this.sizeQuantity = sizeQuantity;
    }

    public List<String> getSizeTypes() {
        return sizeTypes;
    }

    public void setSizeTypes(List<String> sizeTypes) {
        this.sizeTypes = sizeTypes;
    }

    public List<String> getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(List<String> commentContents) {
        this.commentContents = commentContents;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }
    // Trong lớp Product_Detail
    public List<Comment> getComments() {
        List<Comment> comments = new ArrayList<>();

            for (int i = 0; i < commentUsers.size(); i++) {
                Comment comment = new Comment(avatarUsers.get(i),commentUsers.get(i), commentContents.get(i), commentCreatedAts.get(i),rate.get(i));
                comments.add(comment);
            }
            return comments;


    }

}
