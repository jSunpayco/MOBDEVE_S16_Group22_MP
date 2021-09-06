package com.mobdeve.s16.group22.medelivery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    private Button checkoutBtn, prescriptionBtn;
    private TextView cartTotalTv, errorImageTv;

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private FirestoreRecyclerAdapter adapter;
    private DocumentReference cartReference;
    private FirebaseStorage storage;

    private Bitmap bitmap;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private boolean isUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Shopping Cart");

        this.storage = FirebaseStorage.getInstance();

        this.checkoutBtn = findViewById(R.id.checkoutBtn);
        this.prescriptionBtn = findViewById(R.id.prescriptionBtn);
        this.errorImageTv = findViewById(R.id.errorImageTv);
        this.cartTotalTv = findViewById(R.id.cartTotalTv);

        this.recyclerView = findViewById(R.id.cartRecyclerView);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.cartReference = firebaseFirestore.collection("cart").document(this.user.getUid());

        Query q = this.cartReference.collection("myCart");

        FirestoreRecyclerOptions<CartModel> options = new FirestoreRecyclerOptions.Builder<CartModel>()
                .setQuery(q, CartModel.class)
                .build();

        this.adapter = new FirestoreRecyclerAdapter<CartModel, CartViewHolder>(options){
            @Override
            public CartViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cart_item, parent, false);

                return new CartViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel cart) {

                String name = cart.getCartName();
                //holder.cartNameTv.setText(name.substring(0,name.indexOf(" ")));
                holder.cartNameTv.setText(cart.getCartName());
                holder.cartPriceTv.setText("₱ " + String.valueOf(cart.getCartPrice()));
                holder.cartStockTv.setText("Qty: " + String.valueOf(cart.getCartQuantity()));

                holder.cartRemoveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int tempQuantity = Integer.parseInt(cart.getCartQuantity()) - 1;

                        if(tempQuantity == 0){
                            getSnapshots().getSnapshot(position).getReference().delete();
                        }else{
                            int tempPrice = Integer.parseInt(cart.getCartPrice()) -
                                    (Integer.parseInt(cart.getCartPrice()) / (tempQuantity + 1));

                            cart.setCartPrice(String.valueOf(tempPrice));
                            cart.setCartQuantity(String.valueOf(tempQuantity));

                            holder.cartPriceTv.setText("₱ " + String.valueOf(cart.getCartPrice()));
                            holder.cartStockTv.setText("Qty: " + String.valueOf(cart.getCartQuantity()));

                            cartReference.collection("myCart").document(cart.getCartUid()).
                                    update("cartQuantity", String.valueOf(tempQuantity),
                                            "cartPrice", String.valueOf(tempPrice));
                        }

                        updateTotal();
                    }
                });
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

        this.adapter.notifyDataSetChanged();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView.setAdapter(this.adapter);

        updateTotal();

        this.activityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                }
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(CartActivity.this);

                confirm.setMessage("Are you sure you want to proceed?")
                .setTitle("Confirmation")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(uploadPrescription()){
                            bitmap = null;
                            dialogInterface.dismiss();
                            finish();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
                });

                confirm.show();
            }
        });

        prescriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(i);
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

    @Override
    public void onBackPressed() {
        CartActivity.super.onBackPressed();
    }

    protected void updateTotal(){
        cartReference.collection("myCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        int price = Integer.parseInt(document.getString("cartPrice"));
                        count = count + price;
                    }
                    cartTotalTv.setText(String.valueOf(count));
                }
            }
        });
    }

    protected boolean uploadPrescription(){
        if(bitmap != null){
            StorageReference reference = storage.getReference().child("prescriptions/" +
                    UUID.randomUUID().toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = reference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CartActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    isUpload = false;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CartActivity.this, "Upload Success",
                            Toast.LENGTH_SHORT).show();
                    isUpload = true;
                }
            });
        }else{
            errorImageTv.setText("Please upload a prescription");
            isUpload = false;
        }

        return isUpload;
    }

}
