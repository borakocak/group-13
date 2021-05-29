package com.example.joysticktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActicity extends AppCompatActivity {

    EditText registerUsername;
    EditText registerPassword;
    EditText passwordConfirmation;
    Button confirm;
    Button back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acticity);

        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(RegisterActicity.this);

        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        passwordConfirmation = findViewById(R.id.passwordConfirmation);
        confirm = findViewById(R.id.confirm);
        back = findViewById(R.id.back);



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordConfirmation.getText().toString().equals(registerPassword.getText().toString()) ){
                    boolean b = myDatabaseHelper.addOne(registerUsername.getText().toString(), registerPassword.getText().toString());
                    if(b) {
                        Toast.makeText(RegisterActicity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActicity.this,"Please make sure you enter the same password",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActicity.this,MainActivity2.class);
                startActivity(intent);
            }
        });







    }



}
