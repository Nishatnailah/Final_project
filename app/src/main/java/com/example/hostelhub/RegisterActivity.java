package com.example.hostelhub;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername,etEmail,etPassword,etconPassword;
    private String Username,Email,Password,conPassword,regi;
    private Button btnLogin,btnRegister;
    private Pattern etUsernamePattern = Pattern.compile("[a-z A-Z._]+");
    private Pattern etEmailPattern=Pattern.compile("[a-zA-Z\\d._%+-]+@(gmail|yahoo)\\.com");
    private Pattern etPasswordPattern=Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");;
    private Pattern etconPasswordpattern=Pattern.compile("[a-zA-Z\\d]+");

    private FirebaseAuth auth;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth=FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("HostelHub", MODE_PRIVATE);


        etUsername=findViewById(R.id.et_Register_username);
        etEmail=findViewById(R.id.et_Register_email);
        etPassword=findViewById(R.id.et_Register_Password);
        etconPassword=findViewById(R.id.et_Register_con_Password);
        btnLogin=findViewById(R.id.btn_login);
        btnRegister=findViewById(R.id.btn_register);
        dbHelper = new DatabaseHelper(RegisterActivity.this);





        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = etUsername.getText().toString();
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();
                conPassword = etconPassword.getText().toString();

                if (Username.isEmpty()) {
                    etUsername.setError("Empty!");
                    etUsername.requestFocus();
                } else if (!etUsernamePattern.matcher(Username).matches()) {
                    etUsername.setError("Name can be only Alphabet");
                    etUsername.requestFocus();
                } else if (Email.isEmpty()) {
                    etEmail.setError("Empty!");
                    etEmail.requestFocus();
                } else if (!etEmailPattern.matcher(Email).matches()) {
                    etEmail.setError("Invalid email format! Use gmail or yahoo.");
                    etEmail.requestFocus();
                } else if (Password.isEmpty()) {
                    etPassword.setError("Empty");
                    etPassword.requestFocus();
                } else if (!etPasswordPattern.matcher(Password).matches()) {
                    etPassword.setError("Invalid password and Ensures at least 8 characters!!");
                    etPassword.requestFocus();
                } else if (!Password.equals(conPassword)) {
                    etconPassword.setError("Passwords do not match!");
                    etconPassword.requestFocus();
                } else {




                    auth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {

                                        user.updateProfile(new UserProfileChangeRequest.Builder()
                                                .setDisplayName(Username)
                                                .build());


                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", Username);
                                        editor.putString("email", Email);
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.apply();

                                        user.sendEmailVerification()
                                                .addOnCompleteListener(emailTask -> {
                                                    if (emailTask.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(RegisterActivity.this, "Login button clicked!!", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }

        );

            }

    private void storeUserInSQLite(String Username, String Email, String Password,String conPassword) {
        boolean isInserted = dbHelper.insertUser(Username, Email, Password, conPassword);
        if (isInserted) {
            Toast.makeText(RegisterActivity.this, "User data stored in SQLite successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Failed to store user data in SQLite", Toast.LENGTH_SHORT).show();
        }
    }
}