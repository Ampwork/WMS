package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyReportModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ReportsModel> reportsModels;



    public static class ReportsModel {

        @SerializedName("date")
        private String date;

        @SerializedName("report")
        private List<AddReportModel> addReportModels;

        public List<AddReportModel> getAddReportModels() {
            return addReportModels;
        }

        public String getDate() {
            return date;
        }
    }


    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ReportsModel> getReportsModels() {
        return reportsModels;
    }
}
