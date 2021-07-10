package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgramRepose {

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

    public static class ProgramModel {
        @SerializedName("id")
        private String programId;

        @SerializedName("pgm_name")
        private String programName;

        public String getProgramId() {
            return programId;
        }

        public String getProgramName() {
            return programName;
        }
    }
}
