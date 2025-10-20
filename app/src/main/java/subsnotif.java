package com.example.myapplication;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.tasks.Task;
import com.example.myapplication.tasks.TaskRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class subsnotif extends Fragment{
    private LinearLayout notificationsContainer;

    @NonNull
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subsnotif_fragment, container, false);
        notificationsContainer = view.findViewById(R.id.notifications_container);
        loadNotifications();
        return view;
    }

    private void loadNotifications() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<Task> tasks = TaskRepository.getAll();
        notificationsContainer.removeAllViews();

        for (Task t : tasks) {
            if (t.subscriberId != null && t.subscriberId.equals(currentUserId)) {
                addNotification(t);
            }
        }
    }

    private void addNotification(Task task) {
        View notificationView = getLayoutInflater().inflate(R.layout.row_notification, notificationsContainer, false);
        TextView title = notificationView.findViewById(R.id.txtTitle);
        TextView desc = notificationView.findViewById(R.id.txtBody);
        TextView time = notificationView.findViewById(R.id.txtTime);

        title.setText(task.title);
        desc.setText(task.description);
        time.setText("Just now"); // Placeholder

        notificationsContainer.addView(notificationView);
    }
}
