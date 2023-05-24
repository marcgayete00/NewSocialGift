package com.example.newsocialgift;

public class GiftItem {
    private int imageResId;
    private String giftName;
    private boolean checked;

    public GiftItem(int imageResId, String giftName, boolean checked) {
        this.imageResId = imageResId;
        this.giftName = giftName;
        this.checked = checked;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getGiftName() {
        return giftName;
    }

    public boolean isChecked() {
        return checked;
    }
}
