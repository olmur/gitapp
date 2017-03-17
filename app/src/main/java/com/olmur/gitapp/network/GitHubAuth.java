package com.olmur.gitapp.network;

import com.olmur.gitapp.entity.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubAuth {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    Call<Token> authorize(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("code") String code);
}
