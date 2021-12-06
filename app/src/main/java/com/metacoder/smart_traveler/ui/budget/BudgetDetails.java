package com.metacoder.smart_traveler.ui.budget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.metacoder.smart_traveler.databinding.ActivityBudgetDetailsBinding;
import com.metacoder.smart_traveler.models.BudgetModel;
import com.metacoder.smart_traveler.models.CalacModel;

public class BudgetDetails extends AppCompatActivity implements CalclistAdapter.ItemClickListener {
    ActivityBudgetDetailsBinding binding;
    CalclistAdapter mAdapterr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBudgetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BudgetModel model = (BudgetModel) getIntent().getSerializableExtra("MODEL");

        binding.from.setText(model.getFrom());
        binding.to.setText(model.getTol());

        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.list.setAdapter(new CalclistAdapter(model.getBreakDowns(), this, this));



    }


    @Override
    public void onItemClick(CalacModel model) {

    }
}