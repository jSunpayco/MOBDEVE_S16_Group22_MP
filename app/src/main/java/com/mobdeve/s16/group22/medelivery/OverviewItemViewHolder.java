package com.mobdeve.s16.group22.medelivery;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class OverviewItemViewHolder extends RecyclerView.ViewHolder{
    public TextView medicineNameTv, quantityTv, priceTv;

    public OverviewItemViewHolder(View itemView) {
        super(itemView);
        this.medicineNameTv = (TextView) itemView.findViewById(R.id.medicineNameTv);
        this.quantityTv = (TextView) itemView.findViewById(R.id.quantityTv);
        this.priceTv = (TextView) itemView.findViewById(R.id.priceTv);
    }

}
