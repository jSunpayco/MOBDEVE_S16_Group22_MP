package com.mobdeve.s16.group22.medelivery;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aboutus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore fstore = FirebaseFirestore.getInstance();

        TextView aboutTv, authorATv, authorBTv, authorEmailATv, authorEmailBTv;

        aboutTv = findViewById(R.id.aboutTv);
        authorATv = findViewById(R.id.authorATv);
        authorBTv = findViewById(R.id.authorBTv);
        authorEmailATv = findViewById(R.id.authorEmailATv);
        authorEmailBTv = findViewById(R.id.authorEmailBTv);


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
