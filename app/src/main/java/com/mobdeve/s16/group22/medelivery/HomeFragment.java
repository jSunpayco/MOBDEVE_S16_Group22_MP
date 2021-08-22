package com.mobdeve.s16.group22.medelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    private ImageView list,about,cart,handbook;
    Intent intent;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.list = (ImageView) view.findViewById(R.id.listIb);
        this.about = (ImageView) view.findViewById(R.id.aboutIb);
        this.cart = (ImageView) view.findViewById(R.id.cartIb);
        this.handbook = (ImageView) view.findViewById(R.id.handbookIb);

        this.list.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked List");
                Intent intent = new Intent(getActivity(), ItemListActivity.class);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });
//        this.about.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked about");
//                startActivity(intent);
//            }
//        });
//        this.cart.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked cart");
//                startActivity(intent);
//            }
//        });
//        this.handbook.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked handbook");
//                startActivity(intent);
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }
}