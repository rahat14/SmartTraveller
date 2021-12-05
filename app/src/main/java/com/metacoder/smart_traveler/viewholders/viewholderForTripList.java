package com.metacoder.smart_traveler.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.models.LocationModel;


public class viewholderForTripList extends RecyclerView.ViewHolder {

    private static viewholderForTripList.Clicklistener mclicklistener;
    View mview;

    public viewholderForTripList(@NonNull View itemView) {
        super(itemView);

        mview = itemView;


        //item click
        itemView.setOnClickListener(v -> mclicklistener.onItemClick(v, getAdapterPosition()));


    }

    public static void setOnClickListener(Clicklistener clickListener) {


        mclicklistener = clickListener;


    }

    public void setDataToView(Context context, LocationModel model) {

        TextView title = mview.findViewById(R.id.nameTv);
        TextView desc = mview.findViewById(R.id.descTv);
        ImageView tripImage = mview.findViewById(R.id.image);
        RatingBar ratingBar = mview.findViewById(R.id.rateTv);



        title.setText(model.getName());
        //    fareView.setText(fare);
        desc.setText(model.getDesc());


        try {
            ratingBar.setRating(Float.parseFloat(model.getCurrent_rating()));
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }


        Glide.with(context)
                .load(model.getMainImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.placeholder)
                .into(tripImage);

    }

    public interface Clicklistener {

        void onItemClick(View view, int postion);

    }

}
