package com.example.bloodbankmerafinal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Request> requestList;

    private String userId = "currentUserId"; // Replace with actual user ID from authentication

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);
        recyclerView = view.findViewById(R.id.myRequestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        requestList = new ArrayList<Request>();
        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String requesterId = snapshot.child("requesterId").getValue(String.class);

                    if (requesterId != null && requesterId.equals(userId)) {
                        String bloodType = snapshot.child("bloodType").getValue(String.class);
                        String details = snapshot.child("details").getValue(String.class);
                        String timeLimit = snapshot.child("timeLimit").getValue(String.class);

                        Request request = new Request(bloodType, details, timeLimit);
                        requestList.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        return view;
    }
}
