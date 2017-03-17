package com.olmur.gitapp.presentation.screen.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.olmur.gitapp.BuildConfig;
import com.olmur.gitapp.R;
import com.olmur.gitapp.entity.Token;
import com.olmur.gitapp.network.NetworkManager;
import com.olmur.gitapp.presentation.screen.AnimationProvider;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.root)
    View mRoot;
    @BindView(R.id.login_fab)
    FloatingActionButton mLoginFab;
    @BindView(R.id.loading_reveal_view)
    View mLoadingReveal;
    @BindView(R.id.loading_animation_view)
    AVLoadingIndicatorView mLoadingAnimationView;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.expandable_frame)
    FrameLayout mExpandableFrame;
    @BindView(R.id.app_name_tv)
    TextView mAppNameTv;
    @BindView(R.id.wave_animated_circle)
    View mWaveAnimatedCircleV;

    private int mExpandFrameHeight;
    private int mCollapseFrameHeight;

    private int mExpandedAppNameTextSize;
    private int mCollapsedAppNameTextSize;

    public static final String CALLBACK_URL = "http://gitapp/callback?code=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            mCollapseFrameHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        mExpandedAppNameTextSize = 48;
        mCollapsedAppNameTextSize = 18;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveAnimatedCircleV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wave_scaling_animation));
    }

    @OnClick(R.id.login_fab)
    public void loginButtonPressed() {
        renderLoading();
    }

    public void renderWebViewOpen() {
        Animator fadeInAnimator = AnimationProvider.getFadeInAnimator(mWebView, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Snackbar authBar = Snackbar.make(mRoot, "Grand GitApp access to your account", Snackbar.LENGTH_INDEFINITE);
                authBar.setAction("Cancel", v -> {
                    renderWebViewClose();
                });
                authBar.show();
            }
        });
        fadeInAnimator.start();
    }

    public void renderWebViewClose() {
        Animator fadeOutAnimator = AnimationProvider.getFadeOutAnimator(mWebView, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                renderFrameExpand(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        renderLoadingCancel();
                    }
                });
            }
        });
        fadeOutAnimator.start();
    }

    public void makeAuthorizeRequest() {
        mWebView.loadUrl(NetworkManager.AUTHORIZE_REQUEST_PART);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains(CALLBACK_URL)) {
                    String responseCode = url.substring(CALLBACK_URL.length(), url.length());
                    Log.d(TAG, "Response Code: " + responseCode);
                    NetworkManager.getGitHubAuth().authorize(BuildConfig.GITHUB_CLIENT_ID, BuildConfig.GITHUB_CLIENT_SECRET, responseCode).enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            Log.d(TAG, "GitHub Access Token: " + response.body().getAccessToken());
                            if (mWebView.getVisibility() == View.VISIBLE) {
                                renderWebViewClose();
                            } else {
                                renderLoadingCancel();
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Log.e(TAG, "onFailure: ", t);
                            if (mWebView.getVisibility() == View.VISIBLE) {
                                renderWebViewClose();
                            } else {
                                renderLoadingCancel();
                            }
                        }
                    });

                } else {
                    view.loadUrl(url);
                    renderFrameCollapse(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            renderWebViewOpen();
                        }
                    });
                }
                return true;
            }
        });
    }

    public void renderLoading() {
        // get the center start point for reveal animation
        int cx = mLoadingReveal.getMeasuredWidth() / 2;
        int cy = mLoadingReveal.getMeasuredHeight() / 2;
        // get view diagonal
        int endRadius = (int) Math.sqrt(Math.pow(mLoadingReveal.getWidth(), 2) + Math.pow(mLoadingReveal.getHeight(), 2));
        Animator fadeInAnimator = AnimationProvider.getFadeInAnimator(mLoadingAnimationView, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingAnimationView.show();
                makeAuthorizeRequest();
            }
        });

        Animator revealAnimator = AnimationProvider.getRevealAnimator(mLoadingReveal, cx, cy, 0, endRadius, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLoadingReveal.setVisibility(View.VISIBLE);
                mLoginFab.hide();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(revealAnimator, fadeInAnimator);
        animatorSet.start();
    }

    public void renderLoadingCancel() {
        // get the center start point for reveal animation
        int cx = mLoadingReveal.getMeasuredWidth() / 2;
        int cy = mLoadingReveal.getMeasuredHeight() / 2;
        // get view diagonal
        int startRadius = (int) Math.sqrt(Math.pow(mLoadingReveal.getWidth(), 2) + Math.pow(mLoadingReveal.getHeight(), 2));

        Animator revealAnimator = AnimationProvider.getRevealAnimator(mLoadingReveal, cx, cy, startRadius, 0, 300, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingReveal.setVisibility(View.GONE);
                mLoginFab.show();
            }
        });
        Animator fadeOutAnimator = AnimationProvider.getFadeOutAnimator(mLoadingAnimationView, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revealAnimator.start();
            }
        });
        fadeOutAnimator.start();
    }

    public void renderFrameExpand(@Nullable AnimatorListenerAdapter listenerAdapter) {
        Animator expandAnimator = AnimationProvider.getHeightChangeAnimator(mExpandableFrame, mCollapseFrameHeight, mExpandFrameHeight, 300, listenerAdapter);
        Animator textSizeBiggerAnimator = AnimationProvider.getTextSizeAnimator(mAppNameTv, mCollapsedAppNameTextSize, mExpandedAppNameTextSize, 300, null);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(expandAnimator, textSizeBiggerAnimator);
        animatorSet.start();
    }

    public void renderFrameCollapse(@Nullable AnimatorListenerAdapter listener) {
        mExpandFrameHeight = mExpandableFrame.getHeight();
        Animator collapseAnimator = AnimationProvider.getHeightChangeAnimator(mExpandableFrame, mExpandFrameHeight, mCollapseFrameHeight, 300, listener);
        Animator textSizeSmallerAnimator = AnimationProvider.getTextSizeAnimator(mAppNameTv, mExpandedAppNameTextSize, mCollapsedAppNameTextSize, 300, null);
        AnimatorSet collapseSet = new AnimatorSet();
        collapseSet.playTogether(collapseAnimator, textSizeSmallerAnimator);
        collapseSet.start();
    }
}
