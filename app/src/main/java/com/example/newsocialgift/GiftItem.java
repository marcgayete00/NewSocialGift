package com.example.newsocialgift;

public class GiftItem {
    private String imageResId;
    private String giftName;
    private boolean checked;

    public GiftItem(String imageResId, String giftName, boolean checked) {
        this.imageResId = imageResId;
        this.giftName = giftName;
        this.checked = checked;
    }

    public String getImageResId() {
        return imageResId;
    }

    public String getGiftName() {
        return giftName;
    }

    public boolean isChecked() {
        return checked;
    }
}
