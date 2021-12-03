package com.metacoder.transalvania.ui.Events;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.transalvania.databinding.ActivityEventPageBinding;
import com.metacoder.transalvania.models.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventPage extends AppCompatActivity implements EventAdapter.ItemClickListener {
    DatabaseReference mref;
    List<EventModel> loadedEvents = new ArrayList<>();

    EventAdapter mAdapter;
    private ActivityEventPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Events");
        binding.eventList.setLayoutManager(new LinearLayoutManager(this));

        mref = FirebaseDatabase.getInstance().getReference("Events");

        binding.addBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateEventPage.class)));

        loadedEvents();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadedEvents() {
        loadedEvents.clear();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loadedEvents.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    EventModel eventModel = ds.getValue(EventModel.class);

                    loadedEvents.add(eventModel);
                }
                mAdapter = new EventAdapter(loadedEvents, EventPage.this, EventPage.this);
                binding.eventList.setAdapter(mAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    @Override
    public void onItemClick(EventModel model) {

        Intent p = new Intent(getApplicationContext(), EventDetails.class);
        p.putExtra("MODEL", model);
        startActivity(p);
    }
}