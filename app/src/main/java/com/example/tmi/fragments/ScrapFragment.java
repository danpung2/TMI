package com.example.tmi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmi.LoginActivity;
import com.example.tmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ScrapFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_scrap, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //initialize views
        recyclerView = (RecyclerView)layout.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PostAdapter adapter = new PostAdapter();

        showData(adapter);

        return layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void showData(PostAdapter adapter){

        DocumentReference docRef = db.collection("Users").document(user.getUid());
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {


                // 배열에 저장된 HeartList 제목을 가져온다.
                List<String> group = (List<String>) document.get("HeartList");

                for (String title : group) {

                    /* HeartList와 이름과 Exhibitions의 제목 서치하여 생성 */
                    db.collection("Exhibitions").whereEqualTo("Title", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //show data
                                for (DocumentSnapshot doc : task.getResult()) {

                                    String Title = doc.getString("Title");
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
        });
    };
}