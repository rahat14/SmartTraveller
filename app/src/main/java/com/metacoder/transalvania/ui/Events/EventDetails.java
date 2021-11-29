package com.metacoder.transalvania.ui.Events;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.annotations.NotNull;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityEventDetailsBinding;
import com.metacoder.transalvania.models.EventModel;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.RatingModel;
import com.metacoder.transalvania.ui.SuccessPage;
import com.metacoder.transalvania.utils.Utils;
import com.metacoder.transalvania.viewholders.viewholderForReviewList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventDetails extends AppCompatActivity {
    private ActivityEventDetailsBinding binding;
    float tvRate = 0, totalStars = 0;
    List<RatingModel> ratingModelList = new ArrayList<>();
    EventModel model ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
         model = (EventModel) getIntent().getSerializableExtra("MODEL");

        binding.titleTV.setText(model.getName());
        binding.addressTv.setText(model.getLocation());
        binding.descTv.setText(model.getPlaceDetails() + "\n Price Details \n" + model.getPriceDetails());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            binding.reviewBtn.setVisibility(View.GONE);
        }

        binding.reviewBtn.setOnClickListener(view -> {
            String uids = FirebaseAuth.getInstance().getCurrentUser().getUid();
            showDialog(uids, model.getId()+"", ratingModelList);
        });

        Glide.with(getApplicationContext())
                .load(model.getBanner_image())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imageSlider);

        binding.backBtn.setOnClickListener(v -> finish());

        binding.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    ProgressDialog dialog = new ProgressDialog(EventDetails.this);
                    dialog.setMessage("Processing...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext() , SuccessPage.class));
                        }
                    }, 1200) ;
                }else {
                    Utils.showLOginError(EventDetails.this);
                }
            }
        });

    }

    public void showDialog(String uid, String postID, List<RatingModel> ratingModelList) {
        totalStars = 0;
        tvRate = 0;
        final Dialog dialog = new Dialog(EventDetails.this);
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
               // Log.d("TAG", "onClick: " + totalRating + "total stars " + totalStars);
                DatabaseReference mrf = FirebaseDatabase.getInstance().getReference("Events")
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
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Events").child(model.getId()+"").child("rating_list");
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