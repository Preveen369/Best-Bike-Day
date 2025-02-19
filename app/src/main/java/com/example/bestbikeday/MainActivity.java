package com.example.bestbikeday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestbikeday.api.GeocodingService;
import com.example.bestbikeday.api.WeatherService;
import com.example.bestbikeday.model.GeocodingResponse;
import com.example.bestbikeday.model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "2ae3d7d77fe05a46d73babfbc4fe0d7c";
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String KEY_DARK_MODE = "dark_mode";
    private RecyclerView weatherRecyclerView;
    private WeatherAdapter weatherAdapter;
    private EditText searchEditText;
    private ImageButton clearButton;
    private List<WeatherResponse.ForecastItem> currentForecasts = new ArrayList<>();
    private WeatherDataManager weatherDataManager;
    private String locationName;
    private LinearLayout placeholderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        applyTheme();
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize locationName with a default value or from saved state
        locationName = "Default Location"; // Replace with actual logic to set location

        // Initialize views
        placeholderLayout = findViewById(R.id.placeholderLayout);
        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(this, locationName);
        weatherRecyclerView.setAdapter(weatherAdapter);

        searchEditText = findViewById(R.id.searchEditText);
        ImageButton searchButton = findViewById(R.id.searchButton);
        clearButton = findViewById(R.id.clearButton);
        setupSearchBar(searchButton);

        // Restore weather data if available
        if (!currentForecasts.isEmpty()) {
            weatherAdapter.setWeatherData(currentForecasts);
        }

        weatherDataManager = WeatherDataManager.getInstance(this);
        PreferencesManager preferencesManager = new PreferencesManager(this);

        // Create notification channel
        NotificationManager.createNotificationChannel(this);

        // Check if there is data to display
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the weather display if we have data
        if (weatherAdapter != null && !currentForecasts.isEmpty()) {
            weatherAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        themeItem.setIcon(isDarkMode() ? R.drawable.ic_light_mode : R.drawable.ic_dark_mode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_theme) {
            toggleTheme();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
            isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void toggleTheme() {
        boolean newDarkMode = !isDarkMode();
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_DARK_MODE, newDarkMode);
        editor.apply();

        // Store current forecasts before recreating activity
        currentForecasts = new ArrayList<>(currentForecasts);

        AppCompatDelegate.setDefaultNightMode(
            newDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private boolean isDarkMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    private void setupSearchBar(ImageButton searchButton) {
        // Handle search button click
        searchButton.setOnClickListener(v -> performSearch());

        // Handle clear button
        clearButton.setOnClickListener(v -> {
            searchEditText.setText("");
            clearButton.setVisibility(View.GONE);
            searchEditText.requestFocus();
            
            // Show keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);

            // Show placeholder when search is cleared
            showPlaceholder();
        });

        // Show/hide clear button based on text
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle keyboard enter
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String userInput = searchEditText.getText().toString().trim();
        if (!userInput.isEmpty()) {
            searchLocation(userInput);
        }
    }

    private void searchLocation(String cityName) {
        searchEditText.setEnabled(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeocodingService geocodingService = retrofit.create(GeocodingService.class);
        Call<List<GeocodingResponse>> call = geocodingService.getCoordinates(
                cityName, 1, API_KEY);

        call.enqueue(new Callback<List<GeocodingResponse>>() {
            @Override
            public void onResponse(Call<List<GeocodingResponse>> call, 
                                 Response<List<GeocodingResponse>> response) {
                searchEditText.setEnabled(true);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GeocodingResponse location = response.body().get(0);
                    locationName = location.name;
                    if (location.state != null) {
                        locationName += ", " + location.state;
                    }
                    if (location.country != null) {
                        locationName += ", " + location.country;
                    }
                    searchEditText.setText(locationName);
                    fetchWeatherData(location.lat, location.lon, locationName);
                } else {
                    showError("Location not found. Please check the spelling.");
                }
            }

            @Override
            public void onFailure(Call<List<GeocodingResponse>> call, Throwable t) {
                searchEditText.setEnabled(true);
                showError("Please check your internet connection.");
            }
        });
    }

    private void fetchWeatherData(double lat, double lon, String locationName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getWeatherForecast(
                lat, lon, API_KEY, "metric"
        );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentForecasts = response.body().list;
                    updateUI(); // Update UI after data is fetched
                } else {
                    showError("Unable to get weather data. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                showError("Please check your internet connection.");
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupWeatherAdapter(String locationName) {
        weatherAdapter = new WeatherAdapter(this, locationName);
        weatherRecyclerView.setAdapter(weatherAdapter);
    }

    private void callSetupWeatherAdapter(String locationName) {
        setupWeatherAdapter(locationName);
    }

    private void updateUI() {
        if (currentForecasts.isEmpty()) {
            showPlaceholder();
        } else {
            showWeatherData();
        }
    }

    private void showPlaceholder() {
        placeholderLayout.setVisibility(View.VISIBLE);
        weatherRecyclerView.setVisibility(View.GONE);
    }

    private void showWeatherData() {
        placeholderLayout.setVisibility(View.GONE);
        weatherRecyclerView.setVisibility(View.VISIBLE);
        weatherAdapter.setWeatherData(currentForecasts);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Exit Application")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes", (dialog, which) -> {
                // Exit the application
                finishAffinity(); // Close all activities and exit the app
            })
            .setNegativeButton("No", null)
            .show();
    }
}