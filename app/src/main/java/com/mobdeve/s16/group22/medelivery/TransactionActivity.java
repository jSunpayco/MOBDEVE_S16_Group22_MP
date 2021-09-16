package com.mobdeve.s16.group22.medelivery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {
    private TextView dateTv,totalAmountTv,statusTv,idTV;
    private TextView feedBackTV;
    private EditText feedbackEt;
    private Button submitBtn;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private String id;
    private String totalAmount;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transactions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.dateTv = findViewById(R.id.dateTv);
        this.totalAmountTv = findViewById(R.id.totalAmountTv);
        this.statusTv = findViewById(R.id.statusTv);
        this.idTV = findViewById(R.id.idTV);
        this.feedbackEt = findViewById(R.id.feedbackEt);
        this.feedBackTV = findViewById(R.id.feedbackEt);
        this.submitBtn = findViewById(R.id.submitBtn);
        this.ratingBar = findViewById(R.id.ratingBar);

        Intent i = getIntent();
        id = i.getStringExtra(FirebaseHelper.TRANSACTION_INTENT);

        this.recyclerView = findViewById(R.id.transactionRecyclerView);

        Query q = FirebaseHelper.getItemListCollectionReference(id);

        FirebaseHelper.getMyTransactionDocumentReference(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if(error!=null) {
                    Toast.makeText(TransactionActivity.this,"Listen Failed:" + error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(value != null && value.exists()) {
                    dateTv.setText(value.getString(FirebaseHelper.DATE_FIELD));
                    statusTv.setText(value.getString(FirebaseHelper.STATUS_FIELD));
                    idTV.setText(value.getString(FirebaseHelper.TID_FIELD));
                    totalAmount =  value.getString(FirebaseHelper.SUBTOTAL_FIELD);
                    totalAmountTv.setText("Total Amount: ₱" + totalAmount);
                    ratingBar.setRating(Float.parseFloat(value.getString(FirebaseHelper.RATING_FIELD)));

                    if(value.getString(FirebaseHelper.STATUS_FIELD).equals("Done")){
                        feedbackEt.setText(value.getString(FirebaseHelper.FEEDBACK_FIELD));
                        statusTv.setTextColor(Color.parseColor("#00FF00"));
                    }else if(value.getString(FirebaseHelper.STATUS_FIELD).equals("Pending")){
                        statusTv.setTextColor(Color.parseColor("#FF7F50"));
                    }else if(value.getString(FirebaseHelper.STATUS_FIELD).equals("Canceled")){
                        statusTv.setTextColor(Color.parseColor("#FF0000"));
                    }

                } else {
                    Toast.makeText(TransactionActivity.this,"Current data: null",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirestoreRecyclerOptions<CartModel> options = new FirestoreRecyclerOptions.Builder<CartModel>()
                .setQuery(q, CartModel.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<CartModel, TransactionViewHolder>(options) {
            @Override
            public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.transaction_item, parent, false);

                return new TransactionViewHolder(v);
            }
            @Override
            protected void onBindViewHolder(@NonNull TransactionViewHolder holder, int position, @NonNull CartModel model) {
                holder.medicineNameTv.setText(model.getCartName());
                holder.priceTv.setText("₱ " + String.valueOf(model.getCartPrice()));
                holder.quantityTv.setText("Qty: " + String.valueOf(model.getCartQuantity()));
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        this.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Feedback = feedBackTV.getText().toString();
                String date = dateTv.getText().toString();
                String tAmount = totalAmount;
                String currentId = idTV.getText().toString();
                String rating = String.valueOf(ratingBar.getRating());
                Map<String,Object> feedback = new HashMap<>();
                feedback.put(FirebaseHelper.FEEDBACK_FIELD,Feedback);
                feedback.put(FirebaseHelper.DATE_FIELD,date);
                feedback.put(FirebaseHelper.STATUS_FIELD,"Done");
                feedback.put(FirebaseHelper.SUBTOTAL_FIELD,tAmount);
                feedback.put(FirebaseHelper.TID_FIELD,currentId);
                feedback.put(FirebaseHelper.RATING_FIELD,rating);

                FirebaseHelper.getMyTransactionDocumentReference(id).set(feedback);
                Toast.makeText(TransactionActivity.this, "Submitted Feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.adapter.stopListening();
    }

    /*@Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }*/
}
