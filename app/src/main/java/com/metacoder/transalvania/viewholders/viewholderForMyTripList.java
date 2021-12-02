package com.metacoder.transalvania.viewholders;

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
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.models.RatingModel;
import com.metacoder.transalvania.models.TransactionModel;
import com.metacoder.transalvania.models.LocationModel;
import com.metacoder.transalvania.utils.ConvertTime;

import java.util.List;


public class viewholderForMyTripList extends RecyclerView.ViewHolder {

    private static viewholderForMyTripList.Clicklistener mclicklistener;
    public TextView textView;
    View mview;

    public viewholderForMyTripList(@NonNull View itemView) {
        super(itemView);

        mview = itemView;

        textView = itemView.findViewById(R.id.addRatingTv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mclicklistener.onItemClick(v, getAbsoluteAdapterPosition());
            }
        });
    }

    public static void setOnClickListener(Clicklistener clickListener) {


        mclicklistener = clickListener;


    }

    public void setDataToView(Context context, boolean isRating, TransactionModel transactionModel, LocationModel tripModel, List<RatingModel> ratingModelList) {
        TextView addRatingTv = mview.findViewById(R.id.addRatingTv);
        TextView title = mview.findViewById(R.id.nameTv);
        TextView desc = mview.findViewById(R.id.descTv);
        ImageView tripImage = mview.findViewById(R.id.image);
        RatingBar ratingBar = mview.findViewById(R.id.rateTv);

        if (isRating) {
            ratingBar.setVisibility(View.VISIBLE);
            addRatingTv.setVisibility(View.GONE);
        } else {
            ratingBar.setVisibility(View.GONE);
            addRatingTv.setVisibility(View.VISIBLE);
        }


        title.setText(tripModel.getName());
        //    fareView.setText(fare);

        desc.setText("Purchase Date : " + ConvertTime.getDate(transactionModel.getPurchase_time() , "dd/MM/yyyy"));

        try {
            ratingBar.setRating((float) Double.parseDouble(tripModel.getCurrent_rating()));
          // Log.d("TAG", "setDataToView:  " +   Double.parseDouble(tripModel.getCurrent_rating()));
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }


        Glide.with(context)
                .load(tripModel.getMainImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.placeholder)
                .into(tripImage);

    }

    public interface Clicklistener {

        void onItemClick(View view, int postion);

    }

}
