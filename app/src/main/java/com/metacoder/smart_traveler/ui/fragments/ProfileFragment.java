package com.metacoder.smart_traveler.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.databinding.FragmentProfileBinding;
import com.metacoder.smart_traveler.models.LocationModel;
import com.metacoder.smart_traveler.models.ProfileModel;
import com.metacoder.smart_traveler.models.RatingModel;
import com.metacoder.smart_traveler.models.TransactionModel;
import com.metacoder.smart_traveler.ui.WelcomeScreen;
import com.metacoder.smart_traveler.ui.auth.Register;
import com.metacoder.smart_traveler.viewholders.viewholderForMyTripList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment {


    String uid = "";
    float tvRate = 0;
    float totalStars = 0;
    ProfileModel model;
    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.postList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.logOut.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent p = new Intent(getContext(), WelcomeScreen.class);
            p.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(p);
//                getActivity().finish();
        });


        binding.editProfile.setOnClickListener(view1 -> {
            Intent p = new Intent(getContext(), Register.class);
            p.putExtra("is_edit", true);
            p.putExtra("MODEL", model);
            startActivity(p);
        });

        loadProfileData();
        //  loadTripData();


    }

    private void loadProfileData() {
        Context context;
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Profile Data");
        dialog.setCancelable(false);
        dialog.show();

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

        Log.d("TAG", "loadProfileData: " + uid);
        mref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    model = snapshot.getValue(ProfileModel.class);
                    binding.nameTv.setText(model.getName());
                    binding.mailTv.setText(model.getMail());
                    try {
                        String downloadURL = model.getPp();

                        Glide.with(getContext())
                                .load(downloadURL)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .error(R.drawable.traveller)
                                .into(binding.profilePic);
                    } catch (Exception e) {

                    }
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Error : Profile Not Found!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadTripData() {


        DatabaseReference placeReference = FirebaseDatabase.getInstance().getReference("places");
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("purchase_list");
        Query searchQuery = mref.orderByChild("user_id").equalTo(uid);
        FirebaseRecyclerOptions<TransactionModel> options;
        FirebaseRecyclerAdapter<TransactionModel, viewholderForMyTripList> firebaseRecyclerAdapter;
        options = new FirebaseRecyclerOptions.Builder<TransactionModel>().setQuery(searchQuery, TransactionModel.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TransactionModel, viewholderForMyTripList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForMyTripList holder, final int position, @NonNull TransactionModel model) {


                placeReference.child(model.getTrip_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        List<RatingModel> ratingModelList = new ArrayList<>();
                        LocationModel tripModel = snapshot.getValue(LocationModel.class);

                        placeReference.child(model.getTrip_id()).child("rating_list").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                boolean isRated = false;

                                if (snapshot.exists()) {

                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        RatingModel singleRatingModel = ds.getValue(RatingModel.class);
                                        if (singleRatingModel.getUser_id().equals(uid)) {
                                            isRated = true;


                                        }
                                        ratingModelList.add(singleRatingModel);
                                    }

                                }
                                //     holder.setDataToView(getContext(), isRated, model, tripModel, ratingModelList);

                                double totalSpend = Double.parseDouble(binding.totalSpend.getText().toString());
                                totalSpend = totalSpend + tripModel.getTrip_cost();

                                binding.totalSpend.setText(totalSpend + "");

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                        holder.textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showDialog(uid, tripModel.getId(), ratingModelList);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                int totalTrp = Integer.parseInt(binding.totalTrips.getText().toString());
                totalTrp = totalTrp + 1;

                binding.totalTrips.setText("" + totalTrp);


            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public viewholderForMyTripList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
                final viewholderForMyTripList viewholders = new viewholderForMyTripList(iteamVIew);

                return viewholders;
            }
        };

        firebaseRecyclerAdapter.startListening();
        binding.postList.setAdapter(firebaseRecyclerAdapter);


    }

    public void showDialog(String uid, String postID, List<RatingModel> ratingModelList) {
        totalStars = 0;
        tvRate = 0;
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.rating_dialoug);

        MaterialButton cancelBTN = dialog.findViewById(R.id.cancel_btn);
        MaterialButton acceptBTN = dialog.findViewById(R.id.submit_btn);
        TextInputEditText tvTitle = dialog.findViewById(R.id.descET);
        RatingBar tvRating = dialog.findViewById(R.id.ratingTv);

        tvRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRate = rating;
            }
        });

        for (RatingModel ratingModel : ratingModelList) {
            totalStars = totalStars + (float) ratingModel.getRating();
        }


        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acceptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String feedBack = tvTitle.getText().toString();

                if (feedBack.isEmpty()) {
                    tvTitle.setError("Can't Be Empty");
                } else if (tvRate == 0) {
                    Toast.makeText(getContext(), "Please Add A Rating", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();

                    float totalRating = (float) (totalStars + (float) tvRate) / (float) (ratingModelList.size() + 1);
                    Log.d("TAG", "onClick: " + totalRating + "total stars " + totalStars);
                    DatabaseReference mrf = FirebaseDatabase.getInstance().getReference("places")
                            .child(postID);


                    HashMap<String, Object> ratingMap = new HashMap<>();

                    ratingMap.put("user_id", uid);
                    ratingMap.put("feedback", feedBack);
                    ratingMap.put("rating", tvRate);

                    mrf.child("rating_list").child(uid).setValue(ratingMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    mrf.child("current_rating").setValue(totalRating + "");

                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Success  :  Your Feedback Received...", Toast.LENGTH_LONG).show();
                                    loadTripData();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                }


            }
        });

        dialog.show();


    }
}