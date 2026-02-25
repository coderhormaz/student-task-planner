package com.hormaz.studentplanner.ui.onboarding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.ui.auth.LoginActivity;
import com.hormaz.studentplanner.ui.common.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::navigateNext, SPLASH_DELAY);
    }

    private void navigateNext() {
        App app = App.getInstance();
        Intent intent;

        if (!app.getPreferencesManager().isOnboardingComplete()) {
            intent = new Intent(this, OnboardingActivity.class);
        } else if (app.getAuthRepository().isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
