package com.example.hostelhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_Password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("HostelHub", MODE_PRIVATE);

        checkSession();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Register button clicked!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(view -> {
            String Email = etEmail.getText().toString();
            String Password = etPassword.getText().toString();

            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            } else {
                if (Email.equals("admin@gmail.com") && Password.equals("admin")) {
                    saveSession("admin");
                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", user.getDisplayName() != null ? user.getDisplayName() : "User");
                                editor.putString("email", user.getEmail());
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                saveSession("user");
                                Intent intent = new Intent(LoginActivity.this, FacilitesDisplay.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                                auth.signOut();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Email and password!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void checkSession() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String userType = sharedPreferences.getString("userType", "");

        if (isLoggedIn) {
            if ("admin".equals(userType)) {
                navigateToActivity(AdminHomeActivity.class);
            } else if ("user".equals(userType)) {
                navigateToActivity(FacilitesDisplay.class);
            }
        }
    }

    private void saveSession(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userType", userType);
        editor.apply();
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        startActivity(intent);
        finish();
    }
}
