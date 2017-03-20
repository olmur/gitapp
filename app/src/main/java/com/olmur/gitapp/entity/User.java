package com.olmur.gitapp.entity;


import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    private String mLogin;

    @SerializedName("avatar_url")
    private String mAvatarUrl;

    @SerializedName("name")
    private String mName;

    @SerializedName("company")
    private String mCompany;

    @SerializedName("blog")
    private String mBlog;

    @SerializedName("location")
    private String mLocation;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("bio")
    private String mBio;

    @SerializedName("public_repos")
    private int mPublicRepos;

    @SerializedName("followers")
    private int mFollowers;

    @SerializedName("following")
    private int mFollowing;


    public String getLogin() {
        return mLogin;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public String getName() {
        return mName;
    }

    public String getCompany() {
        return mCompany;
    }

    public String getBlog() {
        return mBlog;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getBio() {
        return mBio;
    }

    public int getPublicRepos() {
        return mPublicRepos;
    }

    public int getFollowers() {
        return mFollowers;
    }

    public int getFollowing() {
        return mFollowing;
    }
}
