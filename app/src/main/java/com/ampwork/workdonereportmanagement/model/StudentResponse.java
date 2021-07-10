package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<StudentDetailsModel> studentDetailsModels;


    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<StudentDetailsModel> getStudentDetailsModels() {
        return studentDetailsModels;
    }
}
