package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class ownerActivity extends AppCompatActivity {

    // Navigation buttons
    private ImageButton homeButton, staffButton, subsButton, messButton, reportButton;
    
    // Fragment manager for handling fragments
    private FragmentManager fragmentManager;
    
    // Menu drawer variables
    private FrameLayout menuOverlay;
    private LinearLayout menuDrawer;
    private boolean isMenuOpen = false;
    private TextView menuUsername, menuUserEmail, welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity);

        // Initialize fragment manager
        fragmentManager = getSupportFragmentManager();

        // Initialize navigation buttons
        initializeButtons();

        // Set up click listeners
        setupClickListeners();
        
        // Set up menu drawer
        setupMenuDrawer();
        
        // Load default fragment (home)
        loadFragment(new com.example.myapplication.homenav());
    }

    private void initializeButtons() {
        homeButton = findViewById(R.id.imageButton5);
        staffButton = findViewById(R.id.imageButton2);
        subsButton = findViewById(R.id.imageButton3);
        messButton = findViewById(R.id.imageButton6);
        reportButton = findViewById(R.id.imageButton7);
    }

    private void setupClickListeners() {
        homeButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.homenav());
            setActiveButton(homeButton);
        });

        staffButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.staffnav());
            setActiveButton(staffButton);
        });

        subsButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.subsnav());
            setActiveButton(subsButton);
        });

        messButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.messnav());
            setActiveButton(messButton);
        });

        reportButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.reportnav());
            setActiveButton(reportButton);
        });
        
        // Menu button click listener
        ImageButton menuButton = findViewById(R.id.imageButton10);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void setActiveButton(ImageButton activeButton) {
        // Reset all buttons to normal state
        resetButtonState(homeButton);
        resetButtonState(staffButton);
        resetButtonState(subsButton);
        resetButtonState(messButton);
        resetButtonState(reportButton);
        
        // Highlight the active button and move it up
        highlightButton(activeButton);
    }
    
    private void resetButtonState(ImageButton button) {
        button.setAlpha(0.6f);
        button.animate()
            .translationY(0f)
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(300)
            .setInterpolator(new android.view.animation.OvershootInterpolator(0.5f))
            .start();
    }
    
    private void highlightButton(ImageButton button) {
        button.setAlpha(1.0f);
        button.animate()
            .translationY(-25f)  // Move up more dramatically
            .scaleX(1.2f)        // Bigger scale for more pop effect
            .scaleY(1.2f)        // Bigger scale for more pop effect
            .setDuration(300)
            .setInterpolator(new android.view.animation.OvershootInterpolator(1.2f))
            .start();
    }
    
    private void setupMenuDrawer() {
        menuOverlay = findViewById(R.id.menu_overlay);
        menuDrawer = findViewById(R.id.menu_drawer);
        menuUsername = findViewById(R.id.menu_username);
        menuUserEmail = findViewById(R.id.menu_useremail);
        welcomeText = findViewById(R.id.textView2);
        
        // Set up menu item click listeners
        findViewById(R.id.menu_home).setOnClickListener(v -> {
            closeMenu();
            loadFragment(new com.example.myapplication.homenav());
            setActiveButton(homeButton);
        });
        
        findViewById(R.id.menu_account_settings).setOnClickListener(v -> {
            closeMenu();
            // TODO: Handle account settings click
        });
        
        findViewById(R.id.menu_about_us).setOnClickListener(v -> {
            closeMenu();
            // TODO: Handle about us click
        });
        
        findViewById(R.id.menu_logout).setOnClickListener(v -> {
            closeMenu();
            // Perform logout
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ownerActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        
        // Close menu when overlay is clicked
        menuOverlay.setOnClickListener(v -> closeMenu());
        
        // Set username from login (you can get this from SharedPreferences or Intent)
        setUsername("admin"); // Replace with actual username from login
    }
    
    private void setUsername(String username) {
        if (menuUsername != null) {
            menuUsername.setText(username);
        }
        if (welcomeText != null) {
            welcomeText.setText("Welcome, " + username);
        }
    }
    
    private void openMenu() {
        if (!isMenuOpen) {
            menuOverlay.setVisibility(View.VISIBLE);
            menuDrawer.setTranslationX(300f); // Start off-screen on the right
            
            Animation slideIn = new TranslateAnimation(300f, 0f, 0f, 0f);
            slideIn.setDuration(300);
            slideIn.setFillAfter(true);
            
            menuDrawer.startAnimation(slideIn);
            isMenuOpen = true;
        }
    }
    
    private void closeMenu() {
        if (isMenuOpen) {
            Animation slideOut = new TranslateAnimation(0f, 300f, 0f, 0f);
            slideOut.setDuration(300);
            slideOut.setFillAfter(true);
            
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                
                @Override
                public void onAnimationEnd(Animation animation) {
                    menuOverlay.setVisibility(View.GONE);
                    isMenuOpen = false;
                }
                
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            
            menuDrawer.startAnimation(slideOut);
        }
    }
}