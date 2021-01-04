package com.trikon.medicine.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.trikon.medicine.fragment.EventListFragment;
import com.trikon.medicine.fragment.HomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    private FragmentActivity activity;
    private EventListFragment fragment;
    private HomeFragment fragment2;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.activity = fragmentActivity;
    }
    @NonNull @Override public Fragment createFragment(int position) {

        switch (position){

            case 0:

                fragment2 =  HomeFragment.newInstance("abc","abc");

                return fragment2;
               // break;
            case 1:
                fragment = EventListFragment.newInstance("abc","abc");
               // mPageReferenceMap.put(index, "Some Tag");
                //activity.getSupportFragmentManager().beginTransaction().add(myFragment, "ABC").commit();
                return fragment;
               // break;
        }
        return HomeFragment.newInstance("abc","abc");
    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }

    public EventListFragment getFragment(){
        return fragment;
    }

    public HomeFragment getFragment2(){
        return fragment2;
    }
}