package com.example.bestbikeday.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.bestbikeday.NotificationManager;
import com.example.bestbikeday.PreferencesManager;
import com.example.bestbikeday.WeatherDataManager;
import com.example.bestbikeday.model.WeatherResponse.ForecastItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        WeatherDataManager.getInstance(context).getBestBikeDay(bestDay -> {
            if (bestDay != null) {
                String formattedDate = formatDate(bestDay.dt);
                String tempUnit = preferencesManager.getTemperatureUnit();
                String windUnit = preferencesManager.getWindSpeedUnit();

                // Convert temperature
                double temp = bestDay.main.temp;
                if (tempUnit.equals("°F")) {
                    temp = (temp * 9/5) + 32;
                }

                // Convert wind speed
                double windSpeed = bestDay.wind.speed;
                if (windUnit.equals("mph")) {
                    windSpeed = windSpeed * 2.237; // Convert m/s to mph
                } else if (windUnit.equals("km/h")) {
                    windSpeed = windSpeed * 3.6; // Convert m/s to km/h
                }

                String message = String.format(Locale.getDefault(),
                    "Best biking conditions on %s\nTemperature: %.1f%s\nWind: %.1f %s\nScore: %d%%",
                    formattedDate,
                    temp, tempUnit,
                    windSpeed, windUnit,
                    bestDay.calculateBikeRideScore(preferencesManager)
                );

                NotificationManager.showNotification(
                    context,
                    "Best Bike Day Alert",
                    message
                );
            }
        });
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000));
    }
} 