package com.example.newsocialgift;

public class GridItem {
    private String text;
    private int color;

    public GridItem(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }
}