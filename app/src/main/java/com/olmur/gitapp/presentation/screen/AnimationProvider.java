package com.olmur.gitapp.presentation.screen;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

public class AnimationProvider {

    public static ObjectAnimator getFadeInAnimator(@NonNull View target, long duration, @Nullable Animator.AnimatorListener listener) {
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(target, View.ALPHA, 0f, 1f);
        fadeInAnimator.setDuration(duration);

        fadeInAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                target.setVisibility(View.VISIBLE);
            }
        });

        if (listener != null) {
            fadeInAnimator.addListener(listener);
        }
        return fadeInAnimator;
    }

    public static ObjectAnimator getFadeOutAnimator(@NonNull View target, long duration, @Nullable Animator.AnimatorListener listener) {
        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0f);
        fadeOutAnimator.setDuration(duration);

        fadeOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }
        });

        if (listener != null) {
            fadeOutAnimator.addListener(listener);
        }
        return fadeOutAnimator;
    }

    public static Animator getRevealAnimator(@NonNull View target, int cx, int cy, int startRadius, int endRadius, long duration, @Nullable Animator.AnimatorListener listener) {
        Animator revealAnimator = ViewAnimationUtils.createCircularReveal(target, cx, cy, startRadius, endRadius);
        revealAnimator.setDuration(duration);
        if (listener != null) {
            revealAnimator.addListener(listener);
        }
        return revealAnimator;
    }

    public static Animator getHeightChangeAnimator(@NonNull View target, int startHeight, int endHeight, long duration, @Nullable Animator.AnimatorListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            target.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            target.requestLayout();
        });
        if (listener != null) {
            valueAnimator.addListener(listener);
        }
        return valueAnimator;
    }

    public static Animator getTextSizeAnimator(@NonNull TextView target, int startSize, int endSize, long duration, @Nullable Animator.AnimatorListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startSize, endSize);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(animation -> {
            target.setTextSize((float) animation.getAnimatedValue());
        });
        if (listener != null) {
            valueAnimator.addListener(listener);
        }
        return valueAnimator;
    }

}
