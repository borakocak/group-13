package com.example.joysticktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        EditText userName = findViewById(R.id.editTextTextPersonName);
        EditText passWord = findViewById(R.id.editTextTextPersonName2);
        TextView googleButton = findViewById(R.id.googleButton);
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(MainActivity2.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = myDatabaseHelper.Search(userName.getText().toString());
                if (passWord.getText().toString().equals(result))
                {
                    Toast.makeText(MainActivity2.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                    //ComponentName componentName = new ComponentName("com.example.joysticktest","com.example.joysticktest.MainActivity");
                    //intent.setComponent(componentName);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity2.this, "Please enter the right username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity2.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,Register.class);
                startActivity(intent);
            }
        });
    }
}