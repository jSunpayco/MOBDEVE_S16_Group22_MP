package com.mobdeve.s16.group22.medelivery;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.recyclerView = findViewById(R.id.itemRecyclerVIew);
        this.swipeRefreshLayout = findViewById(R.id.itemScrollView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        this.recyclerView.setLayoutManager(gridLayoutManager);

        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);

        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                updateDataAndAdapter();
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
                                               if(task.isSuccessful()) {
                                                   ArrayList<ItemsModel> im = new ArrayList<>();
                                                   for (QueryDocumentSnapshot document : task.getResult())
                                                       im.add(document.toObject(ItemsModel.class));

                                                   itemAdapter.setData(im);
                                                   itemAdapter.notifyDataSetChanged();
                                               }
                                           }
                                       }
                );
    }
}
