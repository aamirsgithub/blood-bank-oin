
package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Form0Activity extends AppCompatActivity {

    private EditText editTextNumberOfUnits;
    private Button submitButton;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_test0);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        usersRef = database.getReference("users");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("FormAnswers", MODE_PRIVATE);

        // Initialize views
        editTextNumberOfUnits = findViewById(R.id.editTextNumberOfUnits);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(view -> {
            // Get the entered number of units
            String numberOfUnits = editTextNumberOfUnits.getText().toString().trim();

            // Validate the input
            if (numberOfUnits.isEmpty()) {
                Toast.makeText(Form0Activity.this, "Please enter the number of units", Toast.LENGTH_SHORT).show();
            } else {
                // Save the number of units in SharedPreferences for later use in Form 4
                saveNumberOfUnitsToSharedPreferences(numberOfUnits);

                // Navigate to Form1Activity
                Intent intent = new Intent(Form0Activity.this, Form1Activity.class);
                startActivity(intent);
                finish(); // Optionally finish the current activity
            }
        });
    }

    private void saveNumberOfUnitsToSharedPreferences(String numberOfUnits) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numberOfUnits", numberOfUnits);
        editor.apply(); // Save to SharedPreferences
    }
}






//package com.example.bloodbankmerafinal;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class Form0Activity extends AppCompatActivity {
//
//    private EditText editTextNumberOfUnits;
//    private Button submitButton;
//    private FirebaseDatabase database;
//    private DatabaseReference usersRef;
//    private FirebaseAuth auth;
//
//    private SharedPreferences noOfUnitsForForm4;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.request_test0);
//
//        // Initialize Firebase
//        database = FirebaseDatabase.getInstance();
//        auth = FirebaseAuth.getInstance();
//        usersRef = database.getReference("users");
//
//        // Initialize views
//        editTextNumberOfUnits = findViewById(R.id.editTextNumberOfUnits);
//        submitButton = findViewById(R.id.submitButton);
//
//        submitButton.setOnClickListener(view -> {
//            // Get the entered number of units
//            String numberOfUnits = editTextNumberOfUnits.getText().toString().trim();
//
//            // Validate the input
//            if (numberOfUnits.isEmpty()) {
//                Toast.makeText(Form0Activity.this, "Please enter the number of units", Toast.LENGTH_SHORT).show();
//            } else {
//                // Save to Firebase Realtime Database
//                saveNumberOfUnitsToFirebase(numberOfUnits);
//            }
//        });
//    }
//
//    private void saveNumberOfUnitsToFirebase(String numberOfUnits) {
//        // Get the current user ID (ensure user is logged in)
//        String userId = auth.getCurrentUser().getUid();  // Firebase Auth user ID
//
//        // Reference to the user's data in the "users" node
//        DatabaseReference userRef = usersRef.child(userId);
//
//        // Save the number of units under the user's data
//        userRef.child("noOfUnits").setValue(numberOfUnits)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(Form0Activity.this, "Units saved successfully", Toast.LENGTH_SHORT).show();
//
//                        // Navigate to Form1Activity after saving data
//                        Intent intent = new Intent(Form0Activity.this, Form1Activity.class);
//                        startActivity(intent);
//                        finish(); // Optionally finish the current activity
//                    } else {
//                        Toast.makeText(Form0Activity.this, "Failed to save units", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}
