package com.mobdeve.s16.group22.medelivery;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

//
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<ItemsModel> items;

    public ItemAdapter(){
        this.items = new ArrayList<>();
    }

    @Override
     public ItemViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_detail_layout, parent, false);

        ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindData(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Item added", Toast.LENGTH_SHORT).show();
            }
        });
    }

     @Override
     public int getItemCount() {
         return items.size();
     }

     public void setData(ArrayList<ItemsModel> im) {
         this.items = im;
     }
}
