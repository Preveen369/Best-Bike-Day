package com.example.bestbikeday;

public class WeatherData {
    private String location;
    private double temperature;
    private double windSpeed;
    private int bikeDayScore;

    public WeatherData() {
        // Default constructor required for calls to DataSnapshot.getValue(WeatherData.class)
    }

    public WeatherData(String location, double temperature, double windSpeed, int bikeDayScore) {
        this.location = location;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.bikeDayScore = bikeDayScore;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getBikeDayScore() {
        return bikeDayScore;
    }

    public void setBikeDayScore(int bikeDayScore) {
        this.bikeDayScore = bikeDayScore;
    }
} 