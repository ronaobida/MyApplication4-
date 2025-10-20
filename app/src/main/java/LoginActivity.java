package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // --- Firebase Auth ---
    private FirebaseAuth mAuth;

    // --- Views for UI Toggling ---
    private View loginForm;
    private View signupForm;
    private Button loginToggleButton;
    private Button signupToggleButton;

    // --- Views for the Login Form ---
    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private Button loginSubmitButton;

    // --- Views for the Sign-up Form ---
    private EditText signupUsernameEditText; // This can be used to set a display name later
    private EditText signupEmailEditText;
    private EditText signupPasswordEditText;
    private Button signupSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupClickListeners();
        showLoginForm();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, go to the main activity
            // You might want to add logic here to differentiate between owners and subscribers
            goToOwnerActivity();
        }
    }

    private void initializeViews() {
        loginForm = findViewById(R.id.loginForm);
        signupForm = findViewById(R.id.signupForm);
        loginToggleButton = findViewById(R.id.button);
        signupToggleButton = findViewById(R.id.button2);

        loginEmailEditText = findViewById(R.id.login_email);
        loginPasswordEditText = findViewById(R.id.login_password);
        loginSubmitButton = findViewById(R.id.login_submit_button);

        signupUsernameEditText = findViewById(R.id.fullname);
        signupEmailEditText = findViewById(R.id.email);
        signupPasswordEditText = findViewById(R.id.password);
        signupSubmitButton = findViewById(R.id.buttonSignUpSubmit);
    }

    private void setupClickListeners() {
        loginToggleButton.setOnClickListener(v -> showLoginForm());
        signupToggleButton.setOnClickListener(v -> showSignupForm());
        loginSubmitButton.setOnClickListener(v -> handleLoginSubmit());
        signupSubmitButton.setOnClickListener(v -> handleSignupSubmit());
    }

    private void handleLoginSubmit() {
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        goToOwnerActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleSignupSubmit() {
        String email = signupEmailEditText.getText().toString().trim();
        String password = signupPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Signup successful! You can now log in.", Toast.LENGTH_SHORT).show();
                        showLoginForm(); // Switch back to the login form
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Signup failed. Try another email.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToOwnerActivity() {
        Intent intent = new Intent(LoginActivity.this, ownerActivity.class);
        startActivity(intent);
        finish(); // Call finish to prevent user from returning to login screen with back button
    }

    // --- UI Toggling Methods (no changes needed here) ---

    private void showLoginForm() {
        loginForm.setVisibility(View.VISIBLE);
        signupForm.setVisibility(View.GONE);
        loginToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
        loginToggleButton.setTextColor(getResources().getColor(android.R.color.black));
        signupToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_dark));
        signupToggleButton.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void showSignupForm() {
        loginForm.setVisibility(View.GONE);
        signupForm.setVisibility(View.VISIBLE);
        signupToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
        signupToggleButton.setTextColor(getResources().getColor(android.R.color.black));
        loginToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_dark));
        loginToggleButton.setTextColor(getResources().getColor(android.R.color.white));
    }
}
