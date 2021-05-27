package com.example.joysticktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {
    EditText mVehicleIdNumber, mEmail, mPassword;
    Button mLoginBtn;
    Button mRegisterBtn;
    FirebaseAuth fAuth;  //instance of the class



        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            /*Button login = (Button) findViewById(R.id.login);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    //ComponentName componentName = new ComponentName("com.example.joysticktest","com.example.joysticktest.MainActivity");
                    //intent.setComponent(componentName);
                    startActivity(intent);
                }
            });*/

            mVehicleIdNumber = findViewById(R.id.vehicleIdNumber);
            mEmail = findViewById(R.id.email);
            mPassword = findViewById(R.id.password);
            mLoginBtn = findViewById(R.id.loginBtn);
            mRegisterBtn = findViewById(R.id.registerBtn);
            fAuth = FirebaseAuth.getInstance();

            if (fAuth.getCurrentUser() != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            mLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vehicleIdNumber = mVehicleIdNumber.getText().toString().trim();
                    String email = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();

                   /* Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    //ComponentName componentName = new ComponentName("com.example.joysticktest","com.example.joysticktest.MainActivity");
                    //intent.setComponent(componentName);
                    startActivity(intent);*/

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
                        mPassword.setError("Password must contain atleast 6 Characters");
                        return;
                    }


                }
            });
            mRegisterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
/*                    String email = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();
                    startActivity(new Intent(getApplicationContext(), RegisterUser.class));

                    //register the user in the firebase

                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity2.this, "User Logged in.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(MainActivity2.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                    startActivity(new Intent(getApplicationContext(), RegisterUser.class));


                }

            });
        }
    }