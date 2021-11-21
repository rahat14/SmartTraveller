package com.metacoder.transalvania.ui.services;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.metacoder.transalvania.models.BikeModel;
import com.metacoder.transalvania.models.HotelModel;
import com.metacoder.transalvania.viewholders.viewholderForHotelList;
import com.metacoder.transalvania.viewholders.viewholderForRentalList;

public class BikePage extends AppCompatActivity {
    private ActivityBikePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.hotelLIst.setLayoutManager(new LinearLayoutManager(this));
        loadTripData();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTripData() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Bike_Services");
        FirebaseRecyclerOptions<BikeModel> options;
        FirebaseRecyclerAdapter<BikeModel, viewholderForRentalList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<BikeModel>().setQuery(mref, BikeModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BikeModel, viewholderForRentalList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForRentalList holder, final int position, @NonNull BikeModel model) {

                holder.setDataToView(getApplicationContext(), model);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent p = new Intent(getApplicationContext(), BikeDetails.class);
                        p.putExtra("MODEL", model);
                        startActivity(p);
                    }
                });

            }

            @NonNull
            @Override
            public viewholderForRentalList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
                final viewholderForRentalList viewholders = new viewholderForRentalList(iteamVIew);

                return viewholders;
            }
        };

        firebaseRecyclerAdapter.startListening();
        binding.hotelLIst.setAdapter(firebaseRecyclerAdapter);

    }
}