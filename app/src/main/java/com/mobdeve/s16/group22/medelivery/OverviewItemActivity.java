package com.mobdeve.s16.group22.medelivery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class OverviewItemActivity extends AppCompatActivity {
    private TextView dateTv,totalAmountTv,statusTv;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private FirestoreRecyclerAdapter adapter;
    private DocumentReference OverviewItemReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Transaction Details");

        this.dateTv = findViewById(R.id.dateTv);
        this.totalAmountTv = findViewById(R.id.totalAmountTv);
        this.statusTv = findViewById(R.id.statusTv);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.recyclerView = findViewById(R.id.transactionRecyclerView);
        this.OverviewItemReference = firebaseFirestore.collection("transactions").document(this.user.getUid());

        Query q = this.OverviewItemReference.collection("myTransactions");
        FirestoreRecyclerOptions<OverviewItemModel> options = new FirestoreRecyclerOptions.Builder<OverviewItemModel>()
                .setQuery(q, OverviewItemModel.class)
                .build();
        this.adapter = new FirestoreRecyclerAdapter<OverviewItemModel, OverviewItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OverviewItemViewHolder holder, int position, @NonNull OverviewItemModel model) {
                String name = model.getMedicineName();
                holder.medicineNameTv.setText(name.substring(0, name.indexOf(" ")));
                holder.priceTv.setText("₱ " + String.valueOf(model.getPrice()));
                holder.quantityTv.setText("Qty: " + String.valueOf(model.getQuantity()));
            }

            @Override
            public OverviewItemViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.transaction_item, parent, false);

                return new OverviewItemViewHolder(v);
            }
        };
        this.adapter.notifyDataSetChanged();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView.setAdapter(this.adapter);

        //updateTotal();
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

//    protected void updateTotal(){
//        OverviewItemReference.collection("myTransactions")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    int count = 0;
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        int price = Integer.parseInt(document.getString("itemPrice"));
//                        count = count + price;
//                    }
//                    totalAmountTv.setText(String.valueOf(count));
//                }
//            }
//        });
//    }
}
