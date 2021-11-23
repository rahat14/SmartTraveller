package com.metacoder.transalvania.ui.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoder.transalvania.R;
import com.metacoder.transalvania.models.BudgetModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/*** Created by Rahat Shovo on 11/17/2021 
 */
public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.viewholder> {

    private final Context context;
    private List<BudgetModel> items;
    private ItemClickListener itemClickListener;

    public BudgetListAdapter(List<BudgetModel> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    @NotNull
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        BudgetModel item = items.get(position);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });

        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getLowerLimit() + " - " + item.getUpperLimit() + "");

    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(BudgetModel model);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView title, desc;
        RatingBar ratingBar;
        ImageView tripImage;


        viewholder(@NonNull View mview) {
            super(mview);


            title = mview.findViewById(R.id.nameTv);
            desc = mview.findViewById(R.id.descTv);
            tripImage = mview.findViewById(R.id.image);
            ratingBar = mview.findViewById(R.id.rateTv);

            ratingBar.setVisibility(View.GONE);
            tripImage.setVisibility(View.GONE);

        }


    }


}