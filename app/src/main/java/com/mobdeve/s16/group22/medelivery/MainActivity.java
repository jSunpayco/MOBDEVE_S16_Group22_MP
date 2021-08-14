package com.mobdeve.s16.group22.medelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText loginFNameET, loginLNameET, loginMailET, loginPasswordET;
    Button loginBtn, noAccountBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.loginFNameET = findViewById(R.id.loginFNameET);
        //this.loginLNameET = findViewById(R.id.loginLNameET);
        this.loginMailET = findViewById(R.id.loginMailET);
        this.loginPasswordET = findViewById(R.id.loginPasswordET);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.noAccountBtn = findViewById(R.id.noAccountBtn);

        this.fAuth = FirebaseAuth.getInstance();

        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String fname = loginFNameET.getText().toString().trim();
                //String lname = loginLNameET.getText().toString().trim();
                String email = loginMailET.getText().toString().trim();
                String pass = loginPasswordET.getText().toString().trim();

//                if(TextUtils.isEmpty(fname)){
//                    registerFNameET.setError("Please input your first name.");
//                    return;
//                }

//                if(TextUtils.isEmpty(lname)){
//                    registerFNameET.setError("Please input your surname.");
//                    return;
//                }

                if(TextUtils.isEmpty(email)){
                    loginMailET.setError("Please input your email address.");
                    return;
                }

                if(pass.length() < 8){
                    loginPasswordET.setError("Please input your password.");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        this.noAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }
}