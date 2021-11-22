package com.metacoder.transalvania.ui.Events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityEventDetailsBinding;
import com.metacoder.transalvania.models.EventModel;
import com.metacoder.transalvania.ui.SuccessPage;
import com.metacoder.transalvania.utils.Utils;

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

        binding.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    ProgressDialog dialog = new ProgressDialog(EventDetails.this);
                    dialog.setMessage("Processing...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext() , SuccessPage.class));
                        }
                    }, 1200) ;
                }else {
                    Utils.showLOginError(EventDetails.this);
                }
            }
        });

    }
}