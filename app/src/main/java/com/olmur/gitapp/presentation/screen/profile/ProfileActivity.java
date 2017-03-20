package com.olmur.gitapp.presentation.screen.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.olmur.gitapp.R;
import com.olmur.gitapp.entity.User;
import com.olmur.gitapp.network.GitHubApi;
import com.olmur.gitapp.network.NetworkManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.profile_pic_iv)
    ImageView mProfilePictureIv;
    @BindView(R.id.bio_tv)
    TextView mBioTv;
    @BindView(R.id.location_tv)
    TextView mLocationTv;
    @BindView(R.id.blog_tv)
    TextView mBlogTv;

    private GitHubApi mGitHubApi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mGitHubApi = NetworkManager.getGitHubApi(this);
        mBlogTv.setOnClickListener(v -> {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimaryDark))
                    .build();
            customTabsIntent.launchUrl(ProfileActivity.this, Uri.parse(mBlogTv.getText().toString()));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGitHubApi.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                initUser(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void initUser(@NonNull User user) {
        Glide.with(this).load(user.getAvatarUrl()).into(mProfilePictureIv);
        mBioTv.setText(user.getBio());
        mLocationTv.setText(user.getLocation());
        mBlogTv.setText(user.getBlog());
    }
}
