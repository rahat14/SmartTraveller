package com.metacoder.smart_traveler.ui.timeline;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.models.postModel;
import com.metacoder.smart_traveler.utils.ConvertTime;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class displayAllData extends RecyclerView.Adapter<displayAllData.ItemViewHolder> {
    public static final int ONE_VIEW = 0;
    public static final int VIEW = 1;
    private final List<postModel> mUserLsit;
    public LinearLayout container;
    private Context mContext;


    public displayAllData(Context mContext, List<postModel> mUserLsit) {
        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    @NonNull
    @Override
    public displayAllData.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_for_post, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull displayAllData.ItemViewHolder holder, final int position) {
        postModel post = mUserLsit.get(position);

        if (post.isImage.contains("false")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int coloor = R.color.overLayer;
            //    holder.mediaContent.setForeground(new ColorDrawable(ContextCompat.getColor(mContext, coloor)));
                holder.playBtn.setVisibility(View.GONE);

                //mediaContent.setForeground(Color.parseColor("#88000000"));
            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               // holder.mediaContent.setForeground(null);
                holder.playBtn.setVisibility(View.GONE);
            }
        }


        holder.userName.setText(post.userName);
        holder.time_tv.setText(ConvertTime.From(post.postTime));
        holder.comment_count.setText(post.commentCount + "");
        holder.like_count.setText(post.likeCount + "");
        Glide.with(mContext).load(post.userPicLink).thumbnail(0.25f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileView);
        Glide.with(mContext).load(post.postMediaLink).thumbnail(0.25f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mediaContent);


    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileView;
        TextView userName, userInterest, like_count, comment_count, time_tv;
        ImageView mediaContent, playBtn;
        ImageView shareBtn, reactBtn;
        TextView mTvName, mTvEmail, mTvPwd;

        public ItemViewHolder(View itemView) {
            super(itemView);
            View mView = itemView;
            // containerLayout = itemView.findViewById(R.id.container);
            userName = (TextView) mView.findViewById(R.id.userName);
            profileView = (CircleImageView) mView.findViewById(R.id.profile_pic);
            mediaContent = mView.findViewById(R.id.mediaViewer);
            like_count = mView.findViewById(R.id.likeNumber);
            container = mView.findViewById(R.id.row_container);
            comment_count = mView.findViewById(R.id.commentNumber);
            shareBtn = mView.findViewById(R.id.reactBtn);
            reactBtn = mView.findViewById(R.id.shareBtn);
            playBtn = mView.findViewById(R.id.playBTn);
            time_tv = mView.findViewById(R.id.time_tv);

        }
    }

}