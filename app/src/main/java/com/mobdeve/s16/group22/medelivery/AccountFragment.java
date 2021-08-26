package com.mobdeve.s16.group22.medelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AccountFragment extends Fragment{

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_account, container, false);
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView myFnameTv, myLnameTv, myEmailTv, myAgeTv, myAddressTv;

        myFnameTv = v.findViewById(R.id.myFnameTv);
        myLnameTv = v.findViewById(R.id.myLnameTv);
        myEmailTv = v.findViewById(R.id.myEmailTv);
        myAgeTv = v.findViewById(R.id.myAgeTv);
        myAddressTv = v.findViewById(R.id.myAddressTv);


        DocumentReference documentReference = fstore.collection("users").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if(error!=null) {
                    System.err.println("Listen Failed:" + error);
                    return;
                }
                if(value != null && value.exists()) {
                    myFnameTv.setText(value.getString("fname"));
                    myLnameTv.setText(value.getString("lname"));
                    myEmailTv.setText(value.getString("email"));
                    myAgeTv.setText(value.getString("age"));
                    myAddressTv.setText(value.getString("address"));
                } else {
                    System.out.print("current data: null");
                }
            }
        });

        Button button = (Button) v.findViewById(R.id.logoutBtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}