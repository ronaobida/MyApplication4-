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

import com.example.myapplication.R;

public class subscriberActivity extends AppCompatActivity {
    private ImageButton subsMessButton, subsNotifButton, subsPaymentButton;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscriber_activity);

        fragmentManager = getSupportFragmentManager();
        initializeButtons();
        setupClickListeners();
    }

    private void initializeButtons() {
        subsMessButton = findViewById(R.id.imageButton6);
        subsNotifButton = findViewById(R.id.imageButton9);
        subsPaymentButton = findViewById(R.id.imageButton7);
    }
    private void setupClickListeners() {
        subsMessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new com.example.myapplication.subsmess());
               // setActiveButton(homeButton);
            }
        });
        subsNotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new com.example.myapplication.subsnotif());
                // setActiveButton(homeButton);
            }
        });
        subsPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new com.example.myapplication.subspayment());
                // setActiveButton(homeButton);
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}

