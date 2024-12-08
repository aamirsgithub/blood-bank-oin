package com.example.bloodbankmerafinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<Request> requestList;

    // Constructor to initialize the request list
    public RequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single request item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requests_cardview, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        // Get the current request
        Request request = requestList.get(position);

        // Bind data to the ViewHolder
        holder.bloodType.setText(request.getBloodType());
        holder.requestDetails.setText(request.getDetails());
        holder.timeLimit.setText(request.getTimeLimit());

        // Handle accept button click
        holder.acceptButton.setOnClickListener(v -> {
            // Add accept logic here
            // For example: Update request status in the database
        });

        // Handle cancel button click
        holder.cancelButton.setOnClickListener(v -> {
            // Add cancel logic here
            // For example: Remove the request from the database
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    // ViewHolder class for the adapter
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView bloodType, requestDetails, timeLimit;
        Button acceptButton, cancelButton;

        public RequestViewHolder(View itemView) {
            super(itemView);
            bloodType = itemView.findViewById(R.id.bloodtype);
            requestDetails = itemView.findViewById(R.id.requestDetails);
            timeLimit = itemView.findViewById(R.id.timeLimit);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
