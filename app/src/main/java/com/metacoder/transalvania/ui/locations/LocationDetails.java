package com.metacoder.transalvania.ui.locations;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityLocationDetailsBinding;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.RatingModel;
import com.metacoder.transalvania.models.LocationModel;
import com.metacoder.transalvania.viewholders.viewholderForReviewList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationDetails extends AppCompatActivity {
    float tvRate = 0, totalStars = 0;
    List<RatingModel> ratingModelList = new ArrayList<>();
    LocationModel model;
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

        model = (LocationModel) getIntent().getSerializableExtra("TRIP_MODEL");


        binding.backBtn.setOnClickListener(v -> finish());

        binding.titleTV.setText(model.getName());
        binding.descTv.setText(model.getDesc());
        binding.costTv.setText(model.getTrip_cost() + "");
        binding.ratingTv.setText(model.getCurrent_rating() + "");
        binding.durationTv.setText(model.getTrip_duration() + "");

        Glide.with(getApplicationContext())
                .load(model.getMainImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imageSlider);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            binding.reviewBtn.setVisibility(View.GONE);
        }

        binding.reviewBtn.setOnClickListener(view -> {
            String uids = FirebaseAuth.getInstance().getCurrentUser().getUid();
            showDialog(uids, model.getId(), ratingModelList);
        });

//        binding.bookNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                HashMap<String, Object> map = new HashMap<String, Object>();
//
//                map.put("user_id", uid);
//                map.put("purchase_time", System.currentTimeMillis());
//                map.put("paid", model.getTrip_cost());
//                map.put("trip_id", model.getId());
//
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("purchase_list");
//
//                String key = databaseReference.push().getKey();
//                map.put("trans_id", key);
//
//                databaseReference.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//
//                        Toast.makeText(getApplicationContext(), "Congratulations , You Have Purchased This Trip", Toast.LENGTH_LONG).show();
//                        finish();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Error :  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//        });


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

    public void showDialog(String uid, String postID, List<RatingModel> ratingModelList) {
        totalStars = 0;
        tvRate = 0;
        final Dialog dialog = new Dialog(LocationDetails.this);
        dialog.setContentView(R.layout.rating_dialoug);

        MaterialButton cancelBTN = dialog.findViewById(R.id.cancel_btn);
        MaterialButton acceptBTN = dialog.findViewById(R.id.submit_btn);
        TextInputEditText tvTitle = dialog.findViewById(R.id.descET);
        RatingBar tvRating = dialog.findViewById(R.id.ratingTv);

        tvRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> tvRate = rating);

        for (RatingModel ratingModel : ratingModelList) {
            totalStars = totalStars + (float) ratingModel.getRating();
        }


        cancelBTN.setOnClickListener(v -> dialog.dismiss());

        acceptBTN.setOnClickListener(v -> {

            String feedBack = tvTitle.getText().toString();

            if (feedBack.isEmpty()) {
                tvTitle.setError("Can't Be Empty");
            } else if (tvRate == 0) {
                Toast.makeText(getApplicationContext(), "Please Add A Rating", Toast.LENGTH_SHORT).show();

            } else {
                dialog.dismiss();

                float totalRating = (totalStars + (float) tvRate) / (float) (ratingModelList.size() + 1);
                Log.d("TAG", "onClick: " + totalRating + "total stars " + totalStars);
                DatabaseReference mrf = FirebaseDatabase.getInstance().getReference("places")
                        .child(postID);


                HashMap<String, Object> ratingMap = new HashMap<>();

                ratingMap.put("user_id", uid);
                ratingMap.put("feedback", feedBack);
                ratingMap.put("rating", tvRate);

                mrf.child("rating_list").child(uid).setValue(ratingMap)
                        .addOnSuccessListener(unused -> {

                            mrf.child("current_rating").setValue(totalRating + "");

                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Success  :  Your Feedback Received...", Toast.LENGTH_LONG).show();

                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show());


            }


        });

        dialog.show();


    }

    private void loadReviewList() {
        //  Toast.makeText(getApplicationContext() , "Error " + model.getId() , Toast.LENGTH_LONG).show();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("places").child(model.getId()).child("rating_list");
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseRecyclerOptions<RatingModel> options;
        FirebaseRecyclerAdapter<RatingModel, viewholderForReviewList> firebaseRecyclerAdapter;

        options = new FirebaseRecyclerOptions.Builder<RatingModel>().setQuery(mref, RatingModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RatingModel, viewholderForReviewList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForReviewList holder, final int position, @NonNull RatingModel model) {


                try {
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
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


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
        binding.reviewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.reviewList.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadReviewList();
    }
}