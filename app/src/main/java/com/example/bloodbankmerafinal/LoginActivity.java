


    package com.example.bloodbankmerafinal;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    import java.util.Objects;

    public class LoginActivity extends AppCompatActivity {

        private EditText emailField, passwordField;
        private Button loginButton;
        private TextView forgotPassword, signUpButton,backButton;

        private DatabaseReference databaseReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_page);

            // Initialize Database Reference
            databaseReference = FirebaseDatabase.getInstance().getReference("users");

            // Initialize UI elements
            emailField = findViewById(R.id.emailField);
            passwordField = findViewById(R.id.passwordField);
            loginButton = findViewById(R.id.loginButton);
            forgotPassword = findViewById(R.id.forgotPassword);
            signUpButton = findViewById(R.id.signUpButton);
            backButton = findViewById(R.id.backButton);

            // Handle Login Button Click
            loginButton.setOnClickListener(v -> {
                String input = emailField.getText().toString().trim(); // Email or phone
                String password = passwordField.getText().toString().trim();

                if (TextUtils.isEmpty(input) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter both email/number and password", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(input, password);
                }
            });

            // Redirect to Forgot Password Page
            forgotPassword.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
            );

            // Redirect to Sign-Up Page
            signUpButton.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
            );

            // Redirect to Intro Page
            backButton.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, IntroActivity.class))
            );
        }

        private void loginUser(String input, String password) {
            verifyUserInDatabase(input, password); // Pass both input and password
        }

        // In your LoginActivity.java

        private void verifyUserInDatabase(String input, String password) {
            boolean isEmail = input.contains("@"); // Check if input is an email
            String queryField = isEmail ? "email" : "phone";

            databaseReference.orderByChild(queryField).equalTo(input)
                    .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String dbPassword = userSnapshot.child("password").getValue(String.class);
                                    Integer userID = Integer.parseInt(Objects.requireNonNull(userSnapshot.child("userId").getValue()).toString());

                                    if (dbPassword != null && dbPassword.equals(password)) {
                                        // Store userID in UserSession Singleton
                                        UserSession.getInstance().setUserId(userID); // Store globally

                                        // Successful login
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                        return;
                                    }
                                }
                                // Password mismatch
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            } else {
                                // No user found with the provided email/phone
                                Toast.makeText(LoginActivity.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }




    }
