package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserInfo implements Parcelable {

    @SerializedName("id")
    private String userId;

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    @SerializedName("device_id")
    private String fcmToken;

    @SerializedName("about")
    private String about;

    @SerializedName("profilepic")
    private String profileUrl;

    @SerializedName("sign")
    private String signUrl;

    @SerializedName("role")
    private String role;

    @SerializedName("program")
    private String program;

    @SerializedName("username")
    private String username;

    public UserInfo(String username, String password, String fcmToken) {
        this.password = password;
        this.fcmToken = fcmToken;
        this.username = username;
    }

    public UserInfo(String userId, String firstName, String lastName, String email, String phone,
                    String password, String fcmToken, String about) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fcmToken = fcmToken;
        this.about = about;
    }

    public UserInfo(String firstName, String lastName, String email, String phone,
                    String password, String role, String program) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.program = program;
    }

    protected UserInfo(Parcel in) {
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        fcmToken = in.readString();
        about = in.readString();
        profileUrl = in.readString();
        signUrl = in.readString();
        role = in.readString();
        program = in.readString();
        username = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getAbout() {
        return about;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public String getRole() {
        return role;
    }

    public String getProgram() {
        return program;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(fcmToken);
        dest.writeString(about);
        dest.writeString(profileUrl);
        dest.writeString(signUrl);
        dest.writeString(role);
        dest.writeString(program);
        dest.writeString(username);
    }
}
