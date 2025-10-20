package com.example.myapplication;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;

public class subsmess extends Fragment{
    @NonNull
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subsmess_fragment, container, false);

        TextView clickableItem = view.findViewById(R.id.text_view_clickable_item);
        clickableItem.setOnClickListener(v -> {
            com.example.myapplication.subsconvo subsconvo = new com.example.myapplication.subsconvo();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, subsconvo);
                transaction.commit();

        });

        return view;
    }
}


