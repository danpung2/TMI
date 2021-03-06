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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static boolean isLogin = false;
    private FirebaseAuth mAuth;
    private Intent moveToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent moveToFindPW = new Intent(this, FindPW.class);
        moveToMain = new Intent(this, MainActivity.class);

        mAuth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.tv_email);
        EditText password = findViewById(R.id.tv_password);

        Button findPWBtn = findViewById(R.id.findPWBtn);
        findPWBtn.setOnClickListener(v -> {
            startActivity(moveToFindPW);
        });

        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // editText length check
                if(email.length()==0 || password.length()==0){
                    StartToast(R.string.login_blank);
                }else signIn(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });
    }
    public void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            isLogin = true;
                            startActivity(moveToMain);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            StartToast(R.string.login_failed);
                            return;
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void StartToast(Integer msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }
}