package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent moveToJoin = new Intent(this, JoinActivity.class);
        Intent moveToLogin = new Intent(this, LoginActivity.class);
        Intent moveToMain = new Intent(this, MainActivity.class);

        if(user != null){
            startActivity(moveToMain);
            finish();
        }

        Button joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(v -> {
            startActivity(moveToJoin);
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            startActivity(moveToLogin);
        });
    }
}