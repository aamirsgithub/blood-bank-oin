package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        TextView backButton;

        backButton = findViewById(R.id.backButton);


        // Redirect to Intro Page
        backButton.setOnClickListener(v ->
                startActivity(new Intent(AboutUsActivity.this, IntroActivity.class))
        );

    }
}
