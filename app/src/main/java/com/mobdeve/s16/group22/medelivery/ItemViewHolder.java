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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ItemViewHolder  extends RecyclerView.ViewHolder {

    public TextView itemNameTv, itemPriceTv, itemStockTv;
    public ImageView itemImageIv;

    public ItemViewHolder(View itemView) {
        super(itemView);
        this.itemNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
        this.itemPriceTv = (TextView) itemView.findViewById(R.id.itemPriceTv);
        this.itemStockTv = (TextView) itemView.findViewById(R.id.itemStockTv);
        this.itemImageIv = (ImageView) itemView.findViewById(R.id.itemImageIv);
    }

    public void bindData(ItemsModel im) {

        String path = "items/" + im.getItemPath();

        FirebaseStorage.getInstance().getReference().child(path).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(itemImageIv.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(downloadUrl)
                                .error(R.drawable.ic_baseline_error_24)
                                .placeholder(circularProgressDrawable)
                                .into(itemImageIv);
                    }
                });

        itemNameTv.setText(im.getItemName());
        itemPriceTv.setText("₱ " + im.getItemPrice());
        itemStockTv.setText("Qty: " + im.getItemQuantity());
    }
}
