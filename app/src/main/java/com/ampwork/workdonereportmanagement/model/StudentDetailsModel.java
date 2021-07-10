package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StudentDetailsModel implements Parcelable {

    @SerializedName("id")
    private String studentId;

    @SerializedName("usn")
    private String usn;

    @SerializedName("name")
    private String name;

    @SerializedName("program")
    private String program;

    @SerializedName("semester")
    private String semester;

    @SerializedName("dob")
    private String dob;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("gender")
    private String gender;

    @SerializedName("address")
    private String address;

    boolean isChecked;

    public StudentDetailsModel(String usn, String name, String program, String semester,
                               String dob, String email, String phone, String gender, String address) {
        this.usn = usn;
        this.name = name;
        this.program = program;
        this.semester = semester;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
    }


    protected StudentDetailsModel(Parcel in) {
        usn = in.readString();
        name = in.readString();
        program = in.readString();
        semester = in.readString();
        dob = in.readString();
        email = in.readString();
        phone = in.readString();
        gender = in.readString();
        address = in.readString();
    }

    public static final Creator<StudentDetailsModel> CREATOR = new Creator<StudentDetailsModel>() {
        @Override
        public StudentDetailsModel createFromParcel(Parcel in) {
            return new StudentDetailsModel(in);
        }

        @Override
        public StudentDetailsModel[] newArray(int size) {
            return new StudentDetailsModel[size];
        }
    };

    public String getStudentId() {
        return studentId;
    }

    public String getUsn() {
        return usn;
    }

    public String getName() {
        return name;
    }

    public String getProgram() {
        return program;
    }

    public String getSemester() {
        return semester;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usn);
        dest.writeString(name);
        dest.writeString(program);
        dest.writeString(semester);
        dest.writeString(dob);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(gender);
        dest.writeString(address);
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
