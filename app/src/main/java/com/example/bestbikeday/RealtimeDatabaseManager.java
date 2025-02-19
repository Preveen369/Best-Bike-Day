package com.example.bestbikeday;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeDatabaseManager {
    private DatabaseReference mDatabase;

    public RealtimeDatabaseManager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void writeWeatherData(String location, double temperature, double windSpeed, int bikeDayScore) {
        // Create a unique key for each entry
        String key = mDatabase.child("weatherData").push().getKey();
        if (key != null) {
            WeatherData weatherData = new WeatherData(location, temperature, windSpeed, bikeDayScore);
            mDatabase.child("weatherData").child(key).setValue(weatherData);
        }
    }
} 