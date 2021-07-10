package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

public class SubjectModel {

    @SerializedName("id")
    private String subjectId;

    @SerializedName("subject")
    private String subjectName;

    @SerializedName("pgm_name")
    private String programName;

    @SerializedName("subject_code")
    private String subject_code;

    public String getSubject_code() {
        return subject_code;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public String toString() {
        return subjectName ;
    }
}
