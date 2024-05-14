package com.androidexam.fashionshop.Model;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {
    @SerializedName("access_token")
    private String access_token;

    @SerializedName("refresh_token")
    private String refresh_token;
    @SerializedName("user_id")
    private int user_id;
    private  String message;
    public String getAccessToken() {
        return access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
    public int getId(){
        return user_id;
    }

    public String getMessage() {
        return message;
    }
}

