package com.proxygram.model;

import com.google.gson.annotations.SerializedName;

public class Message {

    /**
     * sender : true
     * text : سلام خوبی ؟؟؟
     * photo : null
     * date : 1398/02/26 - 20:31:10
     */
    @SerializedName("sender")
    private boolean sender;
    @SerializedName("text")
    private String text;
    @SerializedName("photo")
    private String photo;
    @SerializedName("date")
    private String date;

    public boolean isSender() {
        return sender;
    }

    public void setSender(boolean sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
