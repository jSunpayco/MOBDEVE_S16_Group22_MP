package com.mobdeve.s16.group22.medelivery;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About Us");

        FirebaseFirestore fstore = FirebaseFirestore.getInstance();

        TextView aboutTv, authorATv, authorBTv, authorEmailATv, authorEmailBTv;

        aboutTv = findViewById(R.id.myFnameTv);
        authorATv = findViewById(R.id.myLnameTv);
        authorBTv = findViewById(R.id.myEmailTv);
        authorEmailATv = findViewById(R.id.myAgeTv);
        authorEmailBTv = findViewById(R.id.myAddressTv);


        DocumentReference documentReference = fstore.collection("information").document("finalInformation");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if(error!=null) {
                    System.err.println("Listen Failed:" + error);
                    return;
                }
                if(value != null && value.exists()) {
                    aboutTv.setText(value.getString("bodyText"));
                    authorATv.setText(value.getString("authorA"));
                    authorEmailATv.setText(value.getString("emailA"));
                    authorBTv.setText(value.getString("authorB"));
                    authorEmailBTv.setText(value.getString("emailB"));
                } else {
                    System.out.print("current data: null");
                }
            }
        });
    }
}
