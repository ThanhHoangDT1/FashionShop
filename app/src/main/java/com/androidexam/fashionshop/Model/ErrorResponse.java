package com.androidexam.fashionshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ErrorResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private Map<String,String> errorDetail;

    @SerializedName("path")
    private String path;

    @SerializedName("timestamp")
    private String timestamp;

    public int getStatus() {
        return status;
    }

    public Map<String,String> getErrorDetail() {
        return errorDetail;
    }

    public String getPath() {
        return path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public class ErrorDetail {
        @SerializedName("message")
        private String message;

        public String getMessage() {
            return message;
        }
    }
}
