package com.metacoder.smart_traveler.ui.services;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.databinding.ActivityHotelPageBinding;
import com.metacoder.smart_traveler.models.HotelModel;
import com.metacoder.smart_traveler.viewholders.viewholderForHotelList;

public class HotelPage extends AppCompatActivity {
    private ActivityHotelPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hotels");
        binding.hotelLIst.setLayoutManager(new LinearLayoutManager(this));
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String  q  = s.toString().trim() ;
                if(q.isEmpty()){
                    loadTripData("");
                }else {
                    loadTripData(q);
                }

            }
        });
        loadTripData("");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTripData(String q) {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Hotel_Services");
        FirebaseRecyclerOptions<HotelModel> options;
        FirebaseRecyclerAdapter<HotelModel, viewholderForHotelList> firebaseRecyclerAdapter;
        Query firebaseQry = mref.orderByChild("location").startAt(q.toLowerCase()).endAt(q.toLowerCase()+ "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<HotelModel>().setQuery(firebaseQry, HotelModel.class).build();

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