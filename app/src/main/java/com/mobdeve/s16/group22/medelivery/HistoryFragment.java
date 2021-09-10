package com.mobdeve.s16.group22.medelivery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private FirebaseAuth mAuth;
    private FirestoreRecyclerAdapter adapter;
    private DocumentReference historyReference;
    private View view;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        this.mAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.user = mAuth.getCurrentUser();
        this.recyclerView = view.findViewById(R.id.historyRecyclerView);
        this.historyReference = firebaseFirestore.collection("transaction").document(this.user.getUid());
        Log.d("TAG", "User ID: " + this.user.getUid());
        Query q = this.historyReference.collection("myTransactions");

        FirestoreRecyclerOptions<HistoryModel> options = new FirestoreRecyclerOptions.Builder<HistoryModel>()
                .setQuery(q, HistoryModel.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<HistoryModel, HistoryViewHolder>(options) {
            @Override
            public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_item, parent, false);

                return new HistoryViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder holder, int position, @NonNull HistoryModel model) {
                Log.d("TAG", "Set Text: " + String.valueOf(model.getDate()));
                holder.historyDate.setText(String.valueOf(model.getDate()));
                holder.historyStatus.setText(String.valueOf(model.getStatus()));
                holder.TransactionID.setText(String.valueOf(model.getTransactionID()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), OverviewItemActivity.class);
                        DocumentReference documentReference = historyReference.
                                collection("myTransactions").document(model.getTransactionID());
                        String temp = model.getTransactionID();
                        i.putExtra("TRANSACTION_REFERENCE", temp);
                        startActivity(i);
                    }
                });
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManagerWrapper(getContext(),
                LinearLayoutManager.VERTICAL, false);
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

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }
}