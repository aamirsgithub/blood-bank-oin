package com.example.bloodbankmerafinal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Set up ViewPager2 with fragments
        NotificationsPagerAdapter adapter = new NotificationsPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set up TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
        {
            if (position == 0) {
                tab.setText("Received Requests");
            } else {
                tab.setText("My Requests");
            }
        }).attach();
    }

    // Pager Adapter that connects Fragments to ViewPager
    public class NotificationsPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {

        public NotificationsPagerAdapter(AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new ReceivedRequestsFragment();
            } else {
                return new MyRequestsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Two tabs: Received Requests, My Requests
        }
    }

    // Fragment to display Received Requests
    public static class ReceivedRequestsFragment extends Fragment {
        private RecyclerView recyclerView;
        private RequestAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_received_requests, container, false);
            recyclerView = view.findViewById(R.id.receivedRequestsRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new RequestAdapter(getReceivedRequests());
            recyclerView.setAdapter(adapter);
            return view;
        }

        private List<Request> getReceivedRequests() {
            List<Request> requests = new ArrayList<>();
            // Populate list with actual data here
            requests.add(new Request("AB+", "Female, 21 yr old, DHA, Lahore", "15/11/2022"));
            return requests;
        }
    }

    // Fragment to display My Requests
    public static class MyRequestsFragment extends Fragment {
        private RecyclerView recyclerView;
        private RequestAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_my_requests, container, false);
            recyclerView = view.findViewById(R.id.myRequestsRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new RequestAdapter(getMyRequests());
            recyclerView.setAdapter(adapter);
            return view;
        }

        private List<Request> getMyRequests() {
            List<Request> requests = new ArrayList<>();
            // Populate list with actual data here
            requests.add(new Request("O-", "Male, 25 yr old, Karachi", "20/11/2022"));
            return requests;
        }
    }

    // Adapter for RecyclerView to display request card
    public static class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
        private List<Request> requestList;

        public RequestAdapter(List<Request> requestList) {
            this.requestList = requestList;
        }

        @Override
        public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.requests_cardview, parent, false);
            return new RequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RequestViewHolder holder, int position) {
            Request request = requestList.get(position);
            holder.bloodType.setText(request.getBloodType());
            holder.requestDetails.setText(request.getRequestDetails());
            holder.timeLimit.setText(request.getTimeLimit());

            holder.acceptButton.setOnClickListener(v -> {
                // Handle accept button click (e.g., update status)
            });

            holder.cancelButton.setOnClickListener(v -> {
                // Handle cancel button click (e.g., reject request)
            });
        }

        @Override
        public int getItemCount() {
            return requestList.size();
        }

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

    // Request class to represent a request object
    public static class Request {
        private String bloodType;
        private String requestDetails;
        private String timeLimit;

        public Request(String bloodType, String requestDetails, String timeLimit) {
            this.bloodType = bloodType;
            this.requestDetails = requestDetails;
            this.timeLimit = timeLimit;
        }

        public String getBloodType() {
            return bloodType;
        }

        public String getRequestDetails() {
            return requestDetails;
        }

        public String getTimeLimit() {
            return timeLimit;
        }
    }
}
