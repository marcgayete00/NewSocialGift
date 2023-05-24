package com.example.newsocialgift;

public class Wishlist {
    private String id;
    private String wishlistName;
    private String wishlistDescription;
    private int userId;
    private String creationDate;
    private String endDate;
    private Gift[] gifts;

    public Wishlist(String id, String wishlistName, String wishlistDescription, int userId, String creationDate, String endDate, Gift[] gifts) {
        this.id = id;
        this.wishlistName = wishlistName;
        this.wishlistDescription = wishlistDescription;
        this.userId = userId;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.gifts = gifts;
    }

    public String getId() {
        return id;
    }

    public String getWishlistName() {
        return wishlistName;
    }

    public String getWishlistDescription() {
        return wishlistDescription;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Gift[] getGifts() {
        return gifts;
    }
}
