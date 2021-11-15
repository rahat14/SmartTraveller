package com.metacoder.transalvania.ui.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityBikePageBinding;
import com.metacoder.transalvania.models.HotelModel;
import com.metacoder.transalvania.viewholders.viewholderForHotelList;

public class BikePage extends AppCompatActivity {
    private ActivityBikePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.hotelLIst.setLayoutManager(new LinearLayoutManager(this));
        loadTripData();

    }


    private void loadTripData() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Bike_Services");
        FirebaseRecyclerOptions<HotelModel> options;
        FirebaseRecyclerAdapter<HotelModel, viewholderForHotelList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<HotelModel>().setQuery(mref, HotelModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HotelModel, viewholderForHotelList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForHotelList holder, final int position, @NonNull HotelModel model) {

                holder.setDataToView(getApplicationContext(), model);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // change intent
//                        Intent p = new Intent(getApplicationContext(), LocationDetails.class);
//                        p.putExtra("TRIP_MODEL", model);
//                        startActivity(p);
                    }
                });

            }

            @NonNull
            @Override
            public viewholderForHotelList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
                final viewholderForHotelList viewholders = new viewholderForHotelList(iteamVIew);

                return viewholders;
            }
        };

        firebaseRecyclerAdapter.startListening();
        binding.hotelLIst.setAdapter(firebaseRecyclerAdapter);

    }
}