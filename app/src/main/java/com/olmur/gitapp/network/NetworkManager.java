package com.olmur.gitapp.network;

import android.support.annotation.NonNull;

import com.olmur.gitapp.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    public static final String BASE_URL = "https://api.github.com/";
    public static final String AUTH_BASE_URL = "https://github.com/";
    public static final String AUTHORIZE_REQUEST_PART = "https://github.com/login/oauth/authorize?client_id=" + BuildConfig.GITHUB_CLIENT_ID;

    public static Retrofit getRetrofit(@NonNull String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static GitHubApi getGitHubApi() {
        return getRetrofit(BASE_URL).create(GitHubApi.class);
    }

    public static GitHubAuth getGitHubAuth() {
        return getRetrofit(AUTH_BASE_URL).create(GitHubAuth.class);
    }
}
