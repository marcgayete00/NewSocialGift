package com.example.newsocialgift;

public class ChatModel {
    private String sender;
    private String message;

    public ChatModel(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
