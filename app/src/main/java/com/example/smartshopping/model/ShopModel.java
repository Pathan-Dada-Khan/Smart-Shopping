package com.example.smartshopping.model;

public class ShopModel {

    private int shopImage;
    private int ownerImage;
    private String shopName;
    private String address;
    private double rating;
    private long numberOfRatings;

    ShopModel(){}

    public ShopModel(int shopImage, int ownerImage, String shopName, String ownerName, String category, String address, double rating, long numberOfRatings) {
        this.shopImage = shopImage;
        this.ownerImage = ownerImage;
        this.shopName = shopName;
        this.address = address;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
    }

    public int getShopImage() {
        return shopImage;
    }

    public int getOwnerImage() {
        return ownerImage;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public long getNumberOfRatings() {
        return numberOfRatings;
    }
}
