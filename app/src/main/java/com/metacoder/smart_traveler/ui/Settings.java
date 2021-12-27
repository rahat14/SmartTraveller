package com.metacoder.smart_traveler.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.utils.Utils;

public class Settings extends AppCompatActivity {
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchCompat = findViewById(R.id.switchLang);
        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //open job.
                Utils.localeUpdateResources(getApplicationContext(), "bd");
            } else {
                //close job.
                Utils.localeUpdateResources(getApplicationContext(), "en");
            }
        });

    }
}