package com.metacoder.smart_traveler.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.utils.Utils;

import java.util.Locale;

public class Settings extends AppCompatActivity {
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setLocale("bn");
        setContentView(R.layout.activity_settings);
        switchCompat = findViewById(R.id.switchLang);


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("uinfo", MODE_PRIVATE);
        String restoredText = prefs.getString("lang", "en");
        SharedPreferences.Editor editor = prefs.edit();

        Log.d("TAG", "onCreate: " + restoredText);
        switchCompat.setChecked(restoredText.contains("bn"));

        switchCompat.setOnClickListener(view -> {

            boolean b = switchCompat.isChecked();
            if (b) {
                //open job.
                String languageToLoad = "bn";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.setLocale(locale);
                Toast.makeText(getApplicationContext(), "bn is set", Toast.LENGTH_SHORT).show();
                getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

                //  setLocale("bn");
                editor.putString("lang", "bn");
//                Utils.setLocal(getApplicationContext() , "bn");
            } else {
                //close job.
                // Utils.setLocal(getApplicationContext() , "en");

                editor.putString("lang", "en");
                String languageToLoad = "en";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.setLocale(locale);
                Toast.makeText(getApplicationContext(), "english is set", Toast.LENGTH_SHORT).show();
                getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());


            }

            editor.apply();
            startActivity(new Intent(getApplicationContext() , MainActivity.class));
          finish();
        });


    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= 24) {
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        } else {
            config.locale = locale;
            getBaseContext().getApplicationContext().createConfigurationContext(config);

        }
    }


}

