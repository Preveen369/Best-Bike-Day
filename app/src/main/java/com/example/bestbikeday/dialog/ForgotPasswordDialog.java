package com.example.bestbikeday.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.bestbikeday.R;
import com.example.bestbikeday.auth.AuthManager;

public class ForgotPasswordDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot_password, null);
        EditText emailEditText = view.findViewById(R.id.emailEditText);

        builder.setView(view)
                .setTitle("Reset Password")
                .setPositiveButton("Reset", (dialog, id) -> {
                    String email = emailEditText.getText().toString().trim();
                    if (email.isEmpty()) {
                        Toast.makeText(getContext(), 
                            "Please enter your email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    AuthManager authManager = new AuthManager();
                    authManager.resetPassword(email, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), 
                                "Password reset email sent", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), 
                                "Failed to send reset email: " + 
                                task.getException().getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        return builder.create();
    }
} 