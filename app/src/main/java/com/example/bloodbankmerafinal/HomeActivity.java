package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    // Declare variables for UI elements
    private ImageView notificationIcon, locationIcon;
    private TextView homepageTitleLocation;
    private ImageView homeIcon, findDonorIcon, requestIcon, profileIcon;

    // Firebase references
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page); // Connect to your layout file

        // Initialize UI elements
        notificationIcon = findViewById(R.id.imageView10);
        locationIcon = findViewById(R.id.imageView15);
        homepageTitleLocation = findViewById(R.id.homepage_title_location);

        homeIcon = findViewById(R.id.imageView11);
        findDonorIcon = findViewById(R.id.imageView12);
        requestIcon = findViewById(R.id.imageView13);
        profileIcon = findViewById(R.id.imageView14);

        // Fetching the current user ID from Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            fetchCityFromDatabase(); // Fetch and display city
        }

        // Set click listeners for icons and buttons
        notificationIcon.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
                    startActivity(intent);
        });

        locationIcon.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Location clicked", Toast.LENGTH_SHORT).show()
        );

        homeIcon.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Already on Homepage!", Toast.LENGTH_SHORT).show();
        });

        findDonorIcon.setOnClickListener(v -> {
            // Redirect to FindDonorActivity
            Intent intent = new Intent(HomeActivity.this, FindDonorActivity.class);
            startActivity(intent);
        });

        requestIcon.setOnClickListener(v -> {
            // Redirect to Request Form Activity
            Intent intent = new Intent(HomeActivity.this, Form0Activity.class);
            startActivity(intent);
        });

        profileIcon.setOnClickListener(v -> {
            // Redirect to Profile Activity
            Intent intent = new Intent(HomeActivity.this, com.example.bloodbankmerafinal.UserProfileActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Fetch the user's city from the database and display it in the TextView.
     */
    private void fetchCityFromDatabase() {
        databaseReference.child("city").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String city = task.getResult().getValue(String.class);

                if (city != null) {
                    // Set the city in the TextView
                    homepageTitleLocation.setText(city);
                } else {
                    // If the city is null, set a default message or leave it empty
                    homepageTitleLocation.setText("City not available");
                }
            } else {
                // If the task fails, show an error message
                Toast.makeText(HomeActivity.this, "Failed to fetch city", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
