package com.example.newsocialgift;

public class GiftItem {
    private int imageResId;
    private String changeName;
    private boolean checked;

    public GiftItem(int imageResId, String changeName, boolean checked) {
        this.imageResId = imageResId;
        this.changeName = changeName;
        this.checked = checked;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getChangeName() {
        return changeName;
    }

    public boolean isChecked() {
        return checked;
    }
}
