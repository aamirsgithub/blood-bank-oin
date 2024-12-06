package com.example.bloodbankmerafinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonationHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView backButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private DonationHistoryAdapter adapter;
    private List<DonationHistory> donationHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_history); // Ensure this has the RecyclerView

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.back);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donationHistoryList = new ArrayList<>();
        adapter = new DonationHistoryAdapter(donationHistoryList);
        recyclerView.setAdapter(adapter);

        // Back Button Listener
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DonationHistoryActivity.this, com.example.bloodbankmerafinal.UserProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Load Donation History
        loadDonationHistory();
    }

    private void loadDonationHistory() {
        String userId = auth.getCurrentUser().getUid(); // Get current user's ID

        databaseReference.child(userId).child("donationHistory")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            donationHistoryList.clear();
                            for (DataSnapshot donationSnapshot : snapshot.getChildren()) {
                                String bloodType = donationSnapshot.child("bloodType").getValue(String.class);
                                String donorInfo = donationSnapshot.child("donorInfo").getValue(String.class);
                                String donorLocation = donationSnapshot.child("donorLocation").getValue(String.class);
                                String timeLimit = donationSnapshot.child("timeLimit").getValue(String.class);
                                String status = donationSnapshot.child("status").getValue(String.class);

                                donationHistoryList.add(new DonationHistory(bloodType, donorInfo, donorLocation, timeLimit, status));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(DonationHistoryActivity.this, "No donation history found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DonationHistoryActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Inner class for the model
    private static class DonationHistory {
        private String bloodType;
        private String donorInfo;
        private String donorLocation;
        private String timeLimit;
        private String status;

        public DonationHistory(String bloodType, String donorInfo, String donorLocation, String timeLimit, String status) {
            this.bloodType = bloodType;
            this.donorInfo = donorInfo;
            this.donorLocation = donorLocation;
            this.timeLimit = timeLimit;
            this.status = status;
        }

        public String getBloodType() { return bloodType; }
        public String getDonorInfo() { return donorInfo; }
        public String getDonorLocation() { return donorLocation; }
        public String getTimeLimit() { return timeLimit; }
        public String getStatus() { return status; }
    }

    // Inner class for the adapter
    private class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.DonationViewHolder> {

        private final List<DonationHistory> donationHistoryList;

        public DonationHistoryAdapter(List<DonationHistory> donationHistoryList) {
            this.donationHistoryList = donationHistoryList;
        }

        @NonNull
        @Override
        public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_card, parent, false);
            return new DonationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
            DonationHistory donation = donationHistoryList.get(position);
            holder.textViewBloodType.setText(donation.getBloodType());
            holder.textViewDonorInfo.setText(donation.getDonorInfo());
            holder.textViewDonorLocation.setText(donation.getDonorLocation());
            holder.textViewTimeLimit.setText(donation.getTimeLimit());
            holder.textViewStatus.setText(donation.getStatus());

            // Status color
            if (donation.getStatus().equalsIgnoreCase("Accepted")) {
                holder.textViewStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if (donation.getStatus().equalsIgnoreCase("Pending")) {
                holder.textViewStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                holder.textViewStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }

        @Override
        public int getItemCount() {
            return donationHistoryList.size();
        }

        class DonationViewHolder extends RecyclerView.ViewHolder {
            TextView textViewBloodType, textViewDonorInfo, textViewDonorLocation, textViewTimeLimit, textViewStatus;

            public DonationViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewBloodType = itemView.findViewById(R.id.textView_bloodType);
                textViewDonorInfo = itemView.findViewById(R.id.textView_donorInfo);
                textViewDonorLocation = itemView.findViewById(R.id.textView_donorLocation);
                textViewTimeLimit = itemView.findViewById(R.id.textView_timeLimit);
                textViewStatus = itemView.findViewById(R.id.textView_status);
            }
        }
    }
}
