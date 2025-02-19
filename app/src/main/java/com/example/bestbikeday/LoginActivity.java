package com.example.bestbikeday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bestbikeday.auth.AuthManager;
import com.example.bestbikeday.dialog.ForgotPasswordDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar progressBar;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager();

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> handleLogin());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);
        forgotPasswordText.setOnClickListener(v -> {
            ForgotPasswordDialog dialog = new ForgotPasswordDialog();
            dialog.show(getSupportFragmentManager(), "ForgotPasswordDialog");
        });
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInput(email, password)) return;

        showLoading(true);
        authManager.signIn(email, password, task -> {
            showLoading(false);
            if (task.isSuccessful()) {
                startMainActivity();
            } else {
                Toast.makeText(LoginActivity.this, 
                    "Authentication failed: " + task.getException().getMessage(),
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
        registerButton.setEnabled(!show);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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