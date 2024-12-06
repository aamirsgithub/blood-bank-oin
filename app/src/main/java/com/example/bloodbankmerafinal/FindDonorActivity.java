package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import android.util.Log;

public class FindDonorActivity extends AppCompatActivity {

    private Spinner bloodGroupSpinner;
    private EditText cityEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_donor1);

        // Initialize views
        bloodGroupSpinner = findViewById(R.id.spinner_blood_group);
        cityEditText = findViewById(R.id.edit_text_location);
        searchButton = findViewById(R.id.button_search);

        // Populate the spinner with blood group options
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodGroups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapter);

        // Handle Search button click
        searchButton.setOnClickListener(v -> {
            String selectedBloodGroup = bloodGroupSpinner.getSelectedItem().toString();
            String city = cityEditText.getText().toString().trim();

            if (city.isEmpty()) {
                Toast.makeText(FindDonorActivity.this, "Please enter your city", Toast.LENGTH_SHORT).show();
            } else {
                searchDonors(selectedBloodGroup, city);
            }
        });
    }

    private void searchDonors(String bloodGroup, String city) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<HashMap<String, String>> matchingUsers = new ArrayList<>();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    try {
                        // Retrieve the data from the snapshot
                        String bloodGroupFromDb = snapshot.child("bloodGroup").getValue(String.class);
                        String cityFromDb = snapshot.child("city").getValue(String.class);
                        Boolean isDonor = snapshot.child("isDonor").getValue(Boolean.class);

                        // Log each user's data for debugging
                        Log.d("FirebaseData", "Blood Group from DB: " + bloodGroupFromDb);
                        Log.d("FirebaseData", "City from DB: " + cityFromDb);
                        Log.d("FirebaseData", "Is Donor: " + isDonor);

                        // Null check for retrieved data
                        if (bloodGroupFromDb != null && cityFromDb != null && isDonor != null) {
                            if (bloodGroup.equalsIgnoreCase(bloodGroupFromDb) && city.equalsIgnoreCase(cityFromDb) && Boolean.TRUE.equals(isDonor)) {
                                // Create a HashMap with the user details
                                HashMap<String, String> userDetails = new HashMap<>();
                                userDetails.put("userId", Objects.requireNonNull(snapshot.child("userId").getValue()).toString());
                                userDetails.put("name", snapshot.child("name").getValue(String.class));
                                userDetails.put("email", snapshot.child("email").getValue(String.class));
                                userDetails.put("phone", snapshot.child("phone").getValue(String.class));
                                userDetails.put("bloodGroup", bloodGroupFromDb);
                                userDetails.put("city", cityFromDb);
                                userDetails.put("address", snapshot.child("address").getValue(String.class));
                                Integer noOfUnits = Integer.parseInt(Objects.requireNonNull(snapshot.child("noOfUnits").getValue()).toString());
                                userDetails.put("noOfUnits", (noOfUnits != null) ? noOfUnits.toString() : "0");

                                // Log the user details
                                Log.d("FirebaseData", "User Details: " + userDetails);

                                // Add to the matching users list
                                matchingUsers.add(userDetails);
                            }
                        } else {
                            Log.d("FirebaseData", "Missing required data for user: " + snapshot.getKey());
                        }
                    } catch (Exception e) {
                        Log.e("FirebaseError", "Error processing snapshot for user: " + snapshot.getKey(), e);
                    }
                }

                // Check if there are any matching users
                if (!matchingUsers.isEmpty()) {
                    // Pass the data to the next activity
                    Intent intent = new Intent(FindDonorActivity.this, FindDonor2.class);
                    intent.putExtra("USER_LIST", (Serializable) matchingUsers);
                    startActivity(intent);
                } else {
                    // Show a message if no donors were found
                    Toast.makeText(FindDonorActivity.this, "No donors found for the selected blood group and city.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show an error message if data retrieval failed
                Log.e("FirebaseError", "Failed to retrieve data from database.");
                Toast.makeText(FindDonorActivity.this, "Failed to retrieve data from database.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // Handle any issues with the database request itself
            Log.e("FirebaseError", "Failed to execute database query", e);
            Toast.makeText(FindDonorActivity.this, "Database request failed. Please try again.", Toast.LENGTH_SHORT).show();
        });
    }
}



//package com.example.bloodbankmerafinal;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import android.util.Log;
//
//public class FindDonorActivity extends AppCompatActivity {
//
//    private Spinner bloodGroupSpinner;
//    private EditText cityEditText;
//    private Button searchButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.find_donor1);
//
//        // Initialize views
//        bloodGroupSpinner = findViewById(R.id.spinner_blood_group);
//        cityEditText = findViewById(R.id.edit_text_location);
//        searchButton = findViewById(R.id.button_search);
//
//        // Populate the spinner with blood group options
//        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodGroups);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        bloodGroupSpinner.setAdapter(adapter);
//
//        // Handle Search button click
//        searchButton.setOnClickListener(v -> {
//            String selectedBloodGroup = bloodGroupSpinner.getSelectedItem().toString();
//            String city = cityEditText.getText().toString().trim();
//
//            if (city.isEmpty()) {
//                Toast.makeText(FindDonorActivity.this, "Please enter your city", Toast.LENGTH_SHORT).show();
//            } else {
//                searchDonors(selectedBloodGroup, city);
//            }
//        });
//    }
//
//
//
//    private void searchDonors(String bloodGroup, String city) {
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//        usersRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult() != null) {
//                List<HashMap<String, String>> matchingUsers = new ArrayList<>();
//
//                for (DataSnapshot snapshot : task.getResult().getChildren()) {
//                    // Retrieve the data from the snapshot
//                    String bloodGroupFromDb = snapshot.child("bloodGroup").getValue(String.class);
//                    String cityFromDb = snapshot.child("city").getValue(String.class);
//                    Boolean isDonor = snapshot.child("isDonor").getValue(Boolean.class);
//
//                    // Log each user's data for debugging
//                    Log.d("FirebaseData", "Blood Group from DB: " + bloodGroupFromDb);
//                    Log.d("FirebaseData", "City from DB: " + cityFromDb);
//                    Log.d("FirebaseData", "Is Donor: " + isDonor);
//
//                    if (bloodGroup.equalsIgnoreCase(bloodGroupFromDb) && city.equalsIgnoreCase(cityFromDb) && Boolean.TRUE.equals(isDonor)) {
//                        // Create a HashMap with the user details
//                        HashMap<String, String> userDetails = new HashMap<>();
//                        userDetails.put("name", snapshot.child("name").getValue(String.class));
//                        userDetails.put("email", snapshot.child("email").getValue(String.class));
//                        userDetails.put("phone", snapshot.child("phone").getValue(String.class));
//                        userDetails.put("bloodGroup", bloodGroupFromDb);
//                        userDetails.put("city", cityFromDb);
//                        userDetails.put("address", snapshot.child("address").getValue(String.class));
//                        userDetails.put("noOfUnits", snapshot.child("noOfUnits").getValue(Integer.class).toString());
//
//                        // Log the user details
//                        Log.d("FirebaseData", "User Details: " + userDetails);
//
//                        // Add to the matching users list
//                        matchingUsers.add(userDetails);
//                    }
//                }
//
//                // Check if there are any matching users
//                if (!matchingUsers.isEmpty()) {
//                    // Pass the data to the next activity
//                    Intent intent = new Intent(FindDonorActivity.this, FindDonor2.class);
//                    intent.putExtra("USER_LIST", (Serializable) matchingUsers);
//                    startActivity(intent);
//                } else {
//                    // Show a message if no donors were found
//                    Toast.makeText(FindDonorActivity.this, "No donors found for the selected blood group and city.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                // Show an error message if data retrieval failed
//                Toast.makeText(FindDonorActivity.this, "Failed to retrieve data from database.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}
