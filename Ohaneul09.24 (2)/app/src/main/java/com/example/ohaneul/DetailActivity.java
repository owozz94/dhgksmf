package com.example.ohaneul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.Date;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    String docID, uid;
    TextView profileName;
    ImageView profileImage;
    TextView titleText;
    TextView localText;
    TextView dateText;
    TextView contentText;
    ImageView imageContent;
    ImageView bookmark;
    boolean bookmarkChk = true;
    Button postDelete;

    FirebaseStorage storage;
    FirebaseFirestore db;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        docID = intent.getExtras().getString("document");
        uid = intent.getExtras().getString("uid");

        profileName = findViewById(R.id.profile_name);
        profileImage = findViewById(R.id.profile_image);
        titleText = findViewById(R.id.title_text);
        localText = findViewById(R.id.local_text);
        dateText = findViewById(R.id.date_text);
        contentText = findViewById(R.id.content_text);
        bookmark = findViewById(R.id.bookmark_image);
        imageContent = findViewById(R.id.image_content);
        postDelete = findViewById(R.id.post_delete);


        //동그란 프로필 사진
        profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profileImage.setClipToOutline(true);
        }

        //즐겨찾기 기능
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookmarkChk == true) {
                    bookmark.setImageResource(R.drawable.ic_bookmark);
                    bookmarkChk = false;
                } else {
                    bookmark.setImageResource(R.drawable.ic_bookmark_border);
                    bookmarkChk = true;
                }
            }
        });

        //게시글 삭제
        postDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("post").document(docID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                postDelete(docID, document.getData().get("deleteUrl").toString());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
                //postDelete(docID);
            }
        });

        setUI(docID);

        getName(uid);

    }

    public void setUI(String docID){
        DocumentReference docRef = db.collection("post").document(docID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        SimpleDateFormat sfd = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");

                        titleText.setText(document.getData().get("title").toString());
                        localText.setText("#"+document.getData().get("local").toString());
                        dateText.setText(sfd.format(document.getDate("date")));
                        contentText.setText(document.getData().get("content").toString());
                        if(document.getData().get("url") != null){
                            Glide.with(getApplicationContext())
                                    .load(document.getData().get("url"))
                                    .into(imageContent);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void getName(String uid){
        db.collection("user").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        profileName.setText(document.getData().get("name").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void postDelete(String docID, String url) {
        db.collection("post").document(docID)
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

        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child(url);

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

}
