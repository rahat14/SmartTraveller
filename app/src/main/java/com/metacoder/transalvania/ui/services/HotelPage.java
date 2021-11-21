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
import com.metacoder.transalvania.databinding.ActivityHotelPageBinding;
import com.metacoder.transalvania.models.HotelModel;
import com.metacoder.transalvania.viewholders.viewholderForHotelList;

public class HotelPage extends AppCompatActivity {
    private ActivityHotelPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelPageBinding.inflate(getLayoutInflater());
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
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Hotel_Services");
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
                        Intent p = new Intent(getApplicationContext(), HotelDetails.class);
                        p.putExtra("MODEL", model);
                        startActivity(p);
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