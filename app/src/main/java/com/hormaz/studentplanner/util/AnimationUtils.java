package com.hormaz.studentplanner.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class AnimationUtils {

    public static void fadeIn(View view, int duration) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    public static void fadeOut(View view, int duration) {
        view.animate()
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    public static void scaleIn(View view, int duration) {
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(duration)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();
    }

    public static void pulseAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f, 1f);
        scaleX.setDuration(Constants.ANIM_DURATION_MEDIUM);
        scaleY.setDuration(Constants.ANIM_DURATION_MEDIUM);
        scaleX.setInterpolator(new OvershootInterpolator());
        scaleY.setInterpolator(new OvershootInterpolator());
        scaleX.start();
        scaleY.start();
    }

    public static void checkboxAnimation(View view, Runnable onComplete) {
        PropertyValuesHolder scaleXUp = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f);
        PropertyValuesHolder scaleYUp = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f);
        ObjectAnimator scaleUp = ObjectAnimator.ofPropertyValuesHolder(view, scaleXUp, scaleYUp);
        scaleUp.setDuration(150);
        scaleUp.setInterpolator(new OvershootInterpolator());

        scaleUp.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PropertyValuesHolder scaleXDown = PropertyValuesHolder.ofFloat("scaleX", 1.3f, 1f);
                PropertyValuesHolder scaleYDown = PropertyValuesHolder.ofFloat("scaleY", 1.3f, 1f);
                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view, scaleXDown, scaleYDown);
                scaleDown.setDuration(150);
                scaleDown.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (onComplete != null) onComplete.run();
                    }
                });
                scaleDown.start();
            }
        });
        scaleUp.start();
    }

    public static void slideInFromBottom(View view, int duration) {
        view.setTranslationY(view.getHeight() > 0 ? view.getHeight() : 300);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(duration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    public static void slideOutToBottom(View view, int duration) {
        view.animate()
                .translationY(300)
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    private AnimationUtils() {}
}
