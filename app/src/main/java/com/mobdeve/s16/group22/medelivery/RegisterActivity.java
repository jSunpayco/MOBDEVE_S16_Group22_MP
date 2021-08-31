package com.mobdeve.s16.group22.medelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText registerFNameET, registerLNameET, registerMailET, registerPasswordET, registerAgeET,
            registerAddressET;
    Button registerBtn, goBackBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Medelivery - Register");

        this.registerFNameET = findViewById(R.id.registerFNameET);
        this.registerLNameET = findViewById(R.id.registerLNameET);
        this.registerMailET = findViewById(R.id.registerMailET);
        this.registerPasswordET = findViewById(R.id.registerPasswordET);
        this.registerAgeET = findViewById(R.id.registerAgeET);
        this.registerAddressET = findViewById(R.id.registerAddressET);
        this.registerBtn = findViewById(R.id.registerBtn);
        this.goBackBtn = findViewById(R.id.goBackBtn);

        this.fAuth = FirebaseAuth.getInstance();
        this.fstore = FirebaseFirestore.getInstance();

        this.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = registerFNameET.getText().toString().trim();
                String lname = registerLNameET.getText().toString().trim();
                String age = registerAgeET.getText().toString().trim();
                String address = registerAddressET.getText().toString().trim();
                String email = registerMailET.getText().toString().trim();
                String pass = registerPasswordET.getText().toString().trim();

                if(TextUtils.isEmpty(fname)){
                    registerFNameET.setError("Please input your first name.");
                    return;
                }

                if(TextUtils.isEmpty(lname)){
                    registerFNameET.setError("Please input your surname.");
                    return;
                }

                if(TextUtils.isEmpty(age)){
                    registerAgeET.setError("Please input your age.");
                    return;
                }

                if(Integer.parseInt(age) > 120 || Integer.parseInt(age) < 1){
                    registerAgeET.setError("Please input a valid age.");
                    return;
                }

                if(TextUtils.isEmpty(address)){
                    registerAddressET.setError("Please input your address.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    registerMailET.setError("Please input your email address.");
                    return;
                }

                if(pass.length() < 8){
                    registerPasswordET.setError("Please input a password with more than 8 characters.");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // Add to Database
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference docRef = fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fname", fname);
                            user.put("lname", lname);
                            user.put("email", email);
                            user.put("age", age);
                            user.put("address", address);
                            docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>(){
                                @Override
                                public void onSuccess(Void v){
                                    Log.d("TAG", "Success");
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        this.goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}