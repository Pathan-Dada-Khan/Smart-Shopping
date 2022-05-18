package com.example.smartshopping;

public class ShopDetailsModel {

    String shopName;
    String ownerName;

    String weekDaysOpens;
    String weekDaysClosed;
    String weekEndsOpens;
    String weekEndsClosed;

    String category;
    String location;
    String address;

    public ShopDetailsModel(String shopName, String ownerName, String weekDaysOpens, String weekDaysClosed, String weekEndsOpens, String weekEndsClosed, String category, String location, String address) {
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.weekDaysOpens = weekDaysOpens;
        this.weekDaysClosed = weekDaysClosed;
        this.weekEndsOpens = weekEndsOpens;
        this.weekEndsClosed = weekEndsClosed;
        this.category = category;
        this.location = location;
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getWeekDaysOpens() {
        return weekDaysOpens;
    }

    public String getWeekDaysClosed() {
        return weekDaysClosed;
    }

    public String getWeekEndsOpens() {
        return weekEndsOpens;
    }

    public String getWeekEndsClosed() {
        return weekEndsClosed;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }
}
