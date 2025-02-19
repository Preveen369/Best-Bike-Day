package com.example.bestbikeday.model;

import com.example.bestbikeday.PreferencesManager;

import java.util.List;

public class WeatherResponse {
    public List<ForecastItem> list;
    public City city;

    public static class City {
        public String name;
    }

    public static class ForecastItem {
        public long dt;
        public Main main;
        public List<Weather> weather;
        public Wind wind;
        public Rain rain;

        public int calculateBikeRideScore(PreferencesManager prefs) {
            // Temperature is already in Celsius from API
            double tempInCelsius = main.temp;
            
            // Wind speed is already in m/s from API
            double windInMS = wind.speed;
            
            // Calculate scores using metric units
            int tempScore = calculateTemperatureScore(tempInCelsius);
            int windScore = calculateWindScore(windInMS);
            int rainScore = calculateRainScore(rain);

            // Equal weights for all factors (33.33% each)
            return (int) ((tempScore * 0.33) + (windScore * 0.33) + (rainScore * 0.34));
        }

        private int calculateTemperatureScore(double tempCelsius) {
            // Score based on Celsius
            if (tempCelsius >= 15 && tempCelsius <= 25) {
                return 100;    // Perfect temperature (15-25°C)
            } else if (tempCelsius >= 10 && tempCelsius < 15 || tempCelsius > 25 && tempCelsius <= 30) {
                return 75;     // Good temperature (10-15°C or 25-30°C)
            } else if (tempCelsius >= 5 && tempCelsius < 10 || tempCelsius > 30 && tempCelsius <= 35) {
                return 50;     // Fair temperature (5-10°C or 30-35°C)
            } else if (tempCelsius >= 0 && tempCelsius < 5 || tempCelsius > 35 && tempCelsius <= 40) {
                return 25;     // Poor temperature (0-5°C or 35-40°C)
            } else {
                return 0;      // Extreme temperature (<0°C or >40°C)
            }
        }

        private int calculateWindScore(double windSpeedMS) {
            // Score based on m/s
            if (windSpeedMS <= 5.5) {         // Up to 20 km/h
                return 100;    // Light breeze
            } else if (windSpeedMS <= 8.3) {  // Up to 30 km/h
                return 75;     // Moderate wind
            } else if (windSpeedMS <= 11.1) { // Up to 40 km/h
                return 50;     // Strong wind
            } else if (windSpeedMS <= 13.9) { // Up to 50 km/h
                return 25;     // Very strong wind
            } else {
                return 0;      // Dangerous wind
            }
        }

        private int calculateRainScore(Rain rain) {
            // Rain is always in mm
            if (rain == null || rain.threeHour == 0) {
                return 100;    // No rain
            } else if (rain.threeHour < 0.5) {
                return 75;     // Light rain
            } else if (rain.threeHour < 2.0) {
                return 50;     // Moderate rain
            } else if (rain.threeHour < 4.0) {
                return 25;     // Heavy rain
            } else {
                return 0;      // Very heavy rain
            }
        }
    }

    public static class Main {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public int humidity;
    }

    public static class Weather {
        public String main;
        public String description;
        public String icon;
    }

    public static class Wind {
        public double speed;
        public int deg;
    }

    public static class Rain {
        public double threeHour;
    }
} 