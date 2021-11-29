package com.metacoder.transalvania.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoder.transalvania.R;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.RatingModel;

import de.hdodenhof.circleimageview.CircleImageView;


public class viewholderForReviewList extends RecyclerView.ViewHolder {

    private static viewholderForReviewList.Clicklistener mclicklistener;
    View mview;

    public viewholderForReviewList(@NonNull View itemView) {
        super(itemView);

        mview = itemView;


        //item click
    //    itemView.setOnClickListener(v -> mclicklistener.onItemClick(v, getAdapterPosition()));


    }

    public static void setOnClickListener(Clicklistener clickListener) {


        mclicklistener = clickListener;


    }

    public void setDataToView(Context context, RatingModel ratingModel, ProfileModel profileModel) {

        TextView title = mview.findViewById(R.id.nameTv);
        TextView desc = mview.findViewById(R.id.descTv);
        CircleImageView tripImage = mview.findViewById(R.id.image);
        RatingBar ratingBar = mview.findViewById(R.id.rateTv);


        title.setText(profileModel.getName());
        //    fareView.setText(fare);
        desc.setText(ratingModel.getFeedback() + "");

        try {
            ratingBar.setRating(ratingModel.getRating());
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }


//        Glide.with(context)
//                .load(model.getMainImage())
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .placeholder(R.drawable.placeholder)
//                .into(tripImage);

    }

    public interface Clicklistener {

        void onItemClick(View view, int postion);

    }

}
