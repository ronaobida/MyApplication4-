<<<<<<<< HEAD:app/src/main/java/subspayment.java
package com.example.myapplication;

========
>>>>>>>> origin/main:app/src/main/java/subsnav.java
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.myapplication.R;
<<<<<<<< HEAD:app/src/main/java/subspayment.java
========

>>>>>>>> origin/main:app/src/main/java/subsnav.java

public class subspayment extends Fragment{
    @NonNull
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subspayment_fragment, container, false);
        return view;
    }
}

