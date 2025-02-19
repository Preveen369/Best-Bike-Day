package com.example.bestbikeday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button nextButton;
    private Button prevButton;
    private Button skipButton;

    private static final int[] IMAGES = {
        R.drawable.onboarding_weather1,
        R.drawable.onboarding_score1,
        R.drawable.onboarding_notifications1
    };

    private static final String[] TITLES = {
        "Check Weather Conditions",
        "Get Bike Ride Scores",
        "Stay Informed"
    };

    private static final String[] DESCRIPTIONS = {
        "Get detailed weather forecasts including temperature, wind speed, and precipitation.",
        "See how suitable the weather is for biking with our easy-to-understand scoring system.",
        "Enable notifications to get daily updates about the best time for your bike ride."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize views
        viewPager = findViewById(R.id.onboardingPager);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        skipButton = findViewById(R.id.skipButton);

        // Set initial button states
        prevButton.setVisibility(View.GONE); // Hide previous button initially
        skipButton.setVisibility(View.VISIBLE);
        nextButton.setEnabled(true);

        // Set up click listeners
        nextButton.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < viewPager.getAdapter().getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                // Mark onboarding as completed
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                prefs.edit().putBoolean("hasSeenOnboarding", true).apply();

                // Navigate to LoginActivity after onboarding
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        prevButton.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });

        skipButton.setOnClickListener(v -> finishOnboarding());

        // Set up adapter
        OnboardingAdapter adapter = new OnboardingAdapter();
        viewPager.setAdapter(adapter);

        // Handle page changes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateButtonVisibility(position);
            }
        });
    }

    private void updateButtonVisibility(int position) {
        if (position == IMAGES.length - 1) {
            // Last page
            nextButton.setText("Get Started");
            prevButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.GONE);
        } else if (position == 0) {
            // First page
            nextButton.setText("Next");
            prevButton.setVisibility(View.GONE);
            skipButton.setVisibility(View.VISIBLE);
        } else {
            // Middle pages
            nextButton.setText("Next");
            prevButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.VISIBLE);
        }
    }

    private void finishOnboarding() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("hasSeenOnboarding", true).apply();
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_onboarding, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.imageView.setImageResource(IMAGES[position]);
            holder.titleText.setText(TITLES[position]);
            holder.descriptionText.setText(DESCRIPTIONS[position]);
        }

        @Override
        public int getItemCount() {
            return IMAGES.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView titleText;
            TextView descriptionText;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                titleText = itemView.findViewById(R.id.titleText);
                descriptionText = itemView.findViewById(R.id.descriptionText);
            }
        }
    }
} 