package com.metacoder.smart_traveler.ui.budget;

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

import com.appyvet.materialrangebar.RangeBar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.databinding.ActivityBudgetListPageBinding;
import com.metacoder.smart_traveler.models.BudgetModel;
import com.metacoder.smart_traveler.viewholders.viewholderForBudgetListList;

import java.util.ArrayList;
import java.util.List;

public class BudgetListPage extends AppCompatActivity implements BudgetListAdapter.ItemClickListener {

    ActivityBudgetListPageBinding binding;
    DatabaseReference mref;
    BudgetListAdapter mAdapter;
    List<BudgetModel> loadedTours = new ArrayList<>();
    List<BudgetModel> sortedList = new ArrayList<>();
    String lowerLimit = "0", higherLimit = "10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBudgetListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tour Budget");
        binding.list.setLayoutManager(new LinearLayoutManager(this));

        // loadTripData();
        loadBudgetList();
        binding.rangebar1.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                higherLimit = rightPinValue;
                lowerLimit = leftPinValue;
                Log.d("TAG", "onRangeChangeListener: " + higherLimit + "  -> " + lowerLimit);
            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }
        });
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillterBudgetList();
            }
        });

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rangebar1.setRangePinsByIndices(0, 9);

                loadBudgetList();

            }
        });


    }

    private void fillterBudgetList() {
        sortedList.clear();
        for (BudgetModel model : loadedTours) {
            if (Integer.parseInt(model.getTotal()) >= Integer.parseInt(lowerLimit) && Integer.parseInt(model.getTotal()) <= Integer.parseInt(higherLimit)) {
                sortedList.add(model);
            }
        }

        mAdapter = new BudgetListAdapter(sortedList, BudgetListPage.this, BudgetListPage.this);
        if (sortedList.size() <= 0) {
            Toast.makeText(getApplicationContext(), "There is no Tour On Your Budget", Toast.LENGTH_LONG).show();
        }

        Log.d("TAG", "fillterBudgetList:  size  " + sortedList.size() + " Hugher Limity" + higherLimit + " lower limit " + lowerLimit);
        binding.list.setAdapter(mAdapter);
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
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Budget_Model_Tour_List");
        FirebaseRecyclerOptions<BudgetModel> options;
        FirebaseRecyclerAdapter<BudgetModel, viewholderForBudgetListList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<BudgetModel>().setQuery(mref, BudgetModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BudgetModel, viewholderForBudgetListList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForBudgetListList holder, final int position, @NonNull BudgetModel model) {

                holder.setDataToView(getApplicationContext(), model);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent p = new Intent(getApplicationContext(), BudgetDetails.class);
                        p.putExtra("MODEL", model);
                        startActivity(p);

                    }
                });

            }

            @NonNull
            @Override
            public viewholderForBudgetListList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
                final viewholderForBudgetListList viewholders = new viewholderForBudgetListList(iteamVIew);

                return viewholders;
            }
        };

        firebaseRecyclerAdapter.startListening();
        binding.list.setAdapter(firebaseRecyclerAdapter);

    }


    private void loadBudgetList() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Budget_Model_Tour_List");
        loadedTours.clear();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loadedTours.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BudgetModel eventModel = ds.getValue(BudgetModel.class);

                    loadedTours.add(eventModel);
                }
                mAdapter = new BudgetListAdapter(loadedTours, BudgetListPage.this, BudgetListPage.this);
                binding.list.setAdapter(mAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


    @Override
    public void onItemClick(BudgetModel model) {
        Intent p = new Intent(getApplicationContext(), BudgetDetails.class);
        p.putExtra("MODEL", model);
        startActivity(p);

    }
}