package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubjectModel implements Parcelable {

    @SerializedName("id")
    private String subjectId;

    @SerializedName("subject")
    private String subjectName;

    @SerializedName("pgm_name")
    private String programName;

    @SerializedName("subject_code")
    private String subject_code;

    @SerializedName("semester")
    private String semester;

    public SubjectModel(String subjectName, String programName, String subject_code, String semester) {
        this.subjectName = subjectName;
        this.programName = programName;
        this.subject_code = subject_code;
        this.semester = semester;
    }

    protected SubjectModel(Parcel in) {
        subjectId = in.readString();
        subjectName = in.readString();
        programName = in.readString();
        subject_code = in.readString();
        semester = in.readString();
    }

    public static final Creator<SubjectModel> CREATOR = new Creator<SubjectModel>() {
        @Override
        public SubjectModel createFromParcel(Parcel in) {
            return new SubjectModel(in);
        }

        @Override
        public SubjectModel[] newArray(int size) {
            return new SubjectModel[size];
        }
    };

    public String getSemester() {
        return semester;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subjectId);
        dest.writeString(subjectName);
        dest.writeString(programName);
        dest.writeString(subject_code);
        dest.writeString(semester);
    }
}
