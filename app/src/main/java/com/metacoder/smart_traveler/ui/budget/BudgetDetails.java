package com.metacoder.smart_traveler.ui.budget;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BudgetModel model = (BudgetModel) getIntent().getSerializableExtra("MODEL");

        binding.from.setText(model.getFrom());
        binding.to.setText(model.getTol());
        binding.budgetTv.setText("৳ " +model.getTotal());

        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.list.setAdapter(new CalclistAdapter(model.getBreakDowns(), this, this));



    }


    @Override
    public void onItemClick(CalacModel model) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}