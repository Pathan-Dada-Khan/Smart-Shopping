package com.example.smartshopping.model;

import android.net.Uri;

public class ItemModel {

    Uri imageUri;

    String phoneNumber;
    String name;
    Double price;
    Double quantity;
    String brand;

    ItemModel(){}

    public ItemModel(Uri imageUri, String phoneNumber, String name, Double price, Double quantity, String brand) {
        this.imageUri = imageUri;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.brand = brand;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getBrand() {
        return brand;
    }
}
