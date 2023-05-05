package com.example.newsocialgift;

public class User {

    private String id;
    private String username;
    private String lastName;
    private String email;
    private String image;

    public User(String id, String username, String lastName, String email, String image) {
        this.id = id;
        this.username = username;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
