package com.olmur.gitapp.entity;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("access_token")
    private String mAccessToken;

    public String getAccessToken() {
        return mAccessToken;
    }
}
