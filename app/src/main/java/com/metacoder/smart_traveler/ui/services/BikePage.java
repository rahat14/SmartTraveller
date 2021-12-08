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
import com.metacoder.smart_traveler.databinding.ActivityBikePageBinding;
import com.metacoder.smart_traveler.models.BikeModel;
import com.metacoder.smart_traveler.viewholders.viewholderForRentalList;

import java.util.Locale;

public class BikePage extends AppCompatActivity {
    private ActivityBikePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rental");
        binding.hotelLIst.setLayoutManager(new LinearLayoutManager(this));
        loadTripData("");

        binding.find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Bike_Services");
        FirebaseRecyclerOptions<BikeModel> options;
        FirebaseRecyclerAdapter<BikeModel, viewholderForRentalList> firebaseRecyclerAdapter;
        Query firebaseQry = mref.orderByChild("address").startAt(q.toLowerCase()).endAt(q.toLowerCase()+ "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<BikeModel>().setQuery(firebaseQry, BikeModel.class).build();



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