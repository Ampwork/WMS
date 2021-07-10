package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubmitReportResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    List<GenerateReportResponse.ReportResponseModel> reportResponseModels;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GenerateReportResponse.ReportResponseModel> getReportResponseModels() {
        return reportResponseModels;
    }
}
