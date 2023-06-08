package com.example.newsocialgift;

public class GiftItem {
    private String imageResId;
    private String giftName;
    private boolean checked;
    private String giftId;

    public GiftItem(String imageResId, String giftName, boolean checked, String giftId) {
        this.imageResId = imageResId;
        this.giftName = giftName;
        this.checked = checked;
        this.giftId = giftId;
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

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getGiftId() {
        return giftId;
    }
}
