package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceReportResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("attedance")
    List<ReportAttendanceModel> reportAttendanceModels ;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ReportAttendanceModel> getReportAttendanceModels() {
        return reportAttendanceModels;
    }
}
