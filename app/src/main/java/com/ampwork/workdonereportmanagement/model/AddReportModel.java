package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddReportModel implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

    @SerializedName("day")
    private String day;

    @SerializedName("type")
    private String type;

    @SerializedName("semester")
    private String semester;

    @SerializedName("from_time")
    private String from_time;

    @SerializedName("to_time")
    private String to_time;

    @SerializedName("no_of_hours")
    private String no_of_hours;

    @SerializedName("description")
    private String description;

    @SerializedName("report_id")
    private String reportid;

    @SerializedName("subject")
    private String subject;

    @SerializedName("total_present")
    private String total_present;

    @SerializedName("total_absent")
    private String total_absent;



    public AddReportModel(String date, String day, String type, String semester, String from_time,
                          String to_time, String no_of_hours, String description, String reportid,
                          String subject, String total_present, String total_absent) {
        this.date = date;
        this.day = day;
        this.type = type;
        this.semester = semester;
        this.from_time = from_time;
        this.to_time = to_time;
        this.no_of_hours = no_of_hours;
        this.description = description;
        this.reportid = reportid;
        this.subject = subject;
        this.total_present = total_present;
        this.total_absent = total_absent;
    }

    public AddReportModel(String id, String date, String day, String type, String semester, String from_time,
                          String to_time, String no_of_hours, String description, String reportid,
                          String subject, String total_present, String total_absent) {
        this.id = id;
        this.date = date;
        this.day = day;
        this.type = type;
        this.semester = semester;
        this.from_time = from_time;
        this.to_time = to_time;
        this.no_of_hours = no_of_hours;
        this.description = description;
        this.reportid = reportid;
        this.subject = subject;
        this.total_present = total_present;
        this.total_absent = total_absent;
    }

    public AddReportModel(Parcel in) {
        id = in.readString();
        date = in.readString();
        day = in.readString();
        type = in.readString();
        semester = in.readString();
        from_time = in.readString();
        to_time = in.readString();
        no_of_hours = in.readString();
        description = in.readString();
        reportid = in.readString();
        subject = in.readString();
        total_present = in.readString();
        total_absent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(date);
        dest.writeString(day);
        dest.writeString(type);
        dest.writeString(semester);
        dest.writeString(from_time);
        dest.writeString(to_time);
        dest.writeString(no_of_hours);
        dest.writeString(description);
        dest.writeString(reportid);
        dest.writeString(subject);
        dest.writeString(total_present);
        dest.writeString(total_absent);
    }

    public static final Creator<AddReportModel> CREATOR = new Creator<AddReportModel>() {
        @Override
        public AddReportModel createFromParcel(Parcel in) {
            return new AddReportModel(in);
        }

        @Override
        public AddReportModel[] newArray(int size) {
            return new AddReportModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getType() {
        return type;
    }

    public String getSemester() {
        return semester;
    }

    public String getFrom_time() {
        return from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public String getNo_of_hours() {
        return no_of_hours;
    }

    public String getDescription() {
        return description;
    }

    public String getReportid() {
        return reportid;
    }

    public String getSubject() {
        return subject;
    }

    public String getTotal_present() {
        return total_present;
    }

    public String getTotal_absent() {
        return total_absent;
    }


    @Override
    public String toString() {
        return "AddReportModel{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", type='" + type + '\'' +
                ", semester='" + semester + '\'' +
                ", from_time='" + from_time + '\'' +
                ", to_time='" + to_time + '\'' +
                ", no_of_hours='" + no_of_hours + '\'' +
                ", description='" + description + '\'' +
                ", reportid='" + reportid + '\'' +
                ", subject='" + subject + '\'' +
                ", total_present='" + total_present + '\'' +
                ", total_absent='" + total_absent + '\'' +
                '}';
    }


}
