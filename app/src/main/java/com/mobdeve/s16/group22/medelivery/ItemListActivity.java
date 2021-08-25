package com.mobdeve.s16.group22.medelivery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.recyclerView = findViewById(R.id.itemRecyclerVIew);
        this.swipeRefreshLayout = findViewById(R.id.itemScrollView);
        this.firebaseFirestore = FirebaseFirestore.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        Query q = firebaseFirestore.collection("items");

        FirestoreRecyclerOptions<ItemsModel> options = new FirestoreRecyclerOptions.Builder<ItemsModel>()
                .setQuery(q, ItemsModel.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<ItemsModel, ItemViewHolder>(options){

            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_detail_layout, parent, false);

                return new ItemViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull ItemsModel item) {

                Glide.with(getApplicationContext())
                        .load(item.getItemPath())
                        .into(holder.itemImageIv);

                holder.itemNameTv.setText(item.getItemName());
                holder.itemPriceTv.setText("₱ " + String.valueOf(item.getItemPrice()));
                holder.itemStockTv.setText("Qty: " + String.valueOf(item.getItemQuantity()));

                holder.addItemFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Item added", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

        this.adapter.notifyDataSetChanged();
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(this.adapter);

        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                //updateDataAndAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateDataAndAdapter() {
        FirebaseFirestore.getInstance().collection("items")
                .orderBy("itemName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ItemsModel> im = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                im.add(document.toObject(ItemsModel.class));

                            //itemAdapter.setData(im);
                            //itemAdapter.notifyDataSetChanged();
                        }
                        }
                    }
                );
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
