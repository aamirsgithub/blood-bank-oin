package com.example.bloodbankmerafinal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbankmerafinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etCity, etPhoneNumber, etBloodGroup, etAddress;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize UI components
        etName = findViewById(R.id.editTextName); // Adjust IDs as per XML
        etEmail = findViewById(R.id.editTextEmail);
        etCity = findViewById(R.id.editTextCity);
        etPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        etBloodGroup = findViewById(R.id.editTextBloodGroup);
        etAddress = findViewById(R.id.editTextAddress);

        // Reference Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Fetch and display user profile data
        fetchUserProfileData();
    }

    private void fetchUserProfileData() {
        String userId = "USER_ID"; // Replace with actual user ID logic
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etName.setText(snapshot.child("name").getValue(String.class));
                    etEmail.setText(snapshot.child("email").getValue(String.class));
                    etCity.setText(snapshot.child("city").getValue(String.class));
                    etPhoneNumber.setText(snapshot.child("phone").getValue(String.class));
                    etBloodGroup.setText(snapshot.child("bloodGroup").getValue(String.class));
                    etAddress.setText(snapshot.child("address").getValue(String.class));
                } else {
                    Toast.makeText(UserProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
