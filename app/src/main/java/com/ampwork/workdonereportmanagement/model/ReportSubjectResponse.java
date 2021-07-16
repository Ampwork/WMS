package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportSubjectResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<GenerateReportResponse.ReportSubject> reportSubjects;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GenerateReportResponse.ReportSubject> getReportSubjects() {
        return reportSubjects;
    }
}
