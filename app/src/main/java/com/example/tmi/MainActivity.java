package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

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
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> {
            startActivity(moveToLogin);
        });


        new Thread(() -> {
            ContestCrawler crawler = new ContestCrawler();
            try {
                crawler.LoadExhibition();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();




//        SaveInfo saveInfo = new SaveInfo(crawler.Title, crawler.First_category, crawler.DDay, crawler.Second_category, crawler.StartDate, crawler.DueDate,
//                crawler.Team, crawler.NumPerson, crawler.MaxNum, crawler.Link,crawler.Image_Link);
//        saveInfo.infoUpload();

    }
}