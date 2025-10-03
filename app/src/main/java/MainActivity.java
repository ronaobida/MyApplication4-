package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Define the delay time (3.5 seconds)
    private static final long DELAY_TIME = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this Java file to your XML layout (activity_main.xml)
        setContentView(R.layout.main_activity);

        // 1. Get the layered logo parts
        ImageView sp = findViewById(R.id.logo_sp);
        ImageView a  = findViewById(R.id.logo_a);
        ImageView rx = findViewById(R.id.logo_rx);

        // 2. Load the different animations
        Animation fadeFast   = AnimationUtils.loadAnimation(this, R.anim.fade_in_fast);
        Animation fadeDelay  = AnimationUtils.loadAnimation(this, R.anim.fade_in_delayed);
        Animation fadeSlow   = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);

        // 3. Start the animations on their respective parts
        if (sp != null) sp.startAnimation(fadeFast);   // "Sp" fades in fast
        if (a  != null) a.startAnimation(fadeSlow);   // "a" fades in slow
        if (rx != null) rx.startAnimation(fadeDelay); // "rx" fades in with a delay

        // 4. Set a delay (3.5 seconds) before transitioning to the next screen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // START THE LOGIN ACTIVITY (THE NEW SCENE)
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                // Close the splash screen
                finish();
            }
        }, DELAY_TIME);
    }
}
