package com.example.tmi.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostAdapter_Scrap extends RecyclerView.Adapter<PostAdapter_Scrap.ViewHolder> {
    ArrayList<PostInfo> items = new ArrayList<PostInfo>();
    private static final String TAG = "PostAdapter_Scrap";
    private FirebaseUser user;
    private Context context;
    Boolean scrap_clicked = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostAdapter_Scrap(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.post_item_scrap, viewGroup, false);
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
        ImageView imageView;
        ImageView tv_Delete;
        TextView tv_Dday;
        TextView tv_Date;
        TextView tv_Title;
        TextView tv_Team;
        TextView tv_Maximum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Dday = (TextView) itemView.findViewById(R.id.tv_Dday);
            imageView = itemView.findViewById(R.id.imageView);
            tv_Delete = itemView.findViewById(R.id.tv_delete);
            tv_Date = (TextView) itemView.findViewById(R.id.tv_Date);
            tv_Title = (TextView) itemView.findViewById(R.id.tv_Title);
            tv_Team = (TextView) itemView.findViewById(R.id.tv_team);
            tv_Maximum = (TextView) itemView.findViewById(R.id.tv_maximum);
            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        public void setItem(PostInfo item) {
            tv_Dday.setText(item.getDDay()); // D-day
            tv_Date.setText(item.getDueDate()); // 기한
            tv_Title.setText(item.getTitle()); // 제목
            tv_Team.setText(item.getTeam()); // 개인 or 팀
            tv_Maximum.setText(item.getNumPerson() + "/" + item.getMaxNum()); // 현재 참여자 수 / 최대 참여자 수

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 링크 연결

                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getItem(pos).getLink()));
                        context.startActivity(intent);
                    }


                }
            });

            user = FirebaseAuth.getInstance().getCurrentUser();
            tv_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("Users").document(user.getUid())
                            .update("scrapList", FieldValue.arrayRemove(item.getTitle())); //파이어스토어에서 삭제
                    StartToast(R.string.scrap_cancel_success);
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

    public void StartToast(Integer msg){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }
}

