package com.example.bloodbankmerafinal;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {

    private ArrayList<HashMap<String, String>> donorList;

    public DonorAdapter(ArrayList<HashMap<String, String>> donorList) {
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_donor_card, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        HashMap<String, String> donor = donorList.get(position);

        // Populate data into the views
        holder.bloodTypeTextView.setText(donor.get("bloodGroup"));
        holder.donorInfoTextView.setText(donor.get("name"));
        holder.donorLocationTextView.setText(donor.get("city"));
        holder.timeLimitTextView.setText("Units: " + donor.get("noOfUnits"));
        holder.addressTextView.setText("Address: " + donor.get("address"));

        // Set up the callIcon click listener to navigate to DonorInfoActivity
        holder.callIcon.setOnClickListener(v -> {
            // Navigate to DonorInfoActivity
            Context context = holder.itemView.getContext();
            Intent donorInfoIntent = new Intent(context, DonorInfoActivity.class);

            // Pass donor details as extras
            donorInfoIntent.putExtra("NAME", donor.get("name"));
            donorInfoIntent.putExtra("EMAIL", donor.get("email"));
            donorInfoIntent.putExtra("PHONE", donor.get("phone"));
            donorInfoIntent.putExtra("BLOOD_GROUP", donor.get("bloodGroup"));
            donorInfoIntent.putExtra("CITY", donor.get("city"));
            donorInfoIntent.putExtra("ADDRESS", donor.get("address"));
            donorInfoIntent.putExtra("NO_OF_UNITS", donor.get("noOfUnits"));

            context.startActivity(donorInfoIntent);
        });

        // Check if the user is logged in before allowing a request
        if (UserSession.getInstance().getUserId() == null) {

            holder.requestButton.setEnabled(false);
            holder.requestButton.setText("Login to Request");
            holder.requestButton.setOnClickListener(v -> {
                Toast.makeText(holder.itemView.getContext(), "Please log in to send a request.", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Handle Request Button click event if user is logged in
            holder.requestButton.setEnabled(true);
            holder.requestButton.setText("Send Request");
            holder.requestButton.setOnClickListener(v -> {
                // Send a request to Firebase
                sendRequestToFirebase(donor.get("userId"), holder);
                Toast.makeText(holder.itemView.getContext(), "Request sent to " + donor.get("name"), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView bloodTypeTextView, donorInfoTextView, donorLocationTextView, timeLimitTextView, addressTextView;
        ImageView callIcon;
        Button requestButton;

        public DonorViewHolder(View itemView) {
            super(itemView);
            bloodTypeTextView = itemView.findViewById(R.id.textView_bloodType);
            donorInfoTextView = itemView.findViewById(R.id.textView_donorInfo);
            donorLocationTextView = itemView.findViewById(R.id.textView_donorLocation);
            timeLimitTextView = itemView.findViewById(R.id.textView_timeLimit);
            addressTextView = itemView.findViewById(R.id.textView_address);
            callIcon = itemView.findViewById(R.id.icon_call);
            requestButton = itemView.findViewById(R.id.button_request);
        }
    }

    // Method to send request to Firebase with the donorId
    private void sendRequestToFirebase(String donorId, DonorViewHolder holder) {
        // Get the current userId from the session
        String currentUserId = UserSession.getInstance().getUserId();

        if (currentUserId == null) {
            Log.e("Firebase", "No user logged in. Cannot send request.");
            Toast.makeText(holder.itemView.getContext(), "You must be logged in to send a request.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log currentUserId for debugging purposes
        Log.d("Firebase", "Current User ID: " + currentUserId);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("requests");

        // Prepare request data with both requester and donor info
        HashMap<String, Object> requestData = new HashMap<>();
        requestData.put("requesterId", currentUserId);  // Add requester ID
        requestData.put("donorId", donorId);  // Add donor ID
        requestData.put("status", "pending");
        requestData.put("requestTime", System.currentTimeMillis());

        // Log requestData to ensure all fields are included
        Log.d("Firebase", "Request Data: " + requestData.toString());

        // Push request data to Firebase
        databaseRef.push().setValue(requestData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Request successfully sent to Firebase.");
                        Toast.makeText(holder.itemView.getContext(), "Request sent to donor", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Firebase", "Failed to send request: " + task.getException().getMessage());
                        Toast.makeText(holder.itemView.getContext(), "Failed to send request", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}





//package com.example.bloodbankmerafinal;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.bloodbankmerafinal.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {
//
//    private ArrayList<HashMap<String, String>> donorList;
//
//    public DonorAdapter(ArrayList<HashMap<String, String>> donorList) {
//        this.donorList = donorList;
//    }
//
//    @NonNull
//    @Override
//    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_donor_card, parent, false);
//        return new DonorViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
//        HashMap<String, String> donor = donorList.get(position);
//
//        // Populate data into the views
//        holder.bloodTypeTextView.setText(donor.get("bloodGroup"));
//        holder.donorInfoTextView.setText(donor.get("name"));
//        holder.donorLocationTextView.setText(donor.get("city"));
//        holder.timeLimitTextView.setText("Units: " + donor.get("noOfUnits"));
//        holder.addressTextView.setText("Address: " + donor.get("address"));
//
//        // Set up the callIcon click listener to navigate to DonorInfoActivity
//        holder.callIcon.setOnClickListener(v -> {
//            // Navigate to DonorInfoActivity
//            Context context = holder.itemView.getContext();
//            Intent donorInfoIntent = new Intent(context, DonorInfoActivity.class);
//
//            // Pass donor details as extras
//            donorInfoIntent.putExtra("NAME", donor.get("name"));
//            donorInfoIntent.putExtra("EMAIL", donor.get("email"));
//            donorInfoIntent.putExtra("PHONE", donor.get("phone"));
//            donorInfoIntent.putExtra("BLOOD_GROUP", donor.get("bloodGroup"));
//            donorInfoIntent.putExtra("CITY", donor.get("city"));
//            donorInfoIntent.putExtra("ADDRESS", donor.get("address"));
//            donorInfoIntent.putExtra("NO_OF_UNITS", donor.get("noOfUnits"));
//
//            context.startActivity(donorInfoIntent);
//        });
//
//        // Handle Request Button click event (optional)
//        holder.requestButton.setOnClickListener(v -> {
//            // Show a confirmation or take action (e.g., send a request)
//            Toast.makeText(holder.itemView.getContext(), "Request sent to " + donor.get("name"), Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return donorList.size();
//    }
//
//    public static class DonorViewHolder extends RecyclerView.ViewHolder {
//        TextView bloodTypeTextView, donorInfoTextView, donorLocationTextView, timeLimitTextView, addressTextView;
//        ImageView callIcon;
//        Button requestButton;
//
//        public DonorViewHolder(View itemView) {
//            super(itemView);
//            bloodTypeTextView = itemView.findViewById(R.id.textView_bloodType);
//            donorInfoTextView = itemView.findViewById(R.id.textView_donorInfo);
//            donorLocationTextView = itemView.findViewById(R.id.textView_donorLocation);
//            timeLimitTextView = itemView.findViewById(R.id.textView_timeLimit);
//            addressTextView = itemView.findViewById(R.id.textView_address);
//            callIcon = itemView.findViewById(R.id.icon_call);
//            requestButton = itemView.findViewById(R.id.button_request);
//        }
//    }
//}
