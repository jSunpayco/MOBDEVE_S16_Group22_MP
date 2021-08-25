package com.mobdeve.s16.group22.medelivery;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
 //
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context context;
    private List<ItemsModel> items;

    public ItemAdapter(Context c, List<ItemsModel> im){
        this.context = c;
        this.items = im;
    }

    @Override
     public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_store_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.v.setOnClickListener
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

     @Override
     public int getItemCount() {
         return items.size();
     }

     public void setData(ArrayList<ItemsModel> im) {
         this.items = im;
     }

     class ViewHolder extends RecyclerView.ViewHolder {

         public TextView itemNameTv, itemPriceTv, itemStockTv;
         public ImageView itemImageIv;

         public ViewHolder(View itemView) {
             super(itemView);

             this.itemNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
             this.itemPriceTv = (TextView) itemView.findViewById(R.id.itemPriceTv);
             this.itemStockTv = (TextView) itemView.findViewById(R.id.itemStockTv);
             this.itemImageIv = (ImageView) itemView.findViewById(R.id.itemImageIv);
         }

         public void bindData(ItemsModel im) {

             String path = im.getImageUrl();

             FirebaseStorage.getInstance().getReference().child(path).getDownloadUrl()
                     .addOnCompleteListener(new OnCompleteListener<Uri>() {
                         @Override
                         public void onComplete(Task<Uri> task) {
                             CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(itemImageIv.getContext());
                             circularProgressDrawable.setCenterRadius(30);
                             Picasso.get()
                                     .load(task.getResult())
                                     .error(R.drawable.ic_baseline_error_24)
                                     .placeholder(circularProgressDrawable)
                                     .into(itemImageIv);
                         }
                     });

             itemNameTv.setText(im.getItemName());
             itemPriceTv.setText(im.getItemPrice());
             itemStockTv.setText(im.getItemQuantity());
         }
     }
}
