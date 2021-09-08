package com.mobdeve.s16.group22.medelivery;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OverviewItemViewHolder extends RecyclerView.ViewHolder{
    public TextView medicineNameTv, quantityTv, priceTv;

    public OverviewItemViewHolder(View itemView) {
        super(itemView);
        this.medicineNameTv = (TextView) itemView.findViewById(R.id.medicineNameTv);
        this.quantityTv = (TextView) itemView.findViewById(R.id.quantityTv);
        this.priceTv = (TextView) itemView.findViewById(R.id.priceTv);
    }
}
