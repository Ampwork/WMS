package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgramResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ProgramModel> programModels;


    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ProgramModel> getProgramModels() {
        return programModels;
    }

    public static class ProgramModel implements Parcelable {
        @SerializedName("id")
        private String programId;

        @SerializedName("pgm_name")
        private String programName;

        public ProgramModel(String programName) {
            this.programName = programName;
        }



        protected ProgramModel(Parcel in) {
            programId = in.readString();
            programName = in.readString();
        }

        public static final Creator<ProgramModel> CREATOR = new Creator<ProgramModel>() {
            @Override
            public ProgramModel createFromParcel(Parcel in) {
                return new ProgramModel(in);
            }

            @Override
            public ProgramModel[] newArray(int size) {
                return new ProgramModel[size];
            }
        };

        public String getProgramId() {
            return programId;
        }

        public String getProgramName() {
            return programName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(programId);
            dest.writeString(programName);
        }
    }
}
