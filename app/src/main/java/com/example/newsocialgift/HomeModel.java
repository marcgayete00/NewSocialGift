package com.example.newsocialgift;

import java.util.List;

public class HomeModel {
    private int imageResource;
    private String text;
    private int buttonImageResource;
    private String wishlistName;
    private String wishlistDescription;
    private List<GiftItem> giftItems;

    public HomeModel(int imageResource, String text, int buttonImageResource, String wishlistName, String wishlistDescription, List<GiftItem> giftItems) {
        this.imageResource = imageResource;
        this.text = text;
        this.buttonImageResource = buttonImageResource;
        this.wishlistName = wishlistName;
        this.wishlistDescription = wishlistDescription;
        this.giftItems = giftItems;
    }

    public int getImageResource() {
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

    public List<GiftItem> getPresentItems() {
        return giftItems;
    }
}
