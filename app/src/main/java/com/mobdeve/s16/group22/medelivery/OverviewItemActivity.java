package com.mobdeve.s16.group22.medelivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OverviewItemActivity extends AppCompatActivity {
    private TextView dateTv,totalAmountTv,statusTv,idTV;
    private TextView feedBackTV;
    private Button submitBtn;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private FirestoreRecyclerAdapter adapter;
    private DocumentReference overviewItemReference;
    private String id;
    private String totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Details");

        this.dateTv = findViewById(R.id.dateTv);
        this.totalAmountTv = findViewById(R.id.totalAmountTv);
        this.statusTv = findViewById(R.id.statusTv);
        this.idTV = findViewById(R.id.idTV);
        this.feedBackTV = findViewById(R.id.feedbackEt);
        this.submitBtn = findViewById(R.id.submitBtn);

        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();

        Intent i = getIntent();
        id = i.getStringExtra("TRANSACTION_REFERENCE");

        this.recyclerView = findViewById(R.id.transactionRecyclerView);
        this.overviewItemReference = firebaseFirestore.collection("transaction").document(this.user.getUid());

        DocumentReference documentReference = this.overviewItemReference.collection("myTransactions").
                document(id);
        Query q = documentReference.collection("itemList");

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if(error!=null) {
                    Toast.makeText(OverviewItemActivity.this,"Listen Failed:" + error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(value != null && value.exists()) {
                    dateTv.setText(value.getString("date"));
                    statusTv.setText(value.getString("status"));
                    idTV.setText(value.getString("transactionID"));
                    totalAmount =  value.getString("totalAmount");
                    totalAmountTv.setText("Total Amount: ₱" + totalAmount);
                } else {
                    Toast.makeText(OverviewItemActivity.this,"Current data: null",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirestoreRecyclerOptions<CartModel> options = new FirestoreRecyclerOptions.Builder<CartModel>()
                .setQuery(q, CartModel.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<CartModel, OverviewItemViewHolder>(options) {
            @Override
            public OverviewItemViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.transaction_item, parent, false);

                return new OverviewItemViewHolder(v);
            }
            @Override
            protected void onBindViewHolder(@NonNull OverviewItemViewHolder holder, int position, @NonNull CartModel model) {
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
                String status = statusTv.getText().toString();
                String tAmount = totalAmount;
                String currentId = idTV.getText().toString();

                Map<String,Object> feedback = new HashMap<>();
                feedback.put("feedback",Feedback);
                feedback.put("date",date);
                feedback.put("status","Done");
                feedback.put("totalAmount",tAmount);
                feedback.put("transactionID",currentId);

                documentReference.set(feedback);
                Toast.makeText(OverviewItemActivity.this, "Submitted Feedback", Toast.LENGTH_SHORT).show();
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
}
