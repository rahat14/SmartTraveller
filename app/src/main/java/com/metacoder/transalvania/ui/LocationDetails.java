package com.metacoder.transalvania.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityLocationDetailsBinding;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.RatingModel;
import com.metacoder.transalvania.models.TripModel;
import com.metacoder.transalvania.viewholders.viewholderForReviewList;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LocationDetails extends AppCompatActivity {

    TripModel model;
    private ActivityLocationDetailsBinding binding;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationDetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.reviewList.setLayoutManager(new LinearLayoutManager(this));
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        model = (TripModel) getIntent().getSerializableExtra("TRIP_MODEL");


        binding.backBtn.setOnClickListener(v -> finish());

        binding.titleTV.setText(model.getName());
        binding.descTv.setText(model.getDesc());
        binding.costTv.setText(model.getTrip_cost() + "");
        binding.ratingTv.setText(model.getCurrent_rating() + "");
        binding.durationTv.setText(model.getTrip_duration() + "");


        binding.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("user_id", uid);
                map.put("purchase_time", System.currentTimeMillis());
                map.put("paid", model.getTrip_cost());
                map.put("trip_id", model.getId());

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("purchase_list");

                String key = databaseReference.push().getKey();
                map.put("trans_id", key);

                databaseReference.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getApplicationContext(), "Congratulations , You Have Purchased This Trip", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error :  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        loadTripData();
    }


    private void loadTripData() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("places").child(model.getId()).child("rating_list");
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseRecyclerOptions<RatingModel> options;
        FirebaseRecyclerAdapter<RatingModel, viewholderForReviewList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<RatingModel>().setQuery(mref, RatingModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RatingModel, viewholderForReviewList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForReviewList holder, final int position, @NonNull RatingModel model) {


                profileRef.child(model.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ProfileModel profileModel = snapshot.getValue(ProfileModel.class);

                            holder.setDataToView(getApplicationContext(), model, profileModel);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


            }

            @NonNull
            @Override
            public viewholderForReviewList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
                final viewholderForReviewList viewholders = new viewholderForReviewList(iteamVIew);

                return viewholders;
            }
        };

        firebaseRecyclerAdapter.startListening();
        binding.reviewList.setAdapter(firebaseRecyclerAdapter);

    }
}