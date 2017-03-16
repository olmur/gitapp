package com.olmur.gitapp.presentation.screen.login;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.olmur.gitapp.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.error_tv)
    TextView mErrorTv;
    @BindView(R.id.username_tiet)
    TextInputEditText mUsernameEt;
    @BindView(R.id.password_tiet)
    TextInputEditText mPasswordEt;
    @BindView(R.id.login_fab)
    FloatingActionButton mLoginFab;
    @BindView(R.id.loading_reveal_view)
    View mLoadingReveal;
    @BindView(R.id.loading_animation_view)
    AVLoadingIndicatorView mLoadingAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_fab)
    public void loginButtonPressed() {
        enterLoadingReveal();
        new Handler().postDelayed(this::exitLoadingReveal, 3000);
    }

    public void enterLoadingReveal() {
        // get the center for the clipping circle
        int cx = mLoginFab.getRight() - mLoginFab.getMeasuredWidth() / 2;
        int cy = mLoginFab.getLeft() + mLoginFab.getMeasuredWidth() / 2;
        // get the final radius for the clipping circle
        int finalRadius = (int) Math.sqrt(Math.pow(mLoadingReveal.getWidth(), 2) + Math.pow(mLoadingReveal.getHeight(), 2));
        Animator revealAnimation = ViewAnimationUtils.createCircularReveal(mLoadingReveal, cx, cy, 0, finalRadius);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLoadingAnimationView, View.ALPHA, 0f, 1f);

        revealAnimation.setDuration(300);
        alphaAnimation.setDuration(200);

        mLoadingReveal.setVisibility(View.VISIBLE);
        revealAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLoginFab.hide();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingAnimationView.setVisibility(View.VISIBLE);
                alphaAnimation.start();
                mLoadingAnimationView.show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        revealAnimation.start();
    }

    public void exitLoadingReveal() {
        // get the center for the clipping circle
        int cx = mLoginFab.getRight() - mLoginFab.getMeasuredWidth() / 2;
        int cy = mLoginFab.getLeft() + mLoginFab.getMeasuredWidth() / 2;
        // get the final radius for the clipping circle
        int startRadius = (int) Math.sqrt(Math.pow(mLoadingReveal.getWidth(), 2) + Math.pow(mLoadingReveal.getHeight(), 2));
        Animator revealAnimation = ViewAnimationUtils.createCircularReveal(mLoadingReveal, cx, cy, startRadius, 0);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLoadingAnimationView, View.ALPHA, 1f, 0f);
        revealAnimation.setDuration(300);
        alphaAnimation.setDuration(200);
        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLoadingAnimationView.hide();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingAnimationView.setVisibility(View.INVISIBLE);
                revealAnimation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        revealAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingReveal.setVisibility(View.GONE);
                mLoginFab.show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        alphaAnimation.start();
    }
}
