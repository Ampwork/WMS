package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("path")
    private String path;

    @SerializedName("co_device")
    private String co_device;

    @SerializedName("sup_device")
    private String sup_device;

    @SerializedName("user_device")
    private String user_device;

    @SerializedName("data")
    private UserInfo userInfo;


    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getPath() {
        return path;
    }

    public String getCo_device() {
        return co_device;
    }

    public String getSup_device() {
        return sup_device;
    }


    public String getUser_device() {
        return user_device;
    }
}
