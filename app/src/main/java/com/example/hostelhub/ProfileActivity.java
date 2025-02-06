package com.example.hostelhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvUsername = findViewById(R.id.tv_username);
        TextView tvEmail = findViewById(R.id.tv_email);


        sharedPreferences = getSharedPreferences("HostelHub", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "Guest");
        String email = sharedPreferences.getString("email", "Not available");

        tvUsername.setText("Username: " + username);
        tvEmail.setText("Email: " + email);
    }
}
