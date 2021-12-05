package com.metacoder.smart_traveler.ui.timeline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.models.counterModel;
import com.metacoder.smart_traveler.models.postModel;
import com.metacoder.smart_traveler.utils.ConvertTime;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class TimelinePage extends AppCompatActivity {
    RecyclerView postList;
    Context context;
    String uid;
    DatabaseReference postRef;
    FirebaseRecyclerAdapter<postModel, postViewholder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<postModel> options;
    LinearLayoutManager manager;
    displayAllData allDataAdapter;
    ProgressDialog dialog;
    DatabaseReference likeRef;
    boolean islike = false;
    AlertDialog alertDialog;
    DatabaseReference counterRef;
    private List<postModel> postListt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Timeline");

        context = getApplicationContext();
        manager = new LinearLayoutManager(context);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        postList = findViewById(R.id.postList);
        postList.setLayoutManager(manager);
        postList.setHasFixedSize(true);

        uid = FirebaseAuth.getInstance().getUid();


        postRef = FirebaseDatabase.getInstance().getReference("post");

        likeRef = FirebaseDatabase.getInstance().getReference("post");

        counterRef = FirebaseDatabase.getInstance().getReference("counterRef");
        loadPost();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Preparing Media.....");

        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(getApplicationContext(), CreatePostActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "You Are Not Logged In", Toast.LENGTH_LONG).show();
                }

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

    private void loadPost() {


        postRef.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<postModel>()
                .setQuery(postRef, postModel.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<postModel, postViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull postViewholder postViewholder, final int i, @NonNull postModel postModel) {

                //Context context , String username , String userInterett, String pp , String mediaLink,
                //    String likeCount , String commentCount


                postViewholder.setDetails(context, postModel.userName, "", postModel.userPicLink
                        , postModel.postMediaLink, postModel.likeCount,
                        postModel.commentCount, postModel.postTime, postModel.isImage, postModel.postText, postModel.likedUser);


                if (ConvertTime.From(postModel.postTime).contains("123456")) {
//                    postRef.child(getItem(postViewholder.getAbsoluteAdapterPosition()).postId).removeValue();
//                    postViewholder.itemView.setVisibility(View.GONE);
//                    postViewholder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

                } else {


                    postViewholder.itemView.setVisibility(View.VISIBLE);
                    postViewholder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                }


                postViewholder.shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (postModel.isImage.contains("true")) {
                            Log.d("TAG", "onClick:  isImage " + postModel.isImage);

                            try {
                                dialog.show();

                                postViewholder.mediaContent.invalidate();
                                BitmapDrawable drawable = (BitmapDrawable) postViewholder.mediaContent.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();

                                Bitmap image = null;


                                image = bitmap;

                                shareBitmap(image, postModel.postText);

                            } catch (Exception e) {
                                dialog.dismiss();
                                Log.d("TAG", "onClick: " + e.getMessage());
                                shareContent(getApplicationContext(), postModel.postText, postModel.postMediaLink);
                            }


                        } else
                            shareContent(getApplicationContext(), postModel.postText, postModel.postMediaLink);
                    }
                });


                postViewholder.userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent o = new Intent(context, otherProfile.class);
//                        o.putExtra("uid", getItem(i).userId);
//                        startActivity(o);

                    }
                });

                postViewholder.reactBtn.setOnClickListener(v -> {

                    //  biger then Marshmallow+
                    likeRef.child(postModel.postId).runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            postModel p = mutableData.getValue(postModel.class);
                            if (p == null) {
                                return Transaction.success(mutableData);
                            }

                            if (p.likedUser.containsKey(uid)) {

                                // Unstar the post and remove self from stars
                                if (p.likeCount > 0) {
                                    p.likeCount = p.likeCount - 1;
                                    islike = false;
                                    p.likedUser.remove(uid);
                                    //     postViewholder.reactBtn.setImageDrawable(context.getDrawable(R.drawable.ic_react_non_pressed));
                                }

                            } else {
                                // Star the post and add self to stars
                                p.likeCount = p.likeCount + 1;
                                p.likedUser.put(uid, true);
                                islike = true;
                                //   postViewholder.reactBtn.setImageDrawable(context.getDrawable(R.drawable.ic_react_pressed));
                            }

                            // Set value and report transaction success
                            mutableData.setValue(p);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean committed,
                                               DataSnapshot currentData) {
                            // Transaction completed
                            //   Log.d("TAG", "postTransaction:onComplete:" + databaseError);

                            updateThewholePostCounter(islike, postModel.userId);
                        }


                    });


                });


                postViewholder.moreBtn.setOnClickListener(v -> {

                    CharSequence[] textSize;

                    if (postModel.userId.contains(uid)) {
                        textSize = new CharSequence[]{"Report This Post", "Delete Your Post"};
                    } else {
                        textSize = new CharSequence[]{"Report This Post"};
                    }

                    // Toast.makeText(getApplicationContext() , "CLOCKED" , Toast.LENGTH_SHORT).show();


//                        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTheme);
//                        builder.setTitle("Options");
//                        builder.setSingleChoiceItems(textSize, -1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int item) {
//                                switch (item){
//                                    case 0:
//
//                                        reportThisPost(postModel.postId) ;
//                                        break;
//
//                                    case 1 :
//
//                                        deleteThisPost(postModel.postId) ;
//
//                                        break;
//                                }
//                                alertDialog.dismiss();
//                            }
//                        });
//                        alertDialog=builder.create();
//                        alertDialog.show();


                });


                postViewholder.setOnClickListener((view, position) -> {
                    // Toast.makeText(getContext() , "CLICKED " , Toast.LENGTH_LONG).show();

//                        Intent i = new Intent(context , postDetailsActivity.class) ;
//                        i.putExtra("postID" , getItem(position).postId) ;
//                        i.putExtra("mediaLink" , getItem(position).postMediaLink) ;
//                        i.putExtra("postText" , getItem(position).postText) ;
//                        i.putExtra("isImage" , getItem(position).isImage) ;
//                        i.putExtra("likes" , getItem(position).likeCount + "") ;
//                        i.putExtra("postUserId" , getItem(position).userId ) ;
//                        i.putExtra("comments" , getItem(position).commentCount + "") ;
//                        i.putExtra("category" , getItem(position).category) ;
//                        i.putExtra("time",getItem(position).postTime) ;
//
//                        context.startActivity(i);

                });


            }


            @NonNull
            @Override
            public postViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_row_for_post, parent, false);

                final postViewholder viewholder = new postViewholder(view);


                return viewholder;
            }
        };
        postList.setLayoutManager(manager);
        firebaseRecyclerAdapter.startListening();
        postList.setAdapter(firebaseRecyclerAdapter);


    }

    public void shareContent(Context context, String postText, String medialink) {
        String Text = postText + "\n" + medialink;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void shareBitmap(Bitmap bitmap, String postText) {
        final String shareText = postText + " "
                + getString(R.string.app_name) + " developed by "
                + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + ": \n\n";
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            File file = new File(context.getExternalCacheDir(), "share.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            dialog.dismiss();
            startActivity(Intent.createChooser(intent, "Share image via"));

        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("TAG", "shareBitmap:  Error : " + e.getMessage());
        }

    }

    private void updateThewholePostCounter(boolean islike, String userId) {

        counterRef.child(userId).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                counterModel p = mutableData.getValue(counterModel.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                // Star the post and add self to stars
                if (islike) {
                    p.likeCounter = p.likeCounter + 1;
                } else {

                    p.likeCounter = p.likeCounter - 1;
                }
                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {

                // Transaction completed


            }


        });
    }


}