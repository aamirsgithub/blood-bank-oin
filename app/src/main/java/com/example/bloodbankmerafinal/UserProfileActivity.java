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
import android.util.Log;

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
        Integer userId = UserSession.getInstance().getUserId();
        Log.d("UserProfile", "User ID: " + userId); // Check if User ID is being retrieved

        if (userId == null) {
            Toast.makeText(UserProfileActivity.this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next(); // Get the first (and only) match
                    etName.setText(userSnapshot.child("name").getValue(String.class));
                    etEmail.setText(userSnapshot.child("email").getValue(String.class));
                    etCity.setText(userSnapshot.child("city").getValue(String.class));
                    etPhoneNumber.setText(userSnapshot.child("phone").getValue(String.class));
                    etBloodGroup.setText(userSnapshot.child("bloodGroup").getValue(String.class));
                    etAddress.setText(userSnapshot.child("address").getValue(String.class));
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
