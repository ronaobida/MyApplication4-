package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    // --- URLs for your API ---
    private static final String LOGIN_URL = "http://192.168.4.107/sparx-api/login.php";
    private static final String SIGNUP_URL = "http://192.168.4.107/sparx-api/signup.php";

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
    private EditText signupUsernameEditText;
    private EditText signupEmailEditText;
    private EditText signupPasswordEditText;
    private Button signupSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initializeViews();
        setupClickListeners();
        showLoginForm();
    }

    /**
     * Finds all the UI components from the login_activity.xml file.
     */
    private void initializeViews() {
        // Form containers
        loginForm = findViewById(R.id.loginForm);
        signupForm = findViewById(R.id.signupForm);

        // Toggle buttons
        loginToggleButton = findViewById(R.id.button);
        signupToggleButton = findViewById(R.id.button2);

        // =================== THIS IS THE FIX ===================
        // Login form fields - IDs are now actually corrected.
        // Make sure your XML file has these IDs: login_email, login_password, login_submit_button
        loginEmailEditText = findViewById(R.id.login_email);
        loginPasswordEditText = findViewById(R.id.login_password);
        loginSubmitButton = findViewById(R.id.login_submit_button);
        // =======================================================

        // Sign-up form fields
        signupUsernameEditText = findViewById(R.id.fullname);
        signupEmailEditText = findViewById(R.id.email);
        signupPasswordEditText = findViewById(R.id.password);
        signupSubmitButton = findViewById(R.id.buttonSignUpSubmit);
    }

    /**
     * Sets up the OnClickListener for each button.
     */
    private void setupClickListeners() {
        // Use a null check to prevent crashes if a view is not found
        if (loginToggleButton != null) {
            loginToggleButton.setOnClickListener(v -> showLoginForm());
        }
        if (signupToggleButton != null) {
            signupToggleButton.setOnClickListener(v -> showSignupForm());
        }
        if (loginSubmitButton != null) {
            loginSubmitButton.setOnClickListener(v -> handleLoginSubmit());
        }
        if (signupSubmitButton != null) {
            signupSubmitButton.setOnClickListener(v -> handleSignupSubmit());
        }
    }

    // --- UI Toggling Methods ---

    private void showLoginForm() {
        if (loginForm != null) loginForm.setVisibility(View.VISIBLE);
        if (signupForm != null) signupForm.setVisibility(View.GONE);
        
        // Update button colors - Login clicked (white), Sign Up not clicked (orange)
        if (loginToggleButton != null) {
            loginToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
            loginToggleButton.setTextColor(getResources().getColor(android.R.color.black));
        }
        if (signupToggleButton != null) {
            signupToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_dark));
            signupToggleButton.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private void showSignupForm() {
        if (loginForm != null) loginForm.setVisibility(View.GONE);
        if (signupForm != null) signupForm.setVisibility(View.VISIBLE);
        
        // Update button colors - Sign Up clicked (white), Login not clicked (orange)
        if (signupToggleButton != null) {
            signupToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
            signupToggleButton.setTextColor(getResources().getColor(android.R.color.black));
        }
        if (loginToggleButton != null) {
            loginToggleButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_dark));
            loginToggleButton.setTextColor(getResources().getColor(android.R.color.white));
        }
    }


    // --- Form Submission Handlers ---

    private void handleLoginSubmit() {
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        performLogin(email, password);
    }

    private void handleSignupSubmit() {
        String username = signupUsernameEditText.getText().toString().trim();
        String email = signupEmailEditText.getText().toString().trim();
        String password = signupPasswordEditText.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        performSignup(username, email, password);
    }


    // --- Background Network Methods ---

    private void performLogin(String email, String password) {
        String params = "username=" + urlEncode(email) + "&password=" + urlEncode(password);
        makeNetworkRequest(LOGIN_URL, params, true); // isLogin = true
    }

    private void performSignup(String username, String email, String password) {
        String params = "username=" + urlEncode(username) + "&email=" + urlEncode(email) + "&password=" + urlEncode(password);
        makeNetworkRequest(SIGNUP_URL, params, false); // isLogin = false
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private void makeNetworkRequest(String urlString, String params, boolean isLogin) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String response = doInBackground(urlString, params);
            handler.post(() -> {
                handleServerResponse(response, isLogin);
            });
        });
    }

    private String doInBackground(String urlString, String params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = params.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleServerResponse(String response, boolean isLogin) {
        if (response == null) {
            Toast.makeText(this, "Action failed. Check server connection or URL.", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            String message = jsonResponse.getString("message");

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            if (success) {
                if (isLogin) {
                    Intent intent = new Intent(LoginActivity.this, ownerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showLoginForm();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
