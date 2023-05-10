package com.example.newsocialgift;

import android.widget.EditText;

public class HomeModel {
    private int imageResource;
    private String text;
    private int buttonImageResource;
    private String rectangleWithText;

    public HomeModel(int imageResource, String text, int buttonImageResource, String rectangleWithText) {
        this.imageResource = imageResource;
        this.text = text;
        this.buttonImageResource = buttonImageResource;
        this.rectangleWithText = rectangleWithText;
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

    public String getRectangleWithText() {
        return rectangleWithText;
    }
}
