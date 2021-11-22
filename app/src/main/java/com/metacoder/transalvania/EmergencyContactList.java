package com.metacoder.transalvania;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.metacoder.transalvania.databinding.ActivityEmergencyContactListBinding;

public class EmergencyContactList extends AppCompatActivity {
    private ActivityEmergencyContactListBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyContactListBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());


    }
}