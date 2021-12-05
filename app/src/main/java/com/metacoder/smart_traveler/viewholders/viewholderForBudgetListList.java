package com.metacoder.smart_traveler.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.models.BudgetModel;

import de.hdodenhof.circleimageview.CircleImageView;


public class viewholderForBudgetListList extends RecyclerView.ViewHolder {

    private static viewholderForBudgetListList.Clicklistener mclicklistener;
    View mview;

    public viewholderForBudgetListList(@NonNull View itemView) {
        super(itemView);

        mview = itemView;
        //item click
        itemView.setOnClickListener(v -> mclicklistener.onItemClick(v, getAdapterPosition()));


    }

    public static void setOnClickListener(Clicklistener clickListener) {


        mclicklistener = clickListener;


    }

    public void setDataToView(Context context, BudgetModel budgetModel) {

        TextView title = mview.findViewById(R.id.nameTv);
        TextView desc = mview.findViewById(R.id.descTv);
        CircleImageView tripImage = mview.findViewById(R.id.image);
        RatingBar ratingBar = mview.findViewById(R.id.rateTv);

        ratingBar.setVisibility(View.GONE);

        title.setText(budgetModel.getTitle());
        //    fareView.setText(fare);
        desc.setText(budgetModel.getFrom() + " - " + budgetModel.getTol());


        try{
            Glide.with(context)
                    .load(budgetModel.getImage())
                    .error(R.drawable.backpack)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(tripImage);
        }catch (Exception e ){

        }


    }

    public interface Clicklistener {

        void onItemClick(View view, int postion);

    }

}
