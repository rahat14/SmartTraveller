package com.metacoder.smart_traveler.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.databinding.ActivityWelcomeScreenBinding;
import com.metacoder.smart_traveler.ui.auth.Register;
import com.metacoder.smart_traveler.ui.auth.SignIn;
import com.metacoder.smart_traveler.utils.SliderAdapterExample;
import com.metacoder.smart_traveler.utils.Utils;
import com.metacoder.smart_traveler.utils.sliderItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {

    private ActivityWelcomeScreenBinding binding;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setupView();

        binding.regiterBtn.setOnClickListener(v -> {
            Intent p = new Intent(getApplicationContext(), Register.class);
            startActivity(p);

        });

        binding.signinBtn.setOnClickListener(v -> {
            Intent p = new Intent(getApplicationContext(), SignIn.class);
            startActivity(p);

        });
        binding.skipped.setOnClickListener(view -> {
            Intent p = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(p);
        });
    }

    private void setupView() {

        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.startAutoCycle();
        ArrayList<sliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new sliderItem(R.drawable.sunset));
        sliderItems.add(new sliderItem(R.drawable.ba));
        sliderItems.add(new sliderItem(R.drawable.beach2));
        SliderAdapterExample adapter = new SliderAdapterExample(getApplicationContext(), sliderItems);
        binding.imageSlider.setSliderAdapter(adapter);


    }

}