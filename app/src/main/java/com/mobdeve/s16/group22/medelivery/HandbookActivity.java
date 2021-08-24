package com.mobdeve.s16.group22.medelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HandbookActivity extends AppCompatActivity {

    private WebView webV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handbook);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.webV = findViewById(R.id.webV);
        this.webV.setWebViewClient(new WebViewClient());
        this.webV.loadUrl("https://www.drugs.com/drug_information.html");
    }

    @Override
    public void onBackPressed(){
        if(this.webV.canGoBack()){
            this.webV.goBack();
        }else{
            super.onBackPressed();
        }
    }
}