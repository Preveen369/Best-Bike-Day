// This class is no longer used since we switched to current weather display
// You can safely delete this file 

package com.example.bestbikeday;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestbikeday.model.WeatherResponse.ForecastItem;
import com.example.bestbikeday.view.CircularScoreView;
import com.example.bestbikeday.RealtimeDatabaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<ForecastItem> forecasts = new ArrayList<>();
    private final PreferencesManager preferencesManager;
    private RealtimeDatabaseManager databaseManager;
    private String locationName;

    public WeatherAdapter(Context context, String locationName) {
        this.preferencesManager = new PreferencesManager(context);
        this.databaseManager = new RealtimeDatabaseManager();
        this.locationName = locationName;
    }

    public void setWeatherData(List<ForecastItem> newForecasts) {
        this.forecasts = newForecasts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        ForecastItem forecast = forecasts.get(position);
        holder.bind(forecast);

        // Convert temperature and wind speed if needed
        double temperature = forecast.main.temp;
        double windSpeed = forecast.wind.speed;
        String tempUnit = preferencesManager.getTemperatureUnit();
        String windUnit = preferencesManager.getWindSpeedUnit();

        if (tempUnit.equals("°F")) {
            temperature = (temperature * 9/5) + 32;
        }

        if (windUnit.equals("mph")) {
            windSpeed = windSpeed * 2.237; // Convert m/s to mph
        } else if (windUnit.equals("km/h")) {
            windSpeed = windSpeed * 3.6; // Convert m/s to km/h
        }

        int score = forecast.calculateBikeRideScore(preferencesManager);

        // Push data to Firebase using the actual location name
        databaseManager.writeWeatherData(locationName, temperature, windSpeed, score);
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final TextView tempText;
        private final TextView descriptionText;
        private final TextView detailsText;
        private final CircularScoreView bikeScoreView;
        private final View cardBackground;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            tempText = itemView.findViewById(R.id.tempText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            detailsText = itemView.findViewById(R.id.detailsText);
            bikeScoreView = itemView.findViewById(R.id.bikeScoreView);
            cardBackground = itemView.findViewById(R.id.cardBackground);
        }

        void bind(ForecastItem forecast) {
            String tempUnit = preferencesManager.getTemperatureUnit();
            String windUnit = preferencesManager.getWindSpeedUnit();
            
            double temperature = forecast.main.temp;
            double windSpeed = forecast.wind.speed;

            // Convert temperature if needed
            if (tempUnit.equals("°F")) {
                temperature = (temperature * 9/5) + 32;
            }

            // Convert wind speed if needed
            if (windUnit.equals("mph")) {
                windSpeed = windSpeed * 2.237; // Convert m/s to mph
            } else if (windUnit.equals("km/h")) {
                windSpeed = windSpeed * 3.6; // Convert m/s to km/h
            }

            // Format the temperature and wind speed with the correct units
            String temperatureText = String.format(Locale.getDefault(), "%.1f%s", temperature, tempUnit);
            String windText = String.format(Locale.getDefault(), "%.1f %s", windSpeed, windUnit);

            tempText.setText(temperatureText);
            detailsText.setText(String.format(Locale.getDefault(), "Wind: %s\nHumidity: %d%%", 
                windText, forecast.main.humidity));

            dateText.setText(formatDate(forecast.dt));
            descriptionText.setText(forecast.weather.get(0).description);
            
            int score = forecast.calculateBikeRideScore(preferencesManager);
            bikeScoreView.setScore(score);
            updateCardBackground(score);
        }

        private String formatDate(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d HH:mm", Locale.getDefault());
            return sdf.format(new Date(timestamp * 1000));
        }

        private void updateCardBackground(int score) {
            GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.BL_TR,
                new int[]{
                    getScoreStartColor(score),
                    getScoreEndColor(score)
                }
            );
            gradient.setCornerRadius(dpToPx(12));
            gradient.setAlpha(20);
            cardBackground.setBackground(gradient);
        }

        private int getScoreStartColor(int score) {
            if (score >= 80) {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_excellent_start);
            } else if (score >= 60) {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_good_start);
            } else if (score >= 40) {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_fair_start);
            } else {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_poor_start);
            }
        }

        private int getScoreEndColor(int score) {
            if (score >= 80) {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_excellent_end);
            } else if (score >= 60) {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_good_end);
            } else if (score >= 40) {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_fair_end);
            } else {
                return ContextCompat.getColor(itemView.getContext(), R.color.score_poor_end);
            }
        }

        private int dpToPx(int dp) {
            float density = itemView.getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }
} 