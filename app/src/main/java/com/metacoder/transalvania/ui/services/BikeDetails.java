package com.metacoder.transalvania.ui.services;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityBikeDetailsBinding;
import com.metacoder.transalvania.models.BikeModel;

public class BikeDetails extends AppCompatActivity {
    private ActivityBikeDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BikeModel model = (BikeModel) getIntent().getSerializableExtra("MODEL");

        binding.titleTV.setText(model.getName());
        binding.addressTv.setText(model.getAddress());
        binding.descTv.setText(model.getDesc() + "\n Price Details \n" + model.getRate() + "\n Contact Us : \n"+ model.getContact() );

        Glide.with(getApplicationContext())
                .load(model.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imageSlider);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}