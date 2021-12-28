package com.metacoder.smart_traveler.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.metacoder.smart_traveler.R;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    ImageView splash;
    TextView title, subtitle;
    Animation upper, bottom;
    private static int timer=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        getSupportActionBar().hide();


        splash = findViewById(R.id.smrt_trvlr_iv);
        title = findViewById(R.id.smrt_trvlr_tv);
        subtitle = findViewById(R.id.sub_title_tv);

        upper = AnimationUtils.loadAnimation(this,R.anim.upper_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        splash.setAnimation(upper);
        title.setAnimation(bottom);
        subtitle.setAnimation(bottom);

//
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("uinfo", MODE_PRIVATE);
        String restoredText = prefs.getString("lang", "en");

        if (TextUtils.equals(restoredText, "en")) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
       //     Toast.makeText(getApplicationContext(), "en is set", Toast.LENGTH_SHORT).show();
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        } else {
            Locale locale = new Locale("bn");
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
         //   Toast.makeText(getApplicationContext(), "bn is set", Toast.LENGTH_SHORT).show();
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        }

        handler.postDelayed(() -> {

            FirebaseAuth mauth = FirebaseAuth.getInstance();
            FirebaseUser user = mauth.getCurrentUser();




            if (user == null) {
                Intent p = new Intent(getApplicationContext(), WelcomeScreen.class);
                startActivity(p);
                  finish();

            } else {
                Intent p = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(p);
                finish();
            }

        }, 2000) ;
    }


}