package com.metacoder.transalvania.ui.fragments;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.metacoder.transalvania.ui.budget.BudgetListPage;
import com.metacoder.transalvania.ui.EmergencyContactList;
import com.metacoder.transalvania.databinding.FragmentHomeBinding;
import com.metacoder.transalvania.ui.Events.EventPage;
import com.metacoder.transalvania.ui.Pace;
import com.metacoder.transalvania.ui.PlacesCategory;
import com.metacoder.transalvania.ui.services.BikePage;
import com.metacoder.transalvania.ui.services.HotelPage;
import com.metacoder.transalvania.ui.timeline.TimelinePage;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }
    LocationSettingsRequest.Builder builder ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);


        binding.timlineTbn.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), TimelinePage.class));
                }
        );

        binding.placesBTN.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), PlacesCategory.class));
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

        binding.emergencyContact.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), EmergencyContactList.class));
                }
        );

        binding.budgetCalc.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), BudgetListPage.class));
                }
        );

        binding.nearBy.setOnClickListener(v -> {
                   askForGps();
                }
        );


        binding.eventList.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), EventPage.class));
                }
        );



        return binding.getRoot();


    }

    private void askForGps(){


        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());



        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                // All location settings are satisfied. The client can initialize location
                // requests here.
                startActivity(new Intent(getContext(), Pace.class));

            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().

                            resolvable.startResolutionForResult(
                                   getActivity(),
                                    100);


                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        } catch (ClassCastException e) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

}