package com.metacoder.transalvania.ui.emergency;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.transalvania.databinding.ActivityEmergencyContactListBinding;
import com.metacoder.transalvania.models.ContactModel;
import com.metacoder.transalvania.ui.Events.ContactListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EmergencyContactList extends AppCompatActivity implements ContactListAdapter.ItemClickListener {
    DatabaseReference mref;
    List<ContactModel> loadedEvents = new ArrayList<>();
    ContactListAdapter mAdapter;
    String[] placesType = {"Please Select", "Ambulance", "Hospital", "Rescue Team", "Police", "Fire Service"};
    String placeType = "";
    private ActivityEmergencyContactListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyContactListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mref = FirebaseDatabase.getInstance().getReference("Emergency_Contact");
        getSupportActionBar().setTitle("Emergency Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.find.setOnClickListener(view -> {

            try {
                loadedEvents(true, binding.email.getText().toString().toLowerCase());

            } catch (Exception e) {

            }

        });

        binding.contactList.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placesType);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(catAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    placeType = placesType[i].toLowerCase(Locale.ROOT);
                } else placeType = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadedEvents(false, placeType.toLowerCase());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadedEvents(boolean isSearch, String s) {
        loadedEvents.clear();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loadedEvents.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ContactModel eventModel = ds.getValue(ContactModel.class);
                    Log.d("TAG", "onDataChange: " + eventModel.getArea());
                    if (isSearch) {
                        if ((eventModel.getArea().toLowerCase().contains(s) || eventModel.getLocation().toLowerCase().contains(s))) {
                            loadedEvents.add(eventModel);
                        } else if (placeType.contains(eventModel.getType().toLowerCase())) {
                            loadedEvents.add(eventModel);
                        }
                    } else loadedEvents.add(eventModel);

                }
                mAdapter = new ContactListAdapter(loadedEvents, EmergencyContactList.this, EmergencyContactList.this);
                binding.contactList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    @Override
    public void onItemClick(ContactModel model) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + model.getPhone()));
        startActivity(intent);
    }
}