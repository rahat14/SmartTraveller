package com.metacoder.transalvania.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.transalvania.ui.LocationDetails;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.FragmentHomeBinding;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.TripModel;
import com.metacoder.transalvania.viewholders.viewholderForTripList;

import org.jetbrains.annotations.NotNull;


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
        binding.tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTripData();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadProfileData();
//    Toast.makeText(getContext() , "safas"  , Toast.LENGTH_SHORT).show();

    }

    private void loadTripData() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("places");
        FirebaseRecyclerOptions<TripModel> options;
        FirebaseRecyclerAdapter<TripModel, viewholderForTripList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<TripModel>().setQuery(mref, TripModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TripModel, viewholderForTripList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForTripList holder, final int position, @NonNull TripModel model) {

                holder.setDataToView(getContext(), model);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // change intent
                        Intent p = new Intent(getContext(), LocationDetails.class);
                        p.putExtra("TRIP_MODEL", model);
                        startActivity(p);
                    }
                });

            }

            @NonNull
            @Override
            public viewholderForTripList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
                final viewholderForTripList viewholders = new viewholderForTripList(iteamVIew);

                return viewholders;
            }
        };

        firebaseRecyclerAdapter.startListening();
        binding.tripList.setAdapter(firebaseRecyclerAdapter);

    }

    private void loadProfileData() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("TAG", "loadProfileData: " + uid);
        mref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    ProfileModel model = snapshot.getValue(ProfileModel.class);
                    binding.text.setText("Hey " + model.getName() + ",");

                } else {
                    Toast.makeText(getContext(), "Error : Profile Not Found!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}