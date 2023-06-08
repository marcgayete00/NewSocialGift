package com.example.newsocialgift;

public class MessagesModel {
    private String userID;
    private String imageURL;
    private String name;

    public MessagesModel(String userID, String imageURL, String name) {
        this.userID = userID;
        this.imageURL = imageURL;
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }
}
