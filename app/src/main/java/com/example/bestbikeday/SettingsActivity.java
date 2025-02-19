package com.example.bestbikeday;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bestbikeday.auth.AuthManager;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferencesManager = new PreferencesManager(this);
        authManager = new AuthManager();

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        setupUnitToggles();
        setupNotifications();
        setupSignOut();
    }

    private void setupUnitToggles() {
        MaterialButtonToggleGroup tempUnitToggle = findViewById(R.id.tempUnitToggle);
        MaterialButtonToggleGroup windUnitToggle = findViewById(R.id.windUnitToggle);

        // Set initial states
        tempUnitToggle.check(getTemperatureButtonId(preferencesManager.getTemperatureUnit()));
        windUnitToggle.check(getWindSpeedButtonId(preferencesManager.getWindSpeedUnit()));

        // Set listeners
        tempUnitToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                preferencesManager.setTemperatureUnit(getTemperatureUnit(checkedId));
            }
        });

        windUnitToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                preferencesManager.setWindSpeedUnit(getWindSpeedUnit(checkedId));
            }
        });
    }

    private void setupNotifications() {
        SwitchMaterial notificationSwitch = findViewById(R.id.notificationSwitch);
        TextView notificationTimeText = findViewById(R.id.notificationTimeText);
        SwitchMaterial weatherAlertsSwitch = findViewById(R.id.weatherAlertsSwitch);

        notificationSwitch.setChecked(preferencesManager.getNotificationsEnabled());
        weatherAlertsSwitch.setChecked(preferencesManager.getWeatherAlertsEnabled());

        // Initially, do not set a default time
        notificationTimeText.setText("Set Notification Time");

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setNotificationsEnabled(isChecked);
            if (isChecked) {
                // Only schedule notifications if a time has been set
                if (!notificationTimeText.getText().toString().equals("Set Notification Time")) {
                    int hour = preferencesManager.getNotificationHour();
                    int minute = preferencesManager.getNotificationMinute();
                    NotificationManager.scheduleNotifications(this, hour, minute);
                }
            } else {
                NotificationManager.cancelNotifications(this);
            }
        });

        weatherAlertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setWeatherAlertsEnabled(isChecked);
        });

        notificationTimeText.setOnClickListener(v -> {
            // Use current time as the default values for the picker
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
                preferencesManager.setNotificationHour(hourOfDay);
                preferencesManager.setNotificationMinute(minuteOfHour);
                updateNotificationTimeText(notificationTimeText, hourOfDay, minuteOfHour);
                if (notificationSwitch.isChecked()) {
                    NotificationManager.scheduleNotifications(this, hourOfDay, minuteOfHour);
                }
            }, hour, minute, false); // false for 12-hour format
            timePickerDialog.show();
        });
    }

    private void updateNotificationTimeText(TextView textView, int hour, int minute) {
        String time = String.format(Locale.getDefault(), "%02d:%02d %s",
                (hour == 0 || hour == 12) ? 12 : hour % 12,
                minute,
                hour < 12 ? "AM" : "PM");
        textView.setText("Notification Time: " + time);
    }

    private void setupSignOut() {
        TextView signOutText = findViewById(R.id.signOutText);
        signOutText.setOnClickListener(v -> {
            authManager.signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // Helper methods for unit conversion
    private int getTemperatureButtonId(String unit) {
        return unit.equals("°F") ? R.id.fahrenheit : R.id.celsius;
    }

    private String getTemperatureUnit(int buttonId) {
        return buttonId == R.id.fahrenheit ? "°F" : "°C";
    }

    private int getWindSpeedButtonId(String unit) {
        switch (unit) {
            case "mph": return R.id.mph;
            case "m/s": return R.id.ms;
            default: return R.id.kmh;
        }
    }

    private String getWindSpeedUnit(int buttonId) {
        if (buttonId == R.id.mph) {
            return "mph";
        } else if (buttonId == R.id.ms) {
            return "m/s";
        } else {
            return "km/h";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 