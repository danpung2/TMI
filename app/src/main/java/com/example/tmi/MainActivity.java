package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent moveToJoin = new Intent(this, JoinActivity.class);
        Intent moveToLogin = new Intent(this, LoginActivity.class);

        Button joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(v -> {
            startActivity(moveToJoin);
            finish();
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            startActivity(moveToLogin);
            finish();
        });

    }
}