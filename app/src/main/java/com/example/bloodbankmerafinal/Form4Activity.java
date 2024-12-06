package com.example.bloodbankmerafinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Form4Activity extends AppCompatActivity {

    private Button yesButton, noButton;
    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_test4); // Connecting with form 4 XML file

        yesButton = findViewById(R.id.button3);
        noButton = findViewById(R.id.button4);
        sharedPreferences = getSharedPreferences("FormAnswers", MODE_PRIVATE);

        // Get the current authenticated user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        } else {
            Log.e("Form4Activity", "User is not authenticated.");
            return;
        }

        yesButton.setOnClickListener(view -> {
            saveAnswer("form4", "YES");
            checkIfDonor();
        });

        noButton.setOnClickListener(view -> {
            saveAnswer("form4", "NO");
            checkIfDonor();
        });
    }

    private void saveAnswer(String formKey, String ans) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(formKey, ans);
        editor.apply();
        Log.d("Form4Activity", formKey + " saved: " + ans); // Log saved answer for debugging
    }

    private void checkIfDonor() {
        String form1Ans = sharedPreferences.getString("form1", "NO");
        String form2Ans = sharedPreferences.getString("form2", "NO");
        String form3Ans = sharedPreferences.getString("form3", "NO");
        String form4Ans = sharedPreferences.getString("form4", "NO");

        Log.d("FormAnswers", "Form1: " + form1Ans + " Form2: " + form2Ans + " Form3: " + form3Ans + " Form4: " + form4Ans);

        // Check if all answers are "YES" and update the isDonor field
        if (form1Ans.equals("YES") && form2Ans.equals("YES") && form3Ans.equals("YES") && form4Ans.equals("YES")) {
            updateIsDonor(true);
        } else {
            updateIsDonor(false);
        }
    }

    private void updateIsDonor(boolean isDonor) {
        // Ensure the device is connected to the internet
        if (!isNetworkConnected()) {
            Toast.makeText(Form4Activity.this, "No network connection. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the 'isDonor' field in the Firebase database
        databaseReference.child("isDonor").setValue(isDonor).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (isDonor) {
                    // Successfully registered as a donor
                    saveUnitsToFirebase(); // Now save the number of units if the user is a donor
                    Toast.makeText(Form4Activity.this, "You are now a registered donor!", Toast.LENGTH_SHORT).show();
                } else {
                    // Not eligible as a donor
                    Toast.makeText(Form4Activity.this, "Sorry! You cannot be registered as a donor.", Toast.LENGTH_SHORT).show();
                }
                navigateToHomePage();
            } else {
                // Log detailed error and show message
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("FirebaseError", "Error updating database: " + exception.getMessage());
                }
                Toast.makeText(Form4Activity.this, "Database update failed. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUnitsToFirebase() {
        // Retrieve the number of units from SharedPreferences
        String numberOfUnits = sharedPreferences.getString("numberOfUnits", "0");

        // Save the number of units to Firebase if the user is a donor
        if (!numberOfUnits.isEmpty()) {
            databaseReference.child("noOfUnits").setValue(numberOfUnits)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Form4Activity", "Number of units saved successfully.");
                        } else {
                            Log.e("Form4Activity", "Failed to save number of units.");
                        }
                    });
        }
    }

    private void navigateToHomePage() {
        Intent intent = new Intent(Form4Activity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish this activity
    }

    // Check if the device is connected to the internet
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}





//package com.example.bloodbankmerafinal;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class Form4Activity extends AppCompatActivity {
//
//    private Button yesButton, noButton;
//    private SharedPreferences sharedPreferences;
//    private DatabaseReference databaseReference;
//    private String userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.request_test4); // Connecting with form 4 XML file
//
//        yesButton = findViewById(R.id.button3);
//        noButton = findViewById(R.id.button4);
//        sharedPreferences = getSharedPreferences("FormAnswers", MODE_PRIVATE);
//
//        // Get the current authenticated user ID
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            userId = user.getUid();
//            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
//        } else {
//            Log.e("Form4Activity", "User is not authenticated.");
//            return;
//        }
//
//        yesButton.setOnClickListener(view -> {
//            saveAnswer("form4", "YES");
//            checkIfDonor();
//        });
//
//        noButton.setOnClickListener(view -> {
//            saveAnswer("form4", "NO");
//            checkIfDonor();
//        });
//    }
//
//    private void saveAnswer(String formKey, String ans) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(formKey, ans);
//        editor.apply();
//        Log.d("Form4Activity", formKey + " saved: " + ans); // Log saved answer for debugging
//    }
//
//    private void checkIfDonor() {
//        String form1Ans = sharedPreferences.getString("form1", "NO");
//        String form2Ans = sharedPreferences.getString("form2", "NO");
//        String form3Ans = sharedPreferences.getString("form3", "NO");
//        String form4Ans = sharedPreferences.getString("form4", "NO");
//
//        Log.d("FormAnswers", "Form1: " + form1Ans + " Form2: " + form2Ans + " Form3: " + form3Ans + " Form4: " + form4Ans);
//
//        // Check if all answers are "YES" and update the isDonor field
//        if (form1Ans.equals("YES") && form2Ans.equals("YES") && form3Ans.equals("YES") && form4Ans.equals("YES")) {
//            updateIsDonor(true);
//        } else {
//            updateIsDonor(false);
//        }
//    }
//
//    private void updateIsDonor(boolean isDonor) {
//        // Ensure the device is connected to the internet
//        if (!isNetworkConnected()) {
//            Toast.makeText(Form4Activity.this, "No network connection. Please try again later.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Update the 'isDonor' field in the Firebase database
//        databaseReference.child("isDonor").setValue(isDonor).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                if (isDonor) {
//                    // Successful donor registration
//                    Toast.makeText(Form4Activity.this, "You are now a registered donor!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Not eligible as donor
//                    Toast.makeText(Form4Activity.this, "Sorry! You cannot be registered as a donor.", Toast.LENGTH_SHORT).show();
//                }
//                navigateToHomePage();
//            } else {
//                // Log detailed error and show message
//                Exception exception = task.getException();
//                if (exception != null) {
//                    Log.e("FirebaseError", "Error updating database: " + exception.getMessage());
//                }
//                Toast.makeText(Form4Activity.this, "Database update failed. Please try again later.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void navigateToHomePage() {
//        Intent intent = new Intent(Form4Activity.this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }
//
//    // Check if the device is connected to the internet
//    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        return networkInfo != null && networkInfo.isConnected();
//    }
//}
