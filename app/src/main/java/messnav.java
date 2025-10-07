package com.example.myapplication;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

public class messnav extends Fragment {
    @NonNull
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messnav_fragment, container, false);
        return view;
    }
}
