package com.example.newsocialgift;

public class HomeModel {
    private int imageResource;
    private String text;
    private int buttonImageResource;

    public HomeModel(int imageResource, String text, int buttonImageResource) {
        this.imageResource = imageResource;
        this.text = text;
        this.buttonImageResource = buttonImageResource;
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
}
