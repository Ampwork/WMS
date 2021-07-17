package com.ampwork.workdonereportmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeeksModel   {

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

    @Override
    public String toString() {
        return "WeeksModel{" +
                "weeknumber='" + weeknumber + '\'' +
                ", jsonObject=" + jsonObject +
                '}';
    }


}
