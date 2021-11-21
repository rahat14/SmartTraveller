package com.metacoder.transalvania.ui.Events;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityEventDetailsBinding;
import com.metacoder.transalvania.models.EventModel;

public class EventDetails extends AppCompatActivity {
    private ActivityEventDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventModel model = (EventModel) getIntent().getSerializableExtra("MODEL");

        binding.titleTV.setText(model.getName());
        binding.addressTv.setText(model.getLocation());
        binding.descTv.setText(model.getPlaceDetails() + "\n Price Details \n" + model.getPriceDetails());

        Glide.with(getApplicationContext())
                .load(model.getBanner_image())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imageSlider);

        binding.backBtn.setOnClickListener(v -> finish());

    }
}