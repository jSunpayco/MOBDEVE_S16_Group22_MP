package com.mobdeve.s16.group22.medelivery;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class OverviewItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_transaction_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
