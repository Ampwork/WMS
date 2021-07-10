package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClerkResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ProgramCount> programCount;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ProgramCount> getProgramCount() {
        return programCount;
    }

    public class ProgramCount {

        @SerializedName("program")
        private String program;


        @SerializedName("count")
        private List<count> countList;




        public List<count> getCountList() {
            return countList;
        }

        public String getProgram() {
            return program;
        }


    }

    public class count {

        @SerializedName("total_students")
        private String total_students;

        @SerializedName("semester")
        private String semester;

        public String getTotal_students() {
            return total_students;
        }

        public String getSemester() {
            return semester;
        }
    }
}
