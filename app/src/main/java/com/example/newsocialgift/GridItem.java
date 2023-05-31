package com.example.newsocialgift;

public class GridItem {
    String wishlistID;
    private String text;
    private int color;

    public GridItem(String wishlistID, String text, int color) {
        this.wishlistID = wishlistID;
        this.text = text;
        this.color = color;
    }

    public String getWishlistID() {
        return wishlistID;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }
}