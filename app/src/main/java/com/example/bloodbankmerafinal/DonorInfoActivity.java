package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DonorInfoActivity extends AppCompatActivity {

    // Declare UI components
    private EditText nameEditText, bloodGroupEditText, unitsEditText, phoneEditText, emailEditText, addressEditText, cityEditText;

    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_info); // Ensure this matches your XML file name

        // Initialize UI components
        nameEditText = findViewById(R.id.editText_name);
        bloodGroupEditText = findViewById(R.id.editText_blood_group);
        unitsEditText = findViewById(R.id.editText_number_of_units);
        phoneEditText = findViewById(R.id.editText_phone_number);
        emailEditText = findViewById(R.id.editText_email);
        addressEditText = findViewById(R.id.editText_address);
        cityEditText = findViewById(R.id.editText_city);
        backButton = findViewById(R.id.backButton);

        // Retrieve donor data from Intent extras
        String name = getIntent().getStringExtra("NAME");
        String bloodGroup = getIntent().getStringExtra("BLOOD_GROUP");
        String units = getIntent().getStringExtra("NO_OF_UNITS");
        String phone = getIntent().getStringExtra("PHONE");
        String email = getIntent().getStringExtra("EMAIL");
        String address = getIntent().getStringExtra("ADDRESS");
        String city = getIntent().getStringExtra("CITY");

        if (name != null) {
            // Populate the EditText fields with the donor details
            nameEditText.setText(name);
            bloodGroupEditText.setText(bloodGroup);
            unitsEditText.setText(units);
            phoneEditText.setText(phone);
            emailEditText.setText(email);
            addressEditText.setText(address);
            cityEditText.setText(city);
        } else {
            Toast.makeText(this, "No donor details available", Toast.LENGTH_SHORT).show();
        }


//        // Redirect to Intro Page
//        backButton.setOnClickListener(v ->
//                startActivity(new Intent(DonorInfoActivity.this, FindDonor2.class))
//        );
    }
}
