package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyReportResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<AddReportModel> addReportModels;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<AddReportModel> getAddReportModels() {
        return addReportModels;
    }
}
