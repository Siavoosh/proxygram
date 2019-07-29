package com.proxygram.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {


    /**
     * name : mmd
     * row_id : 1
     * expire : حساب کاربری شما منقضی شده است. لطفا نسبت به تمدید آن اقدام نمایید
     * color : #FFB71C1C
     */
    @SerializedName("name")
    private String name;
    @SerializedName("row_id")
    private String row_id;
    @SerializedName("expire")
    private String expire;
    @SerializedName("color")
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.row_id);
        dest.writeString(this.expire);
        dest.writeString(this.color);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.row_id = in.readString();
        this.expire = in.readString();
        this.color = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
