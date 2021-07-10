package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReportAttendanceModel implements Parcelable {
    @SerializedName("report_id")
    private String report_id;

    @SerializedName("student_id")
    private String student_id;

    @SerializedName("usn")
    private String usn;

    @SerializedName("date")
    private String date;

    @SerializedName("att_date")
    private String att_date;

    @SerializedName("subject")
    private String subject;

    @SerializedName("semester")
    private String semester;

    @SerializedName("status")
    private String status;

    @SerializedName("name")
    private String studentName;

    public ReportAttendanceModel(String report_id, String student_id, String date, String subject,
                                 String semester, String status) {
        this.report_id = report_id;
        this.student_id = student_id;
        this.date = date;
        this.subject = subject;
        this.semester = semester;
        this.status = status;
    }

    protected ReportAttendanceModel(Parcel in) {
        report_id = in.readString();
        student_id = in.readString();
        date = in.readString();
        att_date = in.readString();
        subject = in.readString();
        semester = in.readString();
        status = in.readString();
        studentName = in.readString();
    }

    public static final Creator<ReportAttendanceModel> CREATOR = new Creator<ReportAttendanceModel>() {
        @Override
        public ReportAttendanceModel createFromParcel(Parcel in) {
            return new ReportAttendanceModel(in);
        }

        @Override
        public ReportAttendanceModel[] newArray(int size) {
            return new ReportAttendanceModel[size];
        }
    };

    public String getUsn() {
        return usn;
    }

    public String getAtt_date() {
        return att_date;
    }

    public String getReport_id() {
        return report_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public String getSemester() {
        return semester;
    }

    public String getStatus() {
        return status;
    }

    public String getStudentName() {
        return studentName;
    }

    @Override
    public String toString() {
        return "ReportAttendanceModel{" +
                "report_id='" + report_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", date='" + date + '\'' +
                ", subject='" + subject + '\'' +
                ", semester='" + semester + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(report_id);
        dest.writeString(student_id);
        dest.writeString(date);
        dest.writeString(att_date);
        dest.writeString(subject);
        dest.writeString(semester);
        dest.writeString(status);
        dest.writeString(studentName);
    }
}
