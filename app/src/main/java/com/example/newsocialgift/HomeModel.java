package com.example.newsocialgift;

import java.util.List;

public class HomeModel {
    private String userID;
    private String imageResource;
    private String text;
    private int buttonImageResource;
    private String wishlistName;
    private String wishlistDescription;
    private List<GiftItem> giftItems;

    public HomeModel(String userID, String imageResource, String text, int buttonImageResource, String wishlistName, String wishlistDescription, List<GiftItem> giftItems) {
        this.userID = userID;
        this.imageResource = imageResource;
        this.text = text;
        this.buttonImageResource = buttonImageResource;
        this.wishlistName = wishlistName;
        this.wishlistDescription = wishlistDescription;
        this.giftItems = giftItems;
    }

    public String getUserID() {
        return userID;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getText() {
        return text;
    }

    public int getButtonImageResource() {
        return buttonImageResource;
    }

    public String getWishlistName() {
        return wishlistName;
    }

    public String getWishlistDescription() {
        return wishlistDescription;
    }

    public List<GiftItem> getGiftItems() {
        return giftItems;
    }
}
