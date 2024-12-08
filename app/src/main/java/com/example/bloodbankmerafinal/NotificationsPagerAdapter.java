package com.example.bloodbankmerafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NotificationsPagerAdapter extends FragmentStateAdapter {
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
        return 2;
    }
}
