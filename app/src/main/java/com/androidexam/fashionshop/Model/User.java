package com.androidexam.fashionshop.Model;

import java.time.LocalDate;
import java.util.Date;

public class User {

    private int id;
    private String username;
    private String password;
    private String full_name;
    private String urlImage;
    private String address;
    private String birthday;
    private String phoneNumber;
    private String gmail;
    private String gender;

    public User(){

    }

    public User(int id, String username, String password, String name,String email, String urlImage, String address, String birthday, String phoneNumber, String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.full_name = name;
        this.urlImage = urlImage;
        this.address = address;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.gmail = email;
    }



    public User(String name, String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.full_name = name;
            this.gmail = email;

    }
    public User( String username, String password) {
        this.username = username;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return full_name;
    }

    public void setName(String name) {
        this.full_name = name;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return gmail;
    }

    public void setEmail(String gmail) {
        this.gmail = gmail;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public String  getDateOfBirth() {
            return birthday;
        }

        public void setDateOfBirth(String  dateOfBirth) {
            this.birthday = dateOfBirth;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

    public String getUrlImage() {
        return urlImage;
    }
}
