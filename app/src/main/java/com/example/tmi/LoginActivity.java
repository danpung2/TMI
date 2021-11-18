package com.example.tmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent moveToFindPW = new Intent(this, FindPW.class);

        Button findPWBtn = findViewById(R.id.findPWBtn);
        findPWBtn.setOnClickListener(v -> {
            startActivity(moveToFindPW);
        });
    }
}