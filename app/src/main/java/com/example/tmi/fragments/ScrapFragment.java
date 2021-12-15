package com.example.tmi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tmi.MainActivity;
import com.example.tmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ScrapFragment extends Fragment {
    private static PostAdapter_Scrap adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FirebaseUser user;
    ImageButton btn_Delete;
    int item_count;
    TextView tv_no_scrap_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);

        swipeRefreshLayout = view.findViewById(R.id.switerfresh);
        user = FirebaseAuth.getInstance().getCurrentUser();
        btn_Delete = PostAdapter_Scrap.btn_Delete;
        tv_no_scrap_data = view.findViewById(R.id.tv_no_scrap_data);

        //initialize views
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).refresh();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();

    }

    public void refresh() {
        adapter = new PostAdapter_Scrap(getContext());
        item_count = adapter.getItemCount();
        adapter.notifyDataSetChanged();
        showData(adapter);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void showData(PostAdapter_Scrap adapter) {

        DocumentReference docRef = db.collection("Users").document(user.getUid());
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {

                // 배열에 저장된 searchList 제목을 가져온다.

                try {
                    List<String> group = (List<String>) document.get("scrapList");
                    if (group.size() > 0) {
                        tv_no_scrap_data.setVisibility(View.INVISIBLE);
                    } else {
                        tv_no_scrap_data.setVisibility(View.VISIBLE);
                        tv_no_scrap_data.setText(R.string.no_scrap_data);
                    }


                    //스크랩한 공모전이 존재하면
                    for (String title : group) {

                        /* searchList 이름과 Exhibitions의 제목 서치하여 생성 */
                        db.collection("Exhibitions").whereEqualTo("Title", title)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}