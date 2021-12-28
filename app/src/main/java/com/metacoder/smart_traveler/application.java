package com.metacoder.smart_traveler;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.widget.Toast;

import com.metacoder.smart_traveler.utils.Utils;

import java.util.Locale;

public class application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("uinfo", MODE_PRIVATE);
        String restoredText = prefs.getString("lang", "en");

        if (TextUtils.equals(restoredText, "en")) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            Toast.makeText(getApplicationContext(), "en is set", Toast.LENGTH_SHORT).show();
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        } else {
            Locale locale = new Locale("bn");
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            Toast.makeText(getApplicationContext(), "bn is set", Toast.LENGTH_SHORT).show();
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        }
    }

}
