package com.example.tmi;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tmi.fragments.PostAdapter;
import com.example.tmi.fragments.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FindPW extends AppCompatActivity {

    private String userEmail;
    ArrayList userEmailList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        EditText email = findViewById(R.id.email);
        Button sendBtn = findViewById(R.id.sendBtn);

        checkEmailExist();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString().trim();
                if (userEmailList.contains(userEmail))
                    sendPasswordReset(userEmail);
                else
                    StartToast(R.string.email_wrong);
            }
        });

    }


    public void checkEmailExist() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //show data
                    for (DocumentSnapshot doc : task.getResult()) {

                        String email = doc.getString("email");
                        System.out.println("eamils:" + email);
                        userEmailList.add(email);

                        System.out.println("user email list" + userEmailList);
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());

                }
            }
        });

        System.out.println("user email list" + userEmailList);
    }

    public void sendPasswordReset(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent");
                            StartToast(R.string.email_sent);
                        }

                    }
                });
    }

    public void StartToast(Integer msg) {
        Toast toast = Toast.makeText(FindPW.this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }
}