package com.mobdeve.s16.group22.medelivery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private Button checkoutBtn, prescriptionBtn;
    private TextView cartTotalTv, errorImageTv;

    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    private Bitmap bitmap;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart);

        this.checkoutBtn = findViewById(R.id.checkoutBtn);
        this.prescriptionBtn = findViewById(R.id.prescriptionBtn);
        this.errorImageTv = findViewById(R.id.errorImageTv);
        this.cartTotalTv = findViewById(R.id.cartTotalTv);

        this.errorImageTv.setText("");

        this.recyclerView = findViewById(R.id.cartRecyclerView);

        Query q = FirebaseHelper.getMyCartCollectionReference();

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

                        newValue(cart.getCartUid());

                        if(tempQuantity == 0){
                            getSnapshots().getSnapshot(position).getReference().delete();
                        }else{
                            int tempPrice = Integer.parseInt(cart.getCartPrice()) -
                                    (Integer.parseInt(cart.getCartPrice()) / (tempQuantity + 1));

                            cart.setCartPrice(String.valueOf(tempPrice));
                            cart.setCartQuantity(String.valueOf(tempQuantity));

                            holder.cartPriceTv.setText("₱ " + String.valueOf(cart.getCartPrice()));
                            holder.cartStockTv.setText("Qty: " + String.valueOf(cart.getCartQuantity()));

                            FirebaseHelper.getCartDocumentReference().
                                    update(FirebaseHelper.CQUANTITY_FIELD, String.valueOf(tempQuantity),
                                            FirebaseHelper.CPRICE_FIELD, String.valueOf(tempPrice));
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
                    bitmap = (Bitmap) bundle.get(FirebaseHelper.DATA_INTENT);
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
                        if(bitmap != null){
                            FirebaseHelper.getMyCartCollectionReference()
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if(task.getResult().size() > 0) {
                                                    dialogInterface.dismiss();
                                                    addToCart();
                                                } else {
                                                    Toast.makeText(CartActivity.this,
                                                            "Add items to cart",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(CartActivity.this,
                                                        "Add items to cart",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            errorImageTv.setText("Please upload a prescription");
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
        FirebaseHelper.getMyCartCollectionReference()
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
                    total = count;
                }
            }
        });
    }

    protected void uploadPrescription(String _id){
        StorageReference reference = FirebaseHelper.getStorageInstance().getReference().child("prescriptions/" +
                _id);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartActivity.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                FirebaseHelper.getMyCartCollectionReference().document(_id)
                        .delete();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CartActivity.this, "Transaction Completed",
                        Toast.LENGTH_SHORT).show();
                bitmap = null;
                finish();

                Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                i.putExtra(FirebaseHelper.TRANSACTION_INTENT, _id);
                startActivity(i);
            }
        });
    }

    protected void addToCart(){
        Map<String,Object> transaction = new HashMap<>();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());

        transaction.put(FirebaseHelper.DATE_FIELD, date);
        transaction.put(FirebaseHelper.STATUS_FIELD, "Pending");
        transaction.put(FirebaseHelper.SUBTOTAL_FIELD, String.valueOf(total));
        transaction.put(FirebaseHelper.RATING_FIELD, "0");

        FirebaseHelper.getMyTransactionCollectionReference()
                .add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                transaction.put(FirebaseHelper.TID_FIELD, documentReference.getId());
                documentReference.update(transaction);
                addItems(documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartActivity.this, "Error adding transaction. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void addItems(String _id){
        FirebaseHelper.getMyCartCollectionReference()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartModel cart = document.toObject(CartModel.class);
                                FirebaseHelper.getTransactionItemDocumentReference(_id, cart.getCartUid())
                                        .set(cart);
                                document.getReference().delete();
                                adapter.notifyDataSetChanged();
                            }
                            uploadPrescription(_id);
                        }
                    }
                });
    }

    protected void newValue(String _id){
        FirebaseHelper.getItemsCollectionReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getReference().getId().equals(_id)){
                            document.getReference().update(FirebaseHelper.IQUANTITY_FIELD,
                                    document.getLong(FirebaseHelper.IQUANTITY_FIELD) + 1);
                        }
                    }
                }
            }
        });
    }
}
