package com.example.bestbikeday;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREF_NAME = "BikeWeatherPrefs";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_TEMP_UNIT = "temperature_unit";
    private static final String KEY_WIND_UNIT = "wind_speed_unit";
    private static final String KEY_NOTIFICATION_HOUR = "notification_hour";
    private static final String KEY_NOTIFICATION_MINUTE = "notification_minute";
    private static final String KEY_WEATHER_ALERTS = "weather_alerts_enabled";

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean getNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS, false);
    }

    public void setNotificationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply();
    }

    public String getTemperatureUnit() {
        return prefs.getString(KEY_TEMP_UNIT, "°C");
    }

    public void setTemperatureUnit(String unit) {
        prefs.edit().putString(KEY_TEMP_UNIT, unit).apply();
    }

    public String getWindSpeedUnit() {
        return prefs.getString(KEY_WIND_UNIT, "km/h");
    }

    public void setWindSpeedUnit(String unit) {
        prefs.edit().putString(KEY_WIND_UNIT, unit).apply();
    }

    public int getNotificationHour() {
        return prefs.getInt(KEY_NOTIFICATION_HOUR, 8); // Default to 8 AM
    }

    public void setNotificationHour(int hour) {
        prefs.edit().putInt(KEY_NOTIFICATION_HOUR, hour).apply();
    }

    public int getNotificationMinute() {
        return prefs.getInt(KEY_NOTIFICATION_MINUTE, 0); // Default to 0 minutes
    }

    public void setNotificationMinute(int minute) {
        prefs.edit().putInt(KEY_NOTIFICATION_MINUTE, minute).apply();
    }

    public boolean getWeatherAlertsEnabled() {
        return prefs.getBoolean(KEY_WEATHER_ALERTS, false);
    }

    public void setWeatherAlertsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_WEATHER_ALERTS, enabled).apply();
    }
} 