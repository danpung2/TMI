package com.example.tmi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        Button joinBtn = findViewById(R.id.joinBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //파이어베이스에 신규계정 등록하기
                firebaseAuth.createUserWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();

                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            Intent intent = new Intent(JoinActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(JoinActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(JoinActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            return;  //해당 메소드 진행을 멈추고 빠져나감.

                        }

                    }
                });

            }
        });

    }
}
