package com.metacoder.transalvania.ui.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class fragment_adapter extends FragmentStateAdapter {

    public static String[] titles = new String[]{"", "", ""};

    public fragment_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new ProfileFragment();

        }

        return new HomeFragment();
    }


    @Override
    public int getItemCount() {
        return titles.length;
    }
}
