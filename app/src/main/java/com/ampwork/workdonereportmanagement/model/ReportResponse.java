package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportResponse {

    @SerializedName("userid")
    private String userid;

    @SerializedName("report_id")
    private String report_id;

    @SerializedName("co_status")
    private String co_status;

    @SerializedName("remarks")
    private String remarks;

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ReportModel> reportModels;

    public ReportResponse(String userid, String report_id) {
        this.userid = userid;
        this.report_id = report_id;
    }

    public ReportResponse(String userid, String report_id, String co_status, String remarks) {
        this.userid = userid;
        this.report_id = report_id;
        this.co_status = co_status;
        this.remarks = remarks;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ReportModel> getReportModels() {
        return reportModels;
    }

    public String getUserid() {
        return userid;
    }

    public String getReport_id() {
        return report_id;
    }

    public String getCo_status() {
        return co_status;
    }

    public String getRemarks() {
        return remarks;
    }
}
