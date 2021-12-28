package com.metacoder.smart_traveler.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.databinding.ActivityMainBinding;
import com.metacoder.smart_traveler.ui.about.AboutUs;
import com.metacoder.smart_traveler.ui.auth.SignIn;
import com.metacoder.smart_traveler.ui.fragments.fragment_adapter;
import com.metacoder.smart_traveler.ui.locations.PlacesCategory;
import com.metacoder.smart_traveler.ui.nearme.Pace;
import com.metacoder.smart_traveler.ui.services.HotelPage;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
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
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {
                                binding.viewpager.setCurrentItem(2, false);
                            } else {
                                Toast.makeText(getApplicationContext(), "You Are Not Logged In", Toast.LENGTH_LONG).show();
                            }


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
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();


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
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            binding.bottomNav.getMenu().findItem(R.id.profile_fragment).setChecked(true);
                        } else {
                            Toast.makeText(getApplicationContext(), "You Are Not Logged In", Toast.LENGTH_LONG).show();
                        }

                        break;


                }

                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        binding.drawer.addDrawerListener(drawerToggle);


        binding.navigation.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
            if (itemId == R.id.places) {
                startActivity(new Intent(getApplicationContext(), PlacesCategory.class));
            } else if (itemId == R.id.hotels) {

                startActivity(new Intent(getApplicationContext(), HotelPage.class));
            } else if (itemId == R.id.home) {
                binding.viewpager.setCurrentItem(0);
            } else if (itemId == R.id.share2) {
                ShareLink();
            } else if (itemId == R.id.contact) {
                sendMail();
            }else  if (itemId == R.id.settings){
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
            else if (itemId == R.id.about_us) {
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
            } else if (itemId == R.id.log_out) {
                FirebaseUser firebase = FirebaseAuth.getInstance().getCurrentUser();
                if (firebase != null) {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            binding.drawer.closeDrawer(GravityCompat.START);

            return true;
        });


        FirebaseUser firebase = FirebaseAuth.getInstance().getCurrentUser();
        if (firebase == null) {
            Menu menu = binding.navigation.getMenu();
            MenuItem nav_dashboard = menu.findItem(R.id.log_out);
            nav_dashboard.setTitle("Login");
        }


    }

    private void ShareLink() {
        /*Create an ACTION_SEND Intent*/
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/
        String shareBody = "Here is the share content body";
        /*The type of the content is text, obviously.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check Out My App.  link of the app ");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        /*Fire!*/
        startActivity(Intent.createChooser(intent, "Share Using"));
    }


    public void sendMail() {

        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "samarttravelerbd@gmail.com");
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "onActivityResult: GPS Enabled by user");
        if (requestCode == 100) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    Log.i("TAG", "onActivityResult: GPS Enabled by user");

                    startActivity(new Intent(getApplicationContext(), Pace.class));
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    Log.i("TAG", "onActivityResult: User rejected GPS request");
                    break;
                default:
                    break;
            }
        }
    }
}