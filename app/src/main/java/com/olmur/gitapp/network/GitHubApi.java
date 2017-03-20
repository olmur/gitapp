package com.olmur.gitapp.network;

import com.olmur.gitapp.entity.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GitHubApi {

    @GET("user")
    Call<User> getUser();
}
