package com.example.tmi;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingActivity extends AppCompatActivity {
    private Intent moveToStart;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_splash);


        moveToStart = new Intent(this, SplashActivity.class);

        if(user != null){
            email = user.getEmail();
        }
        else{
            startActivity(moveToStart);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        TextView user_email = findViewById(R.id.user_email);
        user_email.setText(email);

        Button change_pw = findViewById(R.id.change_pw);
        Button logout = findViewById(R.id.logout);
        Button remove = findViewById(R.id.remove);

        // ...
        // Intent to scrap
        // ...

        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordReset(email);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signOut();
            }
        });

        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteUser();
            }
        });
    }
    public void sendPasswordReset(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Email sent");
                            Toast.makeText(SettingActivity.this, "이메일을 보냈습니다. 비밀번호를 변경해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Log.d(TAG, "Logout");
        Toast.makeText(SettingActivity.this, "로그아웃 성공",
                Toast.LENGTH_SHORT).show();
        startActivity(moveToStart);
        finish();
    }

    public void deleteUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(user.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User account deleted");
                            Toast.makeText(SettingActivity.this, "탈퇴 성공",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(moveToStart);
                            finish();
                        }

                    }
                });

    }
}
