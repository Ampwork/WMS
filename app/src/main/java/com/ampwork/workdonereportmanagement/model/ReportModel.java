package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

public class ReportModel {
    @SerializedName("id")
    private String reportId;

    @SerializedName("userid")
    private String userid;

    @SerializedName("username")
    private String username;

    @SerializedName("months")
    private String month;

    @SerializedName("years")
    private String year;

    @SerializedName("program")
    private String program;

    @SerializedName("leaves")
    private String leaves;

    @SerializedName("status")
    private String status;

    @SerializedName("co_id")
    private String co_id;

    @SerializedName("sup_id")
    private String sup_id;

    public ReportModel(String userid, String username, String month, String year,
                       String program, String leaves, String co_id, String sup_id) {
        this.userid = userid;
        this.username = username;
        this.month = month;
        this.year = year;
        this.program = program;
        this.leaves = leaves;
        this.co_id = co_id;
        this.sup_id = sup_id;
    }

    public String getReportId() {
        return reportId;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getProgram() {
        return program;
    }

    public String getLeaves() {
        return leaves;
    }

    public String getStatus() {
        return status;
    }


}
