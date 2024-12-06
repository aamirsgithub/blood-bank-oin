package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameField, emailField, phoneField, cityField, bloodGroupField, addressField, passwordField, confirmPasswordField;
    private Button signUpButton;
    private com.google.android.gms.common.SignInButton googleSignUpButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private GoogleSignInClient googleSignInClient;

    private int userIdCounter = 0; // Counter for userId auto-increment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize fields
        nameField = findViewById(R.id.editTextText5);
        emailField = findViewById(R.id.editTextText7);
        phoneField = findViewById(R.id.editTextText2);
        cityField = findViewById(R.id.editTextText10);
        bloodGroupField = findViewById(R.id.editTextText11);
        addressField = findViewById(R.id.editTextText12);
        passwordField = findViewById(R.id.editTextText3);
        confirmPasswordField = findViewById(R.id.editTextText4);
        signUpButton = findViewById(R.id.button);
        googleSignUpButton = findViewById(R.id.googleSignInButton);

        // Set up Google Sign-In options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Load userIdCounter from Firebase
        loadUserIdCounter();

        // Sign-Up Button Listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Google Sign-Up Button Listener
        googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void loadUserIdCounter() {
        databaseReference.child("userIdCounter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userIdCounter = snapshot.getValue(Integer.class);
                } else {
                    userIdCounter = 0; // Default value if not present
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load userIdCounter: " + error.getMessage());
            }
        });
    }

    private void registerUser() {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String bloodGroup = bloodGroupField.getText().toString().trim();
        String address = addressField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Validation checks
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(city) || TextUtils.isEmpty(bloodGroup) || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("[0-9]{11}")) {
            Toast.makeText(this, "Please enter a valid 11-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bloodGroup.matches("^(A|B|AB|O)[+-]$")) {
            Toast.makeText(this, "Please enter a valid blood group (e.g., O+, AB-)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String firebaseUserId = firebaseAuth.getCurrentUser().getUid();
                            incrementUserIdAndSaveUser(firebaseUserId, name, email, phone, city, bloodGroup, address, password);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void incrementUserIdAndSaveUser(String firebaseUserId, String name, String email, String phone, String city, String bloodGroup, String address, String password) {
        // Increment userIdCounter
        userIdCounter++;

        // Update the userIdCounter in Firebase
        databaseReference.child("userIdCounter").setValue(userIdCounter);

        // Creating a map for the user's information
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userIdCounter);
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("city", city);
        user.put("bloodGroup", bloodGroup);
        user.put("address", address);
        user.put("password", password);  // Store the password
        user.put("isDonor", false);  // Default isDonor value
        user.put("noOfUnits", 0);    // Default number of units donated

        // Saving the user data to Firebase Realtime Database
        databaseReference.child(firebaseUserId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String firebaseUserId = firebaseAuth.getCurrentUser().getUid();
                            incrementUserIdAndSaveUser(firebaseUserId, account.getDisplayName(), account.getEmail(), "", "", "", "", "");
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
