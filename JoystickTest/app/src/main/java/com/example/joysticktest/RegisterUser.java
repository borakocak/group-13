package com.example.joysticktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterUser extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mVehicleIdNumber, mName, mEmail, mPassword ,mConfirmPassword;
    Button mLoginBtn;
    Button mRegisterBtn;
    FirebaseAuth fAuth;  //instance of the class
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mVehicleIdNumber = findViewById(R.id.vehicleIdNumber);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterBtn = findViewById(R.id.registerBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String vehicleIdNumber = mVehicleIdNumber.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();
                String name = mName.getText().toString().trim();


                if (TextUtils.isEmpty(vehicleIdNumber)) {
                    mVehicleIdNumber.setError("Vehicle ID number is required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Name is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must contain at least 6 Characters");
                    return;
                }

                //authenticate the user

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterUser.this, "User Registered Successfully.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("vehicleIDNumber",vehicleIdNumber);
                            user.put("name",name);
                            user.put("email",email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for" +userID);

                                }
                            });

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(RegisterUser.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));

            }
        });
    }
}