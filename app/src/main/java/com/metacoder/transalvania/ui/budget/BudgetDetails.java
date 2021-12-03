package com.metacoder.transalvania.ui.budget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.metacoder.transalvania.databinding.ActivityBudgetDetailsBinding;
import com.metacoder.transalvania.models.BudgetModel;
import com.metacoder.transalvania.models.CalacModel;
import com.metacoder.transalvania.ui.Events.BudgetListAdapter;

public class BudgetDetails extends AppCompatActivity implements BudgetListAdapter.ItemClickListener {
    ActivityBudgetDetailsBinding binding;
    BudgetListAdapter mAdapterr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBudgetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BudgetModel model = (BudgetModel) getIntent().getSerializableExtra("MODEL");

        binding.from.setText(model.getFrom());
        binding.ro.setText(model.getTol());

        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.list.setAdapter(new BudgetListAdapter(model.getBreakDowns(), this, this));


    }


    @Override
    public void onItemClick(CalacModel model) {

    }
}