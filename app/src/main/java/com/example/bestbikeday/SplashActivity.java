package com.example.bestbikeday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        ImageView splashIcon = findViewById(R.id.splashIcon);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splashIcon.startAnimation(fadeIn);
        
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            boolean hasSeenOnboarding = prefs.getBoolean("hasSeenOnboarding", false);

            Intent intent;
            if (hasSeenOnboarding) {
                intent = new Intent(this, LoginActivity.class);
            } else {
                intent = new Intent(this, OnboardingActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, SPLASH_DURATION);
    }
} 