package com.example.tmi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {
    public static Activity Splash_Activity;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Splash_Activity = SplashActivity.this;


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent moveToJoin = new Intent(this, JoinActivity.class);
        Intent moveToLogin = new Intent(this, LoginActivity.class);
        Intent moveToMain = new Intent(this, MainActivity.class);

        Button joinBtn = findViewById(R.id.joinBtn);
        Button loginBtn = findViewById(R.id.loginBtn);

        if(user != null){
            joinBtn.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            handler.postDelayed(new Runnable(){
                public void run(){
                    startActivity(moveToMain);
                    finish();
                }
            }, 1000); // 1sec



        }else{


            Animation alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_alpha);//나타나기 효과 객체
            joinBtn.startAnimation(alpha);//회원가입 버튼 나타나기
            loginBtn.startAnimation(alpha);//로그인 버튼 나타나기

            joinBtn.setOnClickListener(v -> {
                startActivity(moveToJoin);
            });

            loginBtn.setOnClickListener(v -> {
                startActivity(moveToLogin);
            });

        }



    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage(R.string.exit);
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show();
    }
}
