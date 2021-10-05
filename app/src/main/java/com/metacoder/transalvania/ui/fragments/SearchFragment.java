package com.metacoder.transalvania.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.metacoder.transalvania.ui.LocationDetails;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.FragmentSearchBinding;
import com.metacoder.transalvania.models.TripModel;
import com.metacoder.transalvania.viewholders.viewholderForTripList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                initSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                initSearch(newText);
                return false;
            }
        });

    }


    public void initSearch(String q) {

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("places");
        Query qq= mref.orderByChild("queryText").startAt(q).endAt(q+ "\uf8ff") ;
        FirebaseRecyclerOptions<TripModel> options;
        FirebaseRecyclerAdapter<TripModel, viewholderForTripList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<TripModel>().setQuery(qq, TripModel.class).build();

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
        binding.list.setAdapter(firebaseRecyclerAdapter);

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
        binding.list.setAdapter(firebaseRecyclerAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();

        loadTripData();
    }
}