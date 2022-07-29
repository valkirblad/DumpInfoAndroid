package com.example.dumpinfoandroid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {

    @SerializedName("status")

    public List<Content> content;
    public String packageName;
    public List<String> clientId;

    Post(List<Content> content, List<String> clientId, String packageName) {
        this.content = content;
        this.clientId = clientId;
        this.packageName = packageName;
    }
}
