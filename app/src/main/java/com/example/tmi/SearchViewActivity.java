package com.example.tmi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmi.fragments.PostAdapter;
import com.example.tmi.fragments.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseUser user;
    String searchWord;
    TextView tv_no_data;
    TextView tv_search_result;
    Boolean setVisible;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);
        Intent intent = getIntent();
        searchWord = intent.getStringExtra("query");
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_search_result = findViewById(R.id.tv_search_result);

        tv_no_data.setText("");
        tv_search_result.setText(searchWord + " 검색 결과");

        //initialize views
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PostAdapter adapter = new PostAdapter(this);

        setVisible = false;
        showData(adapter);
        if(setVisible)
        tv_no_data.setVisibility(View.VISIBLE);
        else tv_no_data.setVisibility(View.INVISIBLE);

    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void showData(PostAdapter adapter){
        db.collection("Exhibitions").orderBy("DueDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //show data
                    for (DocumentSnapshot doc : task.getResult()) {

                        String Title =  doc.getString("Title");
                        tv_no_data = findViewById(R.id.tv_no_data);
                        if(!Title.contains(searchWord)){
//                            tv_no_data.setText(R.string.no_data);
//                            tv_no_data.setTextColor(getResources().getColor(R.color.gray));
//                            recyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
                            setVisible = true;
                            continue;
                        }else{
//                            tv_no_data.setText("");
//                            tv_no_data.setTextColor(getResources().getColor(R.color.white));
                            setVisible = false;
                        }
                        String DDay = doc.getString("DDay");
                        String startDate = doc.getString("StartDate");
                        String DueDate = doc.getString("DueDate");
                        String Team = doc.getString("Team");
                        String NumPerson = doc.getString("NumPerson");
                        String MaxNum = doc.getString("MaxNum");
                        String Link = doc.getString("Link");
                        String filename = doc.getString("filename");

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        adapter.addItem(new PostInfo(Title, DDay, startDate, DueDate, Team, NumPerson, MaxNum, Link, filename));

                        //set adapter to recyclerview
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
