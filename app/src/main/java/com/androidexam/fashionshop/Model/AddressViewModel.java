package com.androidexam.fashionshop.Model;

import androidx.lifecycle.ViewModel;

public class AddressViewModel extends ViewModel {
    private String name;
    private String phoneNumber;
    private String Street;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }
}
