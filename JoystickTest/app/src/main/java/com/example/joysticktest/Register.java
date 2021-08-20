package com.example.joysticktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Register extends AppCompatActivity {

    EditText FullName;
    EditText Email;
    EditText Password;
    EditText Phone;
    Button RegisterBtn;
    Button BackBtn;
    TextView LoginHereBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName = findViewById(R.id.fullName);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        Phone = findViewById(R.id.phoneNumber);
        BackBtn = findViewById(R.id.backBtn);
        LoginHereBtn = findViewById(R.id.loginHereBtn);
        RegisterBtn = findViewById(R.id.registerBtn);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent (getApplicationContext(),MainActivity.class));
            finish();
        }

        RegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (TextUtils.isEmpty(email) ){
                    Email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password) ){
                    Email.setError("Password is Required.");
                    return;
                }
                if (password.length() < 6 ){
                    Password.setError("Password must be >= 6 Characters");
                }

                // register the user in the firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        // send verification link

                        FirebaseUser user = fAuth.getCurrentUser();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Register.this,"Verification E-mail has been sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("tag","onFailure: Email not sent." + e.getMessage());

                            }
                        });


                        Toast.makeText(Register.this, "User created." ,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }else{
                        Toast.makeText(Register.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        LoginHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (getApplicationContext(), LoginActivity.class));
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity2.class);
                startActivity(intent);
            }
        });

    }
}
