package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceReportResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("attendance")
    List<Reports> reportsList;


    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Reports> getReportsList() {
        return reportsList;
    }

    public static class Reports {

        @SerializedName("date")
        private String date;

        @SerializedName("attendance_list")
        List<ReportAttendanceModel> reportAttendanceModels ;

        public List<ReportAttendanceModel> getReportAttendanceModels() {
            return reportAttendanceModels;
        }

        public String getDate() {
            return date;
        }
    }
}
