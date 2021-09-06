package com.mobdeve.s16.group22.medelivery;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder  extends RecyclerView.ViewHolder{
    public TextView cartNameTv, cartStockTv, cartPriceTv;
    public Button cartRemoveBtn;

    public CartViewHolder(View itemView) {
        super(itemView);
        this.cartNameTv = (TextView) itemView.findViewById(R.id.cartNameTv);
        this.cartStockTv = (TextView) itemView.findViewById(R.id.cartStockTv);
        this.cartPriceTv = (TextView) itemView.findViewById(R.id.cartPriceTv);
        this.cartRemoveBtn = (Button) itemView.findViewById(R.id.cartRemoveBtn);
    }
}
