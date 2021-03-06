package com.example.tmi.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tmi.MainActivity;
import com.example.tmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class PopularFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_popular, container, false);

        swipeRefreshLayout = view.findViewById(R.id.switerfresh);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //initialize views
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PostAdapter adapter = new PostAdapter(getContext());

        showData(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity)getActivity()).refresh();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void showData(PostAdapter adapter) {
        db.collection("Exhibitions").orderBy("NumPerson", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
