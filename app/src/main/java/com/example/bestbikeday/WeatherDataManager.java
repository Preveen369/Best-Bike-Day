package com.example.bestbikeday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import com.example.bestbikeday.model.WeatherResponse.ForecastItem;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataManager {
    private static final String PREF_NAME = "WeatherCache";
    private static final String KEY_WEATHER_DATA = "cached_weather";
    private static final String KEY_LAST_UPDATE = "last_update";
    private static final long CACHE_DURATION = 30 * 60 * 1000; // 30 minutes

    private static WeatherDataManager instance;
    private final Context context;
    private final SharedPreferences prefs;
    private final Gson gson;
    private final PreferencesManager preferencesManager;
    private List<ForecastItem> currentForecast;

    public interface WeatherCallback {
        void onWeatherData(ForecastItem bestDay);
    }

    private WeatherDataManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.preferencesManager = new PreferencesManager(context);
        this.gson = new Gson();
        loadCachedData();
    }

    public static synchronized WeatherDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherDataManager(context);
        }
        return instance;
    }

    public void updateForecast(List<ForecastItem> forecast) {
        this.currentForecast = forecast;
        cacheData();
    }

    public void getBestBikeDay(WeatherCallback callback) {
        if (isCacheValid()) {
            ForecastItem bestDay = findBestDay();
            new Handler(Looper.getMainLooper()).post(() -> callback.onWeatherData(bestDay));
        } else {
            // Cache is invalid, trigger a new API call
            callback.onWeatherData(null);
        }
    }

    private ForecastItem findBestDay() {
        if (currentForecast == null || currentForecast.isEmpty()) {
            return null;
        }

        ForecastItem bestDay = currentForecast.get(0);
        int highestScore = bestDay.calculateBikeRideScore(preferencesManager);

        for (ForecastItem day : currentForecast) {
            int score = day.calculateBikeRideScore(preferencesManager);
            if (score > highestScore) {
                highestScore = score;
                bestDay = day;
            }
        }

        return bestDay;
    }

    private void cacheData() {
        if (currentForecast != null) {
            String json = gson.toJson(currentForecast);
            prefs.edit()
                .putString(KEY_WEATHER_DATA, json)
                .putLong(KEY_LAST_UPDATE, System.currentTimeMillis())
                .apply();
        }
    }

    private void loadCachedData() {
        String json = prefs.getString(KEY_WEATHER_DATA, null);
        if (json != null) {
            try {
                currentForecast = gson.fromJson(json, 
                    new com.google.gson.reflect.TypeToken<List<ForecastItem>>(){}.getType());
            } catch (Exception e) {
                currentForecast = new ArrayList<>();
            }
        } else {
            currentForecast = new ArrayList<>();
        }
    }

    private boolean isCacheValid() {
        long lastUpdate = prefs.getLong(KEY_LAST_UPDATE, 0);
        return System.currentTimeMillis() - lastUpdate < CACHE_DURATION;
    }

    public List<ForecastItem> getCachedForecast() {
        return currentForecast != null ? new ArrayList<>(currentForecast) : new ArrayList<>();
    }
} 