package com.example.tmi.fragments;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<PostInfo> items = new ArrayList<PostInfo>();
    private static final String TAG = "PostAdapter";
    ImageButton heart;
    ArrayList userlist_heart;
    Boolean heart_clicked = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.post_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PostInfo item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;//프로필
        ImageView iv_heart;
        TextView tv_Dday;
        TextView tv_Date;
        TextView tv_Title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Dday = (TextView) itemView.findViewById(R.id.tv_Dday);
            imageView = itemView.findViewById(R.id.imageView);
            iv_heart = itemView.findViewById(R.id.iv_heart);
            tv_Date = (TextView) itemView.findViewById(R.id.tv_Date);
            tv_Title = (TextView) itemView.findViewById(R.id.tv_Title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //링크로 들어가기
                }
            });
        }

        public void setItem(PostInfo item) {
            tv_Dday.setText(item.getDDay());//D-day
            tv_Date.setText(item.getDueDate());//기한
            tv_Title.setText(item.getTitle());//제목

            DocumentReference docRef = db.collection("Exhibitions").document(item.getTitle());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }

            });


            /*공모전 이미지 불러오기*/
            //FirebaseStorage 인스턴스를 생성
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            // 위의 저장소를 참조하는 파일명으로 지정
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://gachon--tmi.appspot.com/images/" + item.getFilename());
            //StorageReference에서 파일 다운로드 URL 가져옴
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(itemView)
                                .load(task.getResult())
                                .override(1024, 980)
                                .into(imageView);
                    }
                }
            });

        }
    }

        public void addItem(PostInfo item) {
            items.add(item);
        }

        public void setItems(ArrayList<PostInfo> items) {
            this.items = items;
        }

        public PostInfo getItem(int position) {
            return items.get(position);
        }

        public void setItem(int position, PostInfo item) {
            items.set(position, item);
        }
    }

