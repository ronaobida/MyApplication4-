package com.example.myapplication;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import com.example.myapplication.models.User;
import com.example.myapplication.services.FirestoreService;

import java.util.List;

public class homenav extends Fragment {

    private FirestoreService firestoreService;
    private TextView activeTechCount, checkedInCount, pendingRequestsCount, newMessagesCount;
    private CardView activeTechCard, checkedInCard, messagesCard;

    @NonNull
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homenav_fragment, container, false);

        // Initialize Firestore service
        firestoreService = new FirestoreService();

        // Initialize views
        activeTechCount = view.findViewById(R.id.active_tech_count);
        checkedInCount = view.findViewById(R.id.checked_in_count);
        pendingRequestsCount = view.findViewById(R.id.pending_requests_count);
        newMessagesCount = view.findViewById(R.id.new_messages_count);

        activeTechCard = view.findViewById(R.id.active_tech_card);
        checkedInCard = view.findViewById(R.id.checked_in_card);
        messagesCard = view.findViewById(R.id.messages_card);

        // Set up card click listeners
        if (activeTechCard != null) {
            activeTechCard.setOnClickListener(v -> showActiveTechnicians());
        }
        if (checkedInCard != null) {
            checkedInCard.setOnClickListener(v -> showCheckedInTechnicians());
        }
        if (messagesCard != null) {
            messagesCard.setOnClickListener(v -> openMessages());
        }

        // Load data
        loadDashboardData();

        // MOVE THIS CODE HERE (before return)
        Fragment mapFragment = getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open full-screen map
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view; // This should be last
    }

    private void loadDashboardData() {
        // Load active technicians
        firestoreService.getUsersByRole("technician",
            users -> {
                int activeCount = 0;
                int checkedInNum = 0;
                for (User user : users) {
                    if (user.isActive()) {
                        activeCount++;
                        // For now, assume all active are checked in
                        checkedInNum++;
                    }
                }
                if (activeTechCount != null) {
                    activeTechCount.setText(String.valueOf(activeCount));
                }
                if (this.checkedInCount != null) {
                    this.checkedInCount.setText(checkedInNum + "/" + users.size());
                }
            },
            e -> {
                Toast.makeText(getContext(), "Failed to load technician data", Toast.LENGTH_SHORT).show();
            }
        );

        // For now, set static values for pending requests and messages
        if (pendingRequestsCount != null) {
            pendingRequestsCount.setText("3");
        }
        if (newMessagesCount != null) {
            newMessagesCount.setText("2");
        }
    }

    private void showActiveTechnicians() {
        firestoreService.getUsersByRole("technician",
            users -> {
                StringBuilder message = new StringBuilder("Active Technicians:\n");
                boolean hasActive = false;
                for (User user : users) {
                    if (user.isActive()) {
                        message.append("- ").append(user.getName()).append("\n");
                        hasActive = true;
                    }
                }
                if (!hasActive) {
                    message = new StringBuilder("No active technicians");
                }
                Toast.makeText(getContext(), message.toString(), Toast.LENGTH_LONG).show();
            },
            e -> {
                Toast.makeText(getContext(), "Failed to load active technicians", Toast.LENGTH_SHORT).show();
            }
        );
    }

    private void showCheckedInTechnicians() {
        firestoreService.getUsersByRole("technician",
            users -> {
                StringBuilder message = new StringBuilder("Checked-in Technicians:\n");
                boolean hasCheckedIn = false;
                for (User user : users) {
                    if (user.isActive()) { // Assuming active means checked in
                        message.append("- ").append(user.getName()).append("\n");
                        hasCheckedIn = true;
                    }
                }
                if (!hasCheckedIn) {
                    message = new StringBuilder("No technicians checked in");
                }
                Toast.makeText(getContext(), message.toString(), Toast.LENGTH_LONG).show();
            },
            e -> {
                Toast.makeText(getContext(), "Failed to load checked-in technicians", Toast.LENGTH_SHORT).show();
            }
        );
    }

    private void openMessages() {
        // Navigate to messages fragment
        if (getActivity() instanceof com.example.myapplication.ownerActivity) {
            ((com.example.myapplication.ownerActivity) getActivity()).activateMessagesTab();
        }
    }
}
