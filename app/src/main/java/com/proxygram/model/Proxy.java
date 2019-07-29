package com.proxygram.model;

import com.google.gson.annotations.SerializedName;

public class Proxy {

    @SerializedName("link")
    private String link;
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("date")
    private String date;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
