package com.androidexam.fashionshop.Model;

public class Category {
    public Category() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Category(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name; // Trả về tên danh mục khi chuyển đối tượng thành chuỗi
    }

    private  int id;
    private  String name;
    private String description;
}
