package com.metacoder.smart_traveler.ui.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoder.smart_traveler.R;
import com.metacoder.smart_traveler.models.CalacModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/*** Created by Rahat Shovo on 11/17/2021 
 */
public class CalacModelListAdapter extends RecyclerView.Adapter<CalacModelListAdapter.viewholder> {

    private final Context context;
    private List<CalacModel> items;
    private ItemClickListener itemClickListener;

    public CalacModelListAdapter(List<CalacModel> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    @NotNull
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calculator, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        CalacModel item = items.get(position);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });

        holder.title.setText(item.getPrice());
        holder.desc.setText(item.getDesc());

    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(CalacModel model);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView title, desc;


        viewholder(@NonNull View mview) {
            super(mview);

            title = mview.findViewById(R.id.amt);
            desc = mview.findViewById(R.id.descTv);


        }


    }


}