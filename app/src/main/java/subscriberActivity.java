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
import com.google.firebase.auth.FirebaseAuth;

public class subscriberActivity extends AppCompatActivity {
    private ImageButton subsMessButton, subsNotifButton, subsPaymentButton;
    private FragmentManager fragmentManager;
    private FrameLayout menuOverlay;
    private LinearLayout menuDrawer;
    private boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscriber_activity);

        fragmentManager = getSupportFragmentManager();
        initializeButtons();
        setupClickListeners();
        setupMenuDrawer();
    }

    private void initializeButtons() {
        subsMessButton = findViewById(R.id.imageButton6);
        subsNotifButton = findViewById(R.id.imageButton9);
        subsPaymentButton = findViewById(R.id.imageButton7);
    }

    private void setupClickListeners() {
        subsMessButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.subsmess());
        });
        subsNotifButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.subsnotif());
        });
        subsPaymentButton.setOnClickListener(v -> {
            loadFragment(new com.example.myapplication.subspayment());
        });

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

    private void setupMenuDrawer() {
        menuOverlay = findViewById(R.id.menu_overlay);
        menuDrawer = findViewById(R.id.menu_drawer);

        findViewById(R.id.menu_logout).setOnClickListener(v -> {
            closeMenu();
            // Perform logout
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(subscriberActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        menuOverlay.setOnClickListener(v -> closeMenu());
    }

    private void openMenu() {
        if (!isMenuOpen) {
            menuOverlay.setVisibility(View.VISIBLE);
            menuDrawer.setTranslationX(300f);

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
