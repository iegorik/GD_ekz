package com.example.gd_ekz.Grebenkin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gd_ekz.Grebenkin.MainActivity;
import com.example.gd_ekz.R;

public class ProfileFragment extends Fragment {

    private TextView textViewEmail;
    private Button buttonLogout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext());
        textViewEmail = view.findViewById(R.id.textViewEmail);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        // Отображаем email текущего пользователя
        String email = sessionManager.getUserEmail();
        textViewEmail.setText("Email: " + (email != null ? email : "Неизвестно"));

        buttonLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}