package com.example.newsocialgift;

public class Gift {
    private String id;
    private String wishlistId;
    private String productUrl;
    private int priority;
    private int booked;

    public Gift(String id, String wishlistId, String productUrl, int priority, int booked) {
        this.id = id;
        this.wishlistId = wishlistId;
        this.productUrl = productUrl;
        this.priority = priority;
        this.booked = booked;
    }

    public String getId() {
        return id;
    }

    public String getWishlistId() {
        return wishlistId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getPriority() {
        return priority;
    }

    public int isBooked() {
        return booked;
    }
}
