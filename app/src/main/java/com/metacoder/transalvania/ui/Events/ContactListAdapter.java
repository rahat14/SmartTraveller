package com.metacoder.transalvania.ui.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoder.transalvania.R;
import com.metacoder.transalvania.models.ContactModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/*** Created by Rahat Shovo on 11/17/2021 
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.viewholder> {

    private final Context context;
    private List<ContactModel> items;
    private ItemClickListener itemClickListener;

    public ContactListAdapter(List<ContactModel> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    @NotNull
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        ContactModel item = items.get(position);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });

        holder.title.setText(item.getType());
        holder.location.setText( item.getArea() + " "+ item.getLocation());
        holder.desc.setText(item.getPhone());

    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public interface ItemClickListener {
        void onItemClick(ContactModel model);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView title, desc;
        TextView location;

        viewholder(@NonNull View mview) {
            super(mview);

            title = mview.findViewById(R.id.nameTv);
            desc = mview.findViewById(R.id.descTv);
            location = mview.findViewById(R.id.location);


        }


    }


}