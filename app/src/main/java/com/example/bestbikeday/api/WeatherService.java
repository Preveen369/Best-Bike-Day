package com.example.bestbikeday.api;

import com.example.bestbikeday.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/forecast")
    Call<WeatherResponse> getWeatherForecast(
        @Query("lat") double lat,
        @Query("lon") double lon,
        @Query("appid") String apiKey,
        @Query("units") String units
    );
} 