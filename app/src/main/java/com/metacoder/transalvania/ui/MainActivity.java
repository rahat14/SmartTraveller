package com.metacoder.transalvania.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.metacoder.transalvania.ui.fragments.fragment_adapter;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.home_fragment:
                            binding.viewpager.setCurrentItem(0, false);
                            break;

                        case R.id.search_fragment:
                            binding.viewpager.setCurrentItem(1, false);
                            break;

                        case R.id.profile_fragment:
                            binding.viewpager.setCurrentItem(2, false);
                            break;

                    }

                    return true;

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewpager.setAdapter(new fragment_adapter(this));
        binding.viewpager.setUserInputEnabled(false);
        binding.bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNav.getMenu().findItem(R.id.home_fragment).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNav.getMenu().findItem(R.id.search_fragment).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNav.getMenu().findItem(R.id.profile_fragment).setChecked(true);
                        break;


                }

                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


    }
}