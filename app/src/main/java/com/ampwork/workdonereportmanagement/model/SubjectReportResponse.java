package com.ampwork.workdonereportmanagement.model;

import com.google.gson.annotations.SerializedName;

public class SubjectReportResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private SubjectReport subjectReport;

    public static class SubjectReport {
        @SerializedName("report_id")
        private String report_id;

        @SerializedName("subject")
        private String subject;

        @SerializedName("subject_code")
        private String subject_code;

        @SerializedName("description")
        private String description;

        @SerializedName("percentage")
        private String percentage;

        public SubjectReport(String report_id, String subject, String subject_code, String description, String percentage) {
            this.report_id = report_id;
            this.subject = subject;
            this.subject_code = subject_code;
            this.description = description;
            this.percentage = percentage;
        }
    }
}
