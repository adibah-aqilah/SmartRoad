package com.example.smartroad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button btnGithub = findViewById(R.id.btnGithub);

        // Click handler to open the GitHub web link externally in a browser window
        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String githubUrl = "https://github.com"; // Replace with your group's actual repository link later
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
                startActivity(intent);
            }
        });
    }
}