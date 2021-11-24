package com.metacoder.transalvania.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoder.transalvania.R;
import com.metacoder.transalvania.models.BudgetModel;
import com.metacoder.transalvania.models.ProfileModel;
import com.metacoder.transalvania.models.RatingModel;

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

    public void setDataToView(Context context, BudgetModel ratingModel) {

        TextView title = mview.findViewById(R.id.nameTv);
        TextView desc = mview.findViewById(R.id.descTv);
        CircleImageView tripImage = mview.findViewById(R.id.image);
        RatingBar ratingBar = mview.findViewById(R.id.rateTv);

        ratingBar.setVisibility(View.GONE);

        title.setText(ratingModel.getTitle());
        //    fareView.setText(fare);
        desc.setText(ratingModel.getFrom() + " - " + ratingModel.getTol());

    }

    public interface Clicklistener {

        void onItemClick(View view, int postion);

    }

}
