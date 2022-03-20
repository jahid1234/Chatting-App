package com.example.chatappdemo.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String status;
    boolean isAddedToGroup;
    public User(String id, String username, String imageURL,String status) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        isAddedToGroup = false;
    }

    public User() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddedToGroup(boolean addedToGroup) {
        isAddedToGroup = addedToGroup;
    }

    public boolean isAddedToGroup() {
        return isAddedToGroup;
    }
}
