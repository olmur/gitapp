package com.olmur.gitapp.network;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.olmur.gitapp.BuildConfig;
import com.olmur.gitapp.R;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    public static final String AUTH_TOKEN_PREF = "auth_token";
    public static final String BASE_URL = "https://api.github.com/";
    public static final String AUTH_BASE_URL = "https://github.com/";
    public static final String AUTHORIZE_REQUEST_PART = "https://github.com/login/oauth/authorize?client_id=" + BuildConfig.GITHUB_CLIENT_ID;

    public static Retrofit getRetrofit(@NonNull String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    @NonNull
    private static HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    @NonNull
    private static Interceptor getHeaderInterceptor(@NonNull Context context) {
        return chain -> {
            Request original = chain.request();

            // Request customization: add request headers
            String accessToken = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(NetworkManager.AUTH_TOKEN_PREF, "null");
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "token " + accessToken)
                    .header("User-Agent", context.getString(R.string.app_name));

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    @NonNull
    private static OkHttpClient getHttpClient(Interceptor... interceptors) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        for (Interceptor interceptor : interceptors) {
            httpClient.addInterceptor(interceptor);
        }
        return httpClient.build();
    }

    public static GitHubApi getGitHubApi(@NonNull Context context) {
        return getRetrofit(BASE_URL, getHttpClient(getLogInterceptor(), getHeaderInterceptor(context))).create(GitHubApi.class);
    }

    public static GitHubAuth getGitHubAuth() {
        return getRetrofit(AUTH_BASE_URL, getHttpClient(getLogInterceptor())).create(GitHubAuth.class);
    }
}
