package com.example.ohaneul.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohaneul.Post;
import com.example.ohaneul.R;
import com.example.ohaneul.SettingActivity;
import com.example.ohaneul.UserDetailActivity;
import com.example.ohaneul.adapter.GridAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MypageActivity extends Fragment {

    static FirebaseAuth auth;
    ViewGroup viewGroup;
    TextView userName;
    ImageView profileImage,setting_btn;
    private String TAG;
    private FirebaseFirestore firebaseFirestore;
    private GridAdapter gridAdapter;
    private ArrayList<Post> postList;
    private Context mContext;
    private boolean updating;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_mypage, container, false);

        userName = viewGroup.findViewById(R.id.userName);
        profileImage = viewGroup.findViewById(R.id.profile_image);
        setting_btn = (ImageView) viewGroup.findViewById(R.id.setting_btn);
        mContext = getContext();
        profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profileImage.setBackground(new ShapeDrawable(new OvalShape()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profileImage.setClipToOutline(true);
        }

        //RecyclerView Adapter 연결
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        gridAdapter = new GridAdapter(getActivity(),postList);
        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);


        //격자무늬
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        recyclerView.setAdapter(gridAdapter);

        postsUpdate(false);

        auth = FirebaseAuth.getInstance();

        getData();
        //셋팅액티비티 이동
        setting_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SettingActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        //프로필액티비티 이동
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserDetailActivity.class);
                startActivity(intent);
            }
        });

        return viewGroup;

    }

    private void postsUpdate(final boolean clear) {
        updating = true;
        CollectionReference collectionReference = firebaseFirestore.collection("post");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //데이터 입력

        collectionReference .whereEqualTo("uid", user.getUid())
                //    .orderBy("date", Query.Direction.DESCENDING).limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                postList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(new Post(
                                        document.getData().get("url") ==null?null : document.getData().get("url").toString(),
                                        document.getId(),
                                        document.getData().get("uid").toString()
                                ));
                            }
                            gridAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }

    //유저 name 갖고오기
    private void getData(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final TextView userName = viewGroup.findViewById(R.id.userName);

        db.collection("user")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userName.setText(document.get("name").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}