package com.metacoder.transalvania.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.metacoder.transalvania.databinding.FragmentHomeBinding;
import com.metacoder.transalvania.ui.LocationList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.timlineTbn.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), LocationList.class));
                }
        );


        return binding.getRoot();
    }


}