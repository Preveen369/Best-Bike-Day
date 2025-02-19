package com.example.bestbikeday.api;

import com.example.bestbikeday.model.GeocodingResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingService {
    @GET("geo/1.0/direct")
    Call<List<GeocodingResponse>> getCoordinates(
        @Query("q") String cityName,
        @Query("limit") int limit,
        @Query("appid") String apiKey
    );
} 