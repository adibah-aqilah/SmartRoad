package com.example.smartroad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnSignIn;
    private Button btnGoogleSignIn;
    private TextView tvSignUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the Java controller to your XML layout file
        setContentView(R.layout.activity_login);

        // Initialise UI elements by linking them to their XML IDs
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        // 1. Standard Sign In Button Listener
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Simple validation check before passing onto next task stages
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // For Task 1 (Frontend), bypass validation and jump straight to Map View
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Closes LoginActivity so users cannot press back to return here
                }
            }
        });

        // 2. Google Sign In Button Placeholder (To be integrated fully in Task 2)
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Google Sign-In integration coming soon in Task 2!", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Sign Up text link listener
        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Registration screen coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}