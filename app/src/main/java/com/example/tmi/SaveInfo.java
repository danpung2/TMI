package com.example.tmi;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SaveInfo {

    private static final String TAG = "태그";
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
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

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
        database.collection("Exhibitions").document(Title).set(hashMap);

    }

    private void uploadFile(String imagePath) {


        //storage에 이미지 전송
        if (imagePath != null) {

            try {
                InputStream inputStream = new URL(imagePath).openStream();

            //storage
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

    public void updateExhibitions(){

        SimpleDateFormat sdformat = new
                SimpleDateFormat("yyyy.MM.dd.hh:mm");
        //기한 지난 공모전 삭제
        database.collection("Exhibitions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                try {
                                    String DueD = document.get("DueDate").toString();
                                    Date date1 = sdformat.parse(DueD.substring(0,10)+"."+DueD.substring(16));
                                    Date date_now = new Date(System.currentTimeMillis());
                                    Date date2 = sdformat.parse(sdformat.format(date_now));
                                    if(date2.compareTo(date1) > 0) {
                                        document.getReference().delete()
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
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference desertRef = storageRef.child("images/"+document.get("Title")+".png");
                                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                            }
                                        });

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

}
