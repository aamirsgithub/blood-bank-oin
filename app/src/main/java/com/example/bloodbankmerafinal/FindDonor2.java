
package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindDonor2 extends AppCompatActivity {

    private TextView bloodGrp, location, donorInfo, donorLocation, timeLimit, donorAddress, bloodType;
    private Button requestButton;
    private ImageView callIcon;
    private TextView backIcon;
    private ArrayList<HashMap<String, String>> donorList;  // Using HashMap to hold donor data
    private int currentDonorIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_donor2); // Ensure this is called first!
        RecyclerView recyclerView = findViewById(R.id.recyclerView);  // Make sure the ID matches the one in your layout file

        // Initialize UI components
        initializeUI();

        // Retrieve the global userId from UserSession
        Integer currentUserId = UserSession.getInstance().getUserId();  // Get userId from UserSession
        if (currentUserId != null) {
            Log.d("FindDonor2", "Current User ID: " + currentUserId);
        } else {
            Log.e("FindDonor2", "User ID not found in session");
        }

        // Get donor list from the Intent
        donorList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("USER_LIST");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DonorAdapter donorAdapter = new DonorAdapter(donorList);
        recyclerView.setAdapter(donorAdapter);

        // Check if donor data is received properly
        if (donorList != null && !donorList.isEmpty()) {
            Log.d("FindDonor2", "Received donor data");
            populateDonorDetails(currentDonorIndex); // Populate the first donor
        } else {
            Log.e("FindDonor2", "No donor data received");
//            Toast.makeText(this, "No donor data available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Back button logic
        if (backIcon != null) {
            backIcon.setOnClickListener(v -> finish());
        } else {
            Log.e("FindDonor2", "backIcon is null");
        }

        // Request button logic
        if (requestButton != null) {
            requestButton.setOnClickListener(v -> {
                // Get the userId of the current logged-in user
                if (currentUserId != null && donorList != null && !donorList.isEmpty()) {
                    // Get the donor being requested
                    HashMap<String, String> donor = donorList.get(currentDonorIndex);
                    Toast.makeText(FindDonor2.this, "Requesting donor with ID: " + donor.get("userId"), Toast.LENGTH_SHORT).show();
                    // Send the request directly to Firebase
                    sendRequestToFirebase(currentUserId, donor.get("userId"));

                    // Show confirmation message
                } else {
                    Toast.makeText(FindDonor2.this, "Failed to send request. Invalid data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("FindDonor2", "requestButton is null");
        }

        // Call icon logic
        if (callIcon != null) {
            callIcon.setOnClickListener(v -> navigateToDonorInfoActivity());
        } else {
            Log.e("FindDonor2", "callIcon is null");
        }
    }

    private void initializeUI() {
        bloodGrp = findViewById(R.id.blood_grp);
        location = findViewById(R.id.textView7);
        donorInfo = findViewById(R.id.textView_donorInfo);
        donorLocation = findViewById(R.id.textView_donorLocation);
        timeLimit = findViewById(R.id.textView_timeLimit);
        donorAddress = findViewById(R.id.textView_address);
        bloodType = findViewById(R.id.textView_bloodType);
        requestButton = findViewById(R.id.button_request);
        callIcon = findViewById(R.id.icon_call);
        backIcon = findViewById(R.id.back);
    }

    private void populateDonorDetails(int index) {
        Log.d("FindDonor2", "Populating donor details for index: " + index);
        if (donorList == null || index < 0 || index >= donorList.size()) {
            Log.e("FindDonor2", "Invalid donor index or donor list is null");
            return;
        }

        HashMap<String, String> donor = donorList.get(index);

        // Set values to the UI components from the HashMap
        if (bloodGrp != null) bloodGrp.setText("Blood Group: " + donor.get("bloodGroup"));
        if (location != null) location.setText("City: " + donor.get("city"));
        if (donorInfo != null) donorInfo.setText("Name: " + donor.get("name") + ", " + "Email: " + donor.get("email"));
        if (donorLocation != null) donorLocation.setText("Phone: " + donor.get("phone"));
        if (donorAddress != null) donorAddress.setText("Address: " + donor.get("address"));
        if (timeLimit != null) timeLimit.setText("Units Required: " + donor.get("noOfUnits"));
        if (bloodType != null) bloodType.setText(donor.get("bloodGroup"));
    }

    private void navigateToDonorInfoActivity() {
        if (donorList == null || currentDonorIndex < 0 || currentDonorIndex >= donorList.size()) {
            Log.e("FindDonor2", "Invalid donor index or donor list is null");
            return;
        }

        HashMap<String, String> donor = donorList.get(currentDonorIndex);
        Intent donorInfoIntent = new Intent(FindDonor2.this, DonorInfoActivity.class);

        // Pass donor details to the next activity
        donorInfoIntent.putExtra("NAME", donor.get("name"));
        donorInfoIntent.putExtra("EMAIL", donor.get("email"));
        donorInfoIntent.putExtra("PHONE", donor.get("phone"));
        donorInfoIntent.putExtra("BLOOD_GROUP", donor.get("bloodGroup"));
        donorInfoIntent.putExtra("CITY", donor.get("city"));
        donorInfoIntent.putExtra("ADDRESS", donor.get("address"));
        donorInfoIntent.putExtra("NO_OF_UNITS", donor.get("noOfUnits"));
        startActivity(donorInfoIntent);
    }

    private void sendRequestToFirebase(Integer requesterId, String donorId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("requests");

        // Prepare request data
        HashMap<String, Object> requestData = new HashMap<>();
        requestData.put("requserId", requesterId);
        requestData.put("donorId", donorId);
        requestData.put("status", "pending");
        requestData.put("requestTime", System.currentTimeMillis());

        // Log the request data before sending it
        Log.d("FindDonor2", "Sending request data: " + requestData.toString());

        // Push request data to Firebase
        databaseRef.push().setValue(requestData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Request successfully sent to Firebase.");
                        Toast.makeText(FindDonor2.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Firebase", "Failed to send request to Firebase: " + task.getException().getMessage());
                        Toast.makeText(FindDonor2.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}






//package com.example.bloodbankmerafinal;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class FindDonor2 extends AppCompatActivity {
//
//    private TextView bloodGrp, location, donorInfo, donorLocation, timeLimit, donorAddress, bloodType;
//    private Button requestButton;
//    private ImageView callIcon;
//    private TextView backIcon;
//    private ArrayList<HashMap<String, String>> donorList;  // Using HashMap to hold donor data
//    private int currentDonorIndex = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.find_donor2); // Ensure this is called first!
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);  // Make sure the ID matches the one in your layout file
//
//        // Initialize UI components
//        initializeUI();
//
//        // Retrieve the global userId from UserSession
//        String currentUserId = UserSession.getInstance().getUserId();  // Get userId from UserSession
//        if (currentUserId != null) {
//            Log.d("FindDonor2", "Current User ID: " + currentUserId);
//        } else {
//            Log.e("FindDonor2", "User ID not found in session");
//        }
//
//        // Get donor list from the Intent
//        donorList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("USER_LIST");
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DonorAdapter donorAdapter = new DonorAdapter(donorList);
//        recyclerView.setAdapter(donorAdapter);
//
//        // Check if donor data is received properly
//        if (donorList != null && !donorList.isEmpty()) {
//            Log.d("FindDonor2", "Received donor data");
//            populateDonorDetails(currentDonorIndex); // Populate the first donor
//        } else {
//            Log.e("FindDonor2", "No donor data received");
//            Toast.makeText(this, "No donor data available", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Back button logic
//        if (backIcon != null) {
//            backIcon.setOnClickListener(v -> finish());
//        } else {
//            Log.e("FindDonor2", "backIcon is null");
//        }
//
//        // Request button logic
//        if (requestButton != null) {
//            requestButton.setOnClickListener(v -> {
//                // Get the userId of the current logged-in user
//                if (currentUserId != null && donorList != null && !donorList.isEmpty()) {
//                    // Get the donor being requested
//                    HashMap<String, String> donor = donorList.get(currentDonorIndex);
//                    Toast.makeText(FindDonor2.this, currentUserId, Toast.LENGTH_SHORT).show();
//                    // Send the request directly to Firebase
//                    sendRequestToFirebase(currentUserId, donor.get("userId"));
//
//                    // Show confirmation message
//
//                } else {
//                    Toast.makeText(FindDonor2.this, "Failed to send request", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Log.e("FindDonor2", "requestButton is null");
//        }
//
//        // Call icon logic
//        if (callIcon != null) {
//            callIcon.setOnClickListener(v -> navigateToDonorInfoActivity());
//        } else {
//            Log.e("FindDonor2", "callIcon is null");
//        }
//    }
//
//    private void initializeUI() {
//        bloodGrp = findViewById(R.id.blood_grp);
//        location = findViewById(R.id.textView7);
//        donorInfo = findViewById(R.id.textView_donorInfo);
//        donorLocation = findViewById(R.id.textView_donorLocation);
//        timeLimit = findViewById(R.id.textView_timeLimit);
//        donorAddress = findViewById(R.id.textView_address);
//        bloodType = findViewById(R.id.textView_bloodType);
//        requestButton = findViewById(R.id.button_request);
//        callIcon = findViewById(R.id.icon_call);
//        backIcon = findViewById(R.id.back);
//    }
//
//    private void populateDonorDetails(int index) {
//        Log.d("FindDonor2", "Populating donor details for index: " + index);
//        if (donorList == null || index < 0 || index >= donorList.size()) {
//            Log.e("FindDonor2", "Invalid donor index or donor list is null");
//            return;
//        }
//
//        HashMap<String, String> donor = donorList.get(index);
//
//        // Set values to the UI components from the HashMap
//        if (bloodGrp != null) bloodGrp.setText("Blood Group: " + donor.get("bloodGroup"));
//        if (location != null) location.setText("City: " + donor.get("city"));
//        if (donorInfo != null) donorInfo.setText("Name: " + donor.get("name") + ", " + "Email: " + donor.get("email"));
//        if (donorLocation != null) donorLocation.setText("Phone: " + donor.get("phone"));
//        if (donorAddress != null) donorAddress.setText("Address: " + donor.get("address"));
//        if (timeLimit != null) timeLimit.setText("Units Required: " + donor.get("noOfUnits"));
//        if (bloodType != null) bloodType.setText(donor.get("bloodGroup"));
//    }
//
//    private void navigateToDonorInfoActivity() {
//        if (donorList == null || currentDonorIndex < 0 || currentDonorIndex >= donorList.size()) {
//            Log.e("FindDonor2", "Invalid donor index or donor list is null");
//            return;
//        }
//
//        HashMap<String, String> donor = donorList.get(currentDonorIndex);
//        Intent donorInfoIntent = new Intent(FindDonor2.this, DonorInfoActivity.class);
//
//        // Pass donor details to the next activity
//        donorInfoIntent.putExtra("NAME", donor.get("name"));
//        donorInfoIntent.putExtra("EMAIL", donor.get("email"));
//        donorInfoIntent.putExtra("PHONE", donor.get("phone"));
//        donorInfoIntent.putExtra("BLOOD_GROUP", donor.get("bloodGroup"));
//        donorInfoIntent.putExtra("CITY", donor.get("city"));
//        donorInfoIntent.putExtra("ADDRESS", donor.get("address"));
//        donorInfoIntent.putExtra("NO_OF_UNITS", donor.get("noOfUnits"));
//        startActivity(donorInfoIntent);
//    }
//
//    private void sendRequestToFirebase(String requesterId, String donorId) {
//        // Get the Firebase database reference for the "requests" node
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("requests");
//
//        // Check if donorList is null or empty
//        if (donorList == null || donorList.isEmpty()) {
//            Log.e("FindDonor2", "Donor list is empty or null.");
//            return;
//        }
//
//        // Check if currentUserId is null or empty
//        String currentUserId = UserSession.getInstance().getUserId();
//        if (currentUserId == null || currentUserId.isEmpty()) {
//            Log.e("FindDonor2", "User ID is null or empty.");
//            return;
//        }
//
//        // Get the selected donor data
//        HashMap<String, String> donor = donorList.get(currentDonorIndex);
//        if (donor == null) {
//            Log.e("FindDonor2", "Selected donor is null.");
//            return;
//        }
//
//        // Log the donor details and userId
//        Log.d("FindDonor2", "Current user ID: " + currentUserId);
//        Log.d("FindDonor2", "Donor user ID: " + donor.get("userId"));
//
//        // Prepare request data
//        HashMap<String, Object> requestData = new HashMap<>();
//        requestData.put("requser", currentUserId);
//        requestData.put("donor", donor.get("userId"));
//        requestData.put("status", "pending");
//
//        // Log the request data before sending it
//        Log.d("FindDonor2", "Sending request data: " + requestData.toString());
//
//        // Push request data to Firebase
//        databaseRef.push().setValue(requestData)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("Firebase", "Request successfully sent to Firebase.");
//
//                    } else {
//                        Log.e("Firebase", "Failed to send request to Firebase: " + task.getException().getMessage());
//                        Toast.makeText(FindDonor2.this, "Failed to send request", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//
//
//}
