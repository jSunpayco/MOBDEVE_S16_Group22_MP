package com.mobdeve.s16.group22.medelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private FirestoreRecyclerAdapter adapter;
    private DocumentReference HistoryReference;
    private View view;
    //private LinearLayout transaction_item;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.recyclerView = view.findViewById(R.id.historyRecyclerView);
        this.HistoryReference = firebaseFirestore.collection("transaction").document(this.user.getUid());
        Query q = this.HistoryReference.collection("myTransactions");

        FirestoreRecyclerOptions<HistoryModel> options = new FirestoreRecyclerOptions.Builder<HistoryModel>()
                .setQuery(q, HistoryModel.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<HistoryModel, HistoryViewHolder>(options) {
            @Override
            public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_list, parent, false);

                return new HistoryViewHolder(v);
            }
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder holder, int position, @NonNull HistoryModel model) {
                holder.historyDate.setText(String.valueOf(model.getDate()));
                holder.historyStatus.setText(String.valueOf(model.getStatus()));
                holder.TransactionID.setText(String.valueOf(model.getTransactionID()));
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        this.adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.adapter.stopListening();
    }
}