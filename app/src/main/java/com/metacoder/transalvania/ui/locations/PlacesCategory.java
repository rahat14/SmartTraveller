package com.metacoder.transalvania.ui.locations;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.metacoder.transalvania.R;

public class PlacesCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_category);
        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent(getApplicationContext(), LocationList.class);

        findViewById(R.id.seas).setOnClickListener(view -> {
            intent.putExtra("cat", "sea");
            startActivity(intent);
        });


        findViewById(R.id.plains).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("cat", "plain");
                startActivity(intent);
            }
        });

        findViewById(R.id.historical).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("cat", "historical");
                startActivity(intent);
            }
        });

        findViewById(R.id.mountains).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("cat", "mountain");
                startActivity(intent);
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

}