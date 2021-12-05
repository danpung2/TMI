package com.example.tmi;

import android.net.Uri;
import android.util.Log;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class SaveInfo {

    private static final String TAG = "LOG";
    private String filename = "no_image.jpg";
    private String Title;
    private String First_category;
    private String Second_category;
    private String DDay;
    private String StartDate;
    private String DueDate;
    private String Team;
    private String NumPerson;
    private String MaxNum;
    private String Link;
    private String Image_Link;

    public SaveInfo(String Title, String First_category, String DDay, String Second_category, String StartDate, String DueDate, String Team,
                    String NumPerson, String MaxNum, String Link, String Image_Link){
        this.Title = Title;
        this.First_category = First_category;
        this.DDay = DDay;
        this.Second_category = Second_category;
        this.StartDate = StartDate;
        this.DueDate = DueDate;
        this.Team = Team;
        this.NumPerson = NumPerson;
        this.MaxNum = MaxNum;
        this.Link = Link;
        this.Image_Link = Image_Link;

    }

    public void infoUpload(){

        // 이미지 storage에 업로드
        uploadFile(Image_Link);

        // 공모전 정보 firestore 에 업로드
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("Title", Title);
        hashMap.put("First_category", First_category);
        hashMap.put("DDay", DDay);
        hashMap.put("Second_category", Second_category);
        hashMap.put("StartDate", StartDate);
        hashMap.put("DueDate", DueDate);
        hashMap.put("Team", Team);
        hashMap.put("NumPerson", NumPerson);
        hashMap.put("MaxNum", MaxNum);
        hashMap.put("Link", Link);
        hashMap.put("filename", filename);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Exhibitions").document(Title).set(hashMap);
    }

    private void uploadFile(String imagePath) {
        //storage에 이미지 전송
        if (imagePath != null) {

            try {
                InputStream inputStream = new URL(imagePath).openStream();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //공모전 이릉으로 파일명 생성
            filename = Title + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://gachon--tmi.appspot.com").child("images/"+ filename);
            //업로드
            storageRef.putStream(inputStream)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

}
