package com.androidexam.fashionshop.Model;

public class Comment {
    private String username;
    private String content;
    private String time;
    private float rate;

    public String getUrl() {
        return avatarUsers;
    }

    private String avatarUsers;

    public Comment(String avatarUsers, String username, String content, String time,float rate) {
        this.username = username;
        this.content = content;
        this.time = time;
        this.avatarUsers = avatarUsers;
        this.rate =  rate;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public float getRate() {
        return rate;
    }
}
