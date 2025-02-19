package com.example.bestbikeday.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthManager {
    private FirebaseAuth mAuth;

    public AuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Sign up new user
    public void signUp(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(listener);
    }

    // Sign in existing user
    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(listener);
    }

    // Send password reset email
    public void resetPassword(String email, OnCompleteListener<Void> listener) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(listener);
    }

    // Sign out user
    public void signOut() {
        mAuth.signOut();
    }
}