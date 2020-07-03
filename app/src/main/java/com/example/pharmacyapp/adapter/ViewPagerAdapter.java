package com.example.pharmacyapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pharmacyapp.fragment.EventListFragment;
import com.example.pharmacyapp.fragment.HomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {

        switch (position){

            case 0:
                return HomeFragment.newInstance("abc","abc");
               // break;
            case 1:
                return EventListFragment.newInstance("abc","abc");
               // break;
        }
        return HomeFragment.newInstance("abc","abc");
    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}