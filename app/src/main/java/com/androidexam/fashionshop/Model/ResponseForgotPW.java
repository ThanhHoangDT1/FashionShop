package com.androidexam.fashionshop.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseForgotPW {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("message")
        private String message;

        @SerializedName("token")
        private String token;

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }
    }
}