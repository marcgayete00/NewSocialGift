package com.example.newsocialgift;

public class Product {
    private String id;
    private String productName;
    private String productDescription;
    private String productLink;
    private String productImage;
    private float productPrice;
    private String[] categoryIds;

    public Product(String id, String productName, String productDescription, String productLink, String productImage, float productPrice, String[] categoryIds) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productLink = productLink;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.categoryIds = categoryIds;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductLink() {
        return productLink;
    }

    public String getProductImage() {
        return productImage;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public String[] getCategoryIds() {
        return categoryIds;
    }
}
