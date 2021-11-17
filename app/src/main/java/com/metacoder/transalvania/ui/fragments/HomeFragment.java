package com.metacoder.transalvania.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.metacoder.transalvania.databinding.FragmentHomeBinding;
import com.metacoder.transalvania.ui.Events.EventPage;
import com.metacoder.transalvania.ui.LocationList;
import com.metacoder.transalvania.ui.services.BikePage;
import com.metacoder.transalvania.ui.services.HotelPage;
import com.metacoder.transalvania.ui.timeline.TimelinePage;


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
                    startActivity(new Intent(getContext(), TimelinePage.class));
                }
        );

        binding.placesBTN.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), LocationList.class));
                }
        );

        binding.hotelLIst.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), HotelPage.class));
                }
        );

        binding.bikeList.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), BikePage.class));
                }
        );


        binding.eventList.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), EventPage.class));
                }
        );

        return binding.getRoot();
    }


}