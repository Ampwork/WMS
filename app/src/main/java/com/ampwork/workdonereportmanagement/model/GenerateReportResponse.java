package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class GenerateReportResponse {

    @SerializedName("userid")
    private String userid;

    @SerializedName("semester")
    private String semester;

    @SerializedName("subject")
    private String subject;

    @SerializedName("fromDate")
    private String fromDate;

    @SerializedName("toDate")
    private String toDate;

    @SerializedName("month")
    private String month;

    public GenerateReportResponse(String userid, String semester, String subject, String fromDate, String toDate) {
        this.userid = userid;
        this.semester = semester;
        this.subject = subject;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public GenerateReportResponse(String userid, String semester, String subject, String month) {
        this.userid = userid;
        this.semester = semester;
        this.subject = subject;
        this.month = month;
    }

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ReportResponseModel> reportResponseModelList;

    @SerializedName("weeks")
    private List<WeeksModel> weeksModel;

    public List<WeeksModel> getWeeksModel() {
        return weeksModel;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ReportResponseModel> getReportResponseModelList() {
        return reportResponseModelList;
    }

    public static class ReportResponseModel implements Parcelable {


        @SerializedName("id")
        private String id;

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

        @SerializedName("usersign")
        private String usersign;

        @SerializedName("co_sign")
        private String co_sign;

        @SerializedName("superintendent")
        private String superintendent;

        @SerializedName("status")
        private String status;

        @SerializedName("co_remark")
        private String co_remark;

        @SerializedName("sup_remark")
        private String sup_remark;

        @SerializedName("report_list")
        private List<AddReportModel> addReportModels;

        @SerializedName("report_subject")
        private List<ReportSubject> reportSubjects;

        public ReportResponseModel(String id, String userid) {
            this.id = id;
            this.userid = userid;
        }


        protected ReportResponseModel(Parcel in) {
            id = in.readString();
            userid = in.readString();
            username = in.readString();
            month = in.readString();
            year = in.readString();
            program = in.readString();
            leaves = in.readString();
            usersign = in.readString();
            co_sign = in.readString();
            superintendent = in.readString();
            status = in.readString();
            co_remark = in.readString();
            sup_remark = in.readString();
            addReportModels = in.createTypedArrayList(AddReportModel.CREATOR);
            reportSubjects = in.createTypedArrayList(ReportSubject.CREATOR);
        }

        public static final Creator<ReportResponseModel> CREATOR = new Creator<ReportResponseModel>() {
            @Override
            public ReportResponseModel createFromParcel(Parcel in) {
                return new ReportResponseModel(in);
            }

            @Override
            public ReportResponseModel[] newArray(int size) {
                return new ReportResponseModel[size];
            }
        };

        public String getId() {
            return id;
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

        public String getUsersign() {
            return usersign;
        }

        public String getCo_sign() {
            return co_sign;
        }

        public String getSuperintendent() {
            return superintendent;
        }

        public String getStatus() {
            return status;
        }

        public String getCo_remark() {
            return co_remark;
        }

        public String getSup_remark() {
            return sup_remark;
        }

        public List<AddReportModel> getAddReportModels() {
            return addReportModels;
        }

        public List<ReportSubject> getReportSubjects() {
            return reportSubjects;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(userid);
            dest.writeString(username);
            dest.writeString(month);
            dest.writeString(year);
            dest.writeString(program);
            dest.writeString(leaves);
            dest.writeString(usersign);
            dest.writeString(co_sign);
            dest.writeString(superintendent);
            dest.writeString(status);
            dest.writeString(co_remark);
            dest.writeString(sup_remark);
            dest.writeTypedList(addReportModels);
            dest.writeTypedList(reportSubjects);
        }
    }

    public static class ReportSubject implements Parcelable {

        @SerializedName("id")
        private String id;

        @SerializedName("report_id")
        private String report_id;

        @SerializedName("subject_code")
        private String subject_code;

        @SerializedName("subject_name")
        private String subject_name;

        @SerializedName("percentage")
        private String percentage;

        @SerializedName("description")
        private String description;


        protected ReportSubject(Parcel in) {
            id = in.readString();
            report_id = in.readString();
            subject_code = in.readString();
            subject_name = in.readString();
            percentage = in.readString();
            description = in.readString();
        }

        public static final Creator<ReportSubject> CREATOR = new Creator<ReportSubject>() {
            @Override
            public ReportSubject createFromParcel(Parcel in) {
                return new ReportSubject(in);
            }

            @Override
            public ReportSubject[] newArray(int size) {
                return new ReportSubject[size];
            }
        };

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        public String getReport_id() {
            return report_id;
        }

        public String getSubject_code() {
            return subject_code;
        }

        public String getSubject_name() {
            return subject_name;
        }

        public String getPercentage() {
            return percentage;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(report_id);
            dest.writeString(subject_code);
            dest.writeString(subject_name);
            dest.writeString(percentage);
            dest.writeString(description);
        }
    }

    public static class WeeksModel implements Serializable {
        @SerializedName("weeknumber")
        String weeknumber;

        @SerializedName("dates")
        JsonArray jsonObject;


        public String getWeeknumber() {
            return weeknumber;
        }

        public JsonArray getJsonObject() {
            return jsonObject;
        }

    }
}