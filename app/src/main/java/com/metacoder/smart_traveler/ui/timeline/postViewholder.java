package com.metacoder.smart_traveler.ui.timeline;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.utils.TIme;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class postViewholder extends RecyclerView.ViewHolder {

    private static ClickListener mClickListener;
    public TextView userName, userInterest, postDesc, like_count, comment_count, time_tv;
    public ImageView mediaContent, playBtn;

    public ImageButton moreBtn;
    public ImageView shareBtn, reactBtn;
    public LinearLayout container;
    CircleImageView profileView;
    View mView;
    String uid = FirebaseAuth.getInstance().getUid();

    public postViewholder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

    }

    public void setDetails(Context context, String username, String userInterett, String pp, String mediaLink,
                           long likeCount, long commentCount,
                           long timeStamp, String isImage,
                           String postText, Map<String, Boolean> likedUser) {
        // views
        // containerLayout = itemView.findViewById(R.id.container);
        userName = (TextView) mView.findViewById(R.id.userName);
        profileView = (CircleImageView) mView.findViewById(R.id.profile_pic);
        mediaContent = mView.findViewById(R.id.mediaViewer);
        like_count = mView.findViewById(R.id.likeNumber);
        container = mView.findViewById(R.id.row_container);
        comment_count = mView.findViewById(R.id.commentNumber);
        shareBtn = mView.findViewById(R.id.shareBtn);
        reactBtn = mView.findViewById(R.id.reactBtn);
        postDesc = mView.findViewById(R.id.postDesc);
        playBtn = mView.findViewById(R.id.playBTn);
        time_tv = mView.findViewById(R.id.time_tv);
        moreBtn = mView.findViewById(R.id.moreBtn);


        if (likedUser.containsKey(uid)) {
            reactBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_react_pressed));

        } else {
            reactBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_react_non_pressed));
        }

        if (isImage.contains("false")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int coloor = R.color.overLayer;
             //   mediaContent.setForeground(new ColorDrawable(ContextCompat.getColor(context, coloor)));
              //  playBtn.setVisibility(View.VISIBLE);
                //mediaContent.setForeground(Color.parseColor("#88000000"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               // mediaContent.setForeground(null);
            }
         //   playBtn.setVisibility(View.GONE);
        }

        if (postText.length() > 0) {
            postDesc.setVisibility(View.VISIBLE);
            postDesc.setText(postText);
        } else {
            postDesc.setVisibility(View.GONE);

        }


        userName.setText(username);
        time_tv.setText(TIme.From(timeStamp));
        comment_count.setText(commentCount + "");
        like_count.setText(likeCount + "");
        Glide.with(context).load(pp).thumbnail(0.25f).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(profileView);
        Glide.with(context).load(mediaLink).thumbnail(0.50f)
                .override(700, 700)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(mediaContent);


//        Picasso.get()
//        .load(mediaLink)
//                .placeholder(R.drawable.placeholder)
//                .into(mediaContent) ;

    }

    public void setOnClickListener(ClickListener clickListener) {

        mClickListener = clickListener;

    }


    public interface ClickListener {
        void onItemClick(View view, int position);

    }
}
