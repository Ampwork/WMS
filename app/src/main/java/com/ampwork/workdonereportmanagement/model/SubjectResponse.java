package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<SubjectModel> subjectModel;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<SubjectModel> getSubjectModel() {
        return subjectModel;
    }
}
