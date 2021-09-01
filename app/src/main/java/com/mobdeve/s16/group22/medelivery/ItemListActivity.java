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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private DocumentReference cartReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Item Search");

        this.recyclerView = findViewById(R.id.itemRecyclerVIew);
        this.swipeRefreshLayout = findViewById(R.id.itemScrollView);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.cartReference = firebaseFirestore.collection("cart").document(this.user.getUid());

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

                        cartReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if(documentSnapshot.get(item.getItemUid()) != null){
                                    List<String> tempList = (List<String>) documentSnapshot.get(item.getItemUid());

                                    int tempStock = Integer.parseInt(tempList.get(1)) + 1;
                                    int tempPrice = Integer.parseInt(tempList.get(2)) + item.getItemPrice();

                                    tempList.set(1, String.valueOf(tempStock));
                                    tempList.set(2, String.valueOf(tempPrice));

                                    cartReference.update(item.getItemUid(), tempList);

                                }else{
                                    Map<String, Object> tempItem = new HashMap<>();
                                    tempItem.put(item.getItemUid(), Arrays.asList(item.getItemName(),"1",
                                            String.valueOf(item.getItemPrice())));

                                    cartReference.set(tempItem, SetOptions.merge()).
                                            addOnSuccessListener(new OnSuccessListener<Void>(){
                                        @Override
                                        public void onSuccess(Void v){
                                            Log.d("TAG", "Success");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error writing document", e);
                                                }
                                            });
                                }

                                Toast.makeText(ItemListActivity.this, item.getItemName() + " added",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

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
