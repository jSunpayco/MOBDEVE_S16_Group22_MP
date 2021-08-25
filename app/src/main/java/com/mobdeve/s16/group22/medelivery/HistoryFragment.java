package com.mobdeve.s16.group22.medelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class HistoryFragment extends Fragment {


    private LinearLayout transaction_item;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        this.transaction_item = (LinearLayout) view.findViewById(R.id.test_transaction);
        transaction_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OverviewItemActivity.class);
                ((MainActivity) getActivity()).startActivity(intent);

            }
        });


        return view;
    }
}