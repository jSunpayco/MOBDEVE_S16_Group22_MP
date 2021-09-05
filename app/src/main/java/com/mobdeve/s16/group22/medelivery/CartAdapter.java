package com.mobdeve.s16.group22.medelivery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter {

    private ArrayList<CartModel> mCart;

    public CartAdapter(ArrayList<CartModel> cart) {
        this.mCart = cart;
    }

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        CartViewHolder cartViewHolder = new CartViewHolder(v);
        return cartViewHolder;
    }

    public void onBindViewHolder(CartViewHolder holder, int position) {

        holder.cartNameTv.setText(mCart.get(position).getCartItem());
        holder.cartStockTv.setText(mCart.get(position).getCartQuantity());
        holder.cartPriceTv.setText(mCart.get(position).getCartPrice());

        holder.cartRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp;

                temp = Integer.parseInt(mCart.get(position).getCartQuantity()) - 1;

                // More than one stock of item in cart
                if(temp >= 1){
                    mCart.get(position).setCartQuantity(String.valueOf(temp));
                    int price = Integer.parseInt(mCart.get(position).getCartPrice()) -
                            ( Integer.parseInt(mCart.get(position).getCartPrice()) / (temp+1) );
                    mCart.get(position).setCartPrice(String.valueOf(price));
                }else{ // Zero stock of item in cart
                    mCart.remove(position);
                }
            }
        });
    }

    public int getItemCount() {
        return mCart.size();
    }

    public void setData(ArrayList<CartModel> carts) {
        this.mCart = mCart;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public TextView cartNameTv;
        public TextView cartStockTv;
        public TextView cartPriceTv;
        public Button cartRemoveBtn;

        public CartViewHolder(View itemView) {
            super(itemView);

            this.cartNameTv = itemView.findViewById(R.id.cartNameTv);
            this.cartStockTv = itemView.findViewById(R.id.cartStockTv);
            this.cartPriceTv = itemView.findViewById(R.id.cartPriceTv);
            this.cartRemoveBtn = itemView.findViewById(R.id.cartRemoveBtn);
        }
    }
}
