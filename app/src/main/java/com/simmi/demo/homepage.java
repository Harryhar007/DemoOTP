package com.simmi.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class homepage extends AppCompatActivity {
    String phone,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        TextView username = findViewById(R.id.username);
        TextView num = findViewById(R.id.num);
        username.setText("Name: "+name);
        num.setText("Phone number: "+phone.toString());
    }
}