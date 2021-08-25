package com.mobdeve.s16.group22.medelivery;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ItemViewHolder  extends RecyclerView.ViewHolder {

    public TextView itemNameTv, itemPriceTv, itemStockTv;
    public ImageView itemImageIv;
    public FloatingActionButton addItemFab;

    public ItemViewHolder(View itemView) {
        super(itemView);
        this.itemNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
        this.itemPriceTv = (TextView) itemView.findViewById(R.id.itemPriceTv);
        this.itemStockTv = (TextView) itemView.findViewById(R.id.itemStockTv);
        this.itemImageIv = (ImageView) itemView.findViewById(R.id.itemImageIv);
        this.addItemFab = (FloatingActionButton) itemView.findViewById(R.id.addItemFab);
    }
}
