package com.metacoder.transalvania.ui.locations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityLocationListBinding;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.LocationModel;
import com.metacoder.transalvania.viewholders.viewholderForTripList;

import org.jetbrains.annotations.NotNull;

public class LocationList extends AppCompatActivity {
    private ActivityLocationListBinding binding;
    String cat = "mountain" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationListBinding.inflate(getLayoutInflater());
        binding.tripList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cat = getIntent().getStringExtra("cat");
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
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("places"); // poitining the database
        FirebaseRecyclerOptions<LocationModel> options;
        FirebaseRecyclerAdapter<LocationModel, viewholderForTripList> firebaseRecyclerAdapter;

        Query query = mref.orderByChild("category").equalTo(cat) ;
        options = new FirebaseRecyclerOptions.Builder<LocationModel>().setQuery(query, LocationModel.class).build(); // started query

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LocationModel, viewholderForTripList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForTripList holder, final int position, @NonNull LocationModel model) {

                holder.setDataToView(getApplicationContext(), model);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // change intent
                        Intent p = new Intent(getApplicationContext(), LocationDetails.class);
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

        firebaseRecyclerAdapter.startListening(); // started the call
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
                    Toast.makeText(getApplicationContext(), "Error : Profile Not Found!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            loadProfileData();
        }


    }
}