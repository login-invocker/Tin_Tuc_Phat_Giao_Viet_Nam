package com.invocker.phatgiaovietnam.ui.read;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.invocker.phatgiaovietnam.R;

public class ReadFragment extends Fragment {

    private ReadViewModel readViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        readViewModel =
                ViewModelProviders.of(this).get(ReadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_read, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        readViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
}