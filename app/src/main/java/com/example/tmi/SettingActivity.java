package com.example.tmi;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;

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
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(SettingActivity.this);
                alBuilder.setMessage(R.string.delete_account_sure);
                alBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser();
                    }
                });

                alBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alBuilder.setTitle(R.string.delete_account);
                alBuilder.show();
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
                            StartToast(R.string.email_sent);
                        }

                    }
                });
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Log.d(TAG, "Logout");
        StartToast(R.string.logout_success);
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
                            StartToast(R.string.delete_account_success);
                            startActivity(moveToStart);
                            finish();
                        }

                    }
                });

    }

    public void StartToast(Integer msg){
        Toast toast = Toast.makeText(SettingActivity.this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }
}
