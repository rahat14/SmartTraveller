package com.metacoder.transalvania.ui.budget;

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
import com.metacoder.transalvania.databinding.ActivityBudgetListPageBinding;
import com.metacoder.transalvania.models.BudgetModel;
import com.metacoder.transalvania.viewholders.viewholderForBudgetListList;

public class BudgetListPage extends AppCompatActivity {

    ActivityBudgetListPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBudgetListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.list.setLayoutManager(new LinearLayoutManager(this));

        loadTripData();
//        for (int i = 0; i < 10; i++) {
//
//            List<CalacModel> list = new ArrayList<>();
//            list.add(new CalacModel("Test Description", "100"));
//            list.add(new CalacModel("Test Description", "100"));
//            list.add(new CalacModel("Test Description", "100"));
//            list.add(new CalacModel("Test Description", "100"));
//            list.add(new CalacModel("Test Description", "100"));
//            //   String id, String total, String from, String tol, String title, int upperLimit, int lowerLimit, List<CalacModel> breakDowns
//            BudgetModel model = new BudgetModel(i + "", "10000", "Test", "Test", "Test Title", 1500, 2500, list);
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Budget_Model_Tour_List").child(i + "");
//            databaseReference.setValue(model);
//
//        }

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

                        Intent p = new Intent(getApplicationContext(), BudgetDetails.class) ;
                        p.putExtra("MODEL" , model);
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

}