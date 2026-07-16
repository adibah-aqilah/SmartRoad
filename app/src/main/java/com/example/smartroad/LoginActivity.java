package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private Button btnSignIn;
    private Button btnGoogleSignIn;

    private TextView tvSignUpLink;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        tvSignUpLink = findViewById(R.id.tvSignUpLink);


        btnSignIn.setOnClickListener(v -> {

            String email =
                    etEmail.getText()
                            .toString()
                            .trim();

            String password =
                    etPassword.getText()
                            .toString()
                            .trim();

            if (email.isEmpty()) {

                etEmail.setError(
                        "Email is required"
                );

                return;
            }

            if (password.isEmpty()) {

                etPassword.setError(
                        "Password is required"
                );

                return;
            }

            mAuth.signInWithEmailAndPassword(
                            email,
                            password
                    )
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            startActivity(
                                    new Intent(
                                            LoginActivity.this,
                                            MainActivity.class
                                    )
                            );

                            finish();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Invalid Email or Password",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        btnGoogleSignIn.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Google Sign-In Coming Soon",
                    Toast.LENGTH_SHORT
            ).show();

        });

        tvSignUpLink.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Registration Page Coming Soon",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

}