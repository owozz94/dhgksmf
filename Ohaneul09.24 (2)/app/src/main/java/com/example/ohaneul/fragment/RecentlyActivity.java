package com.example.ohaneul.fragment;

import android.content.Context;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohaneul.Post;
import com.example.ohaneul.R;
import com.example.ohaneul.adapter.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RecentlyActivity extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;

    TextView seoul, gyeonggido, gangwondo, chungcheongdo, jeollado, gyeongsangdo;
    boolean Scheck, GGcheck, GWcheck, Ccheck, Jcheck, GScheck= false;

    private static final String TAG = "RecentlyActivity";
    private FirebaseFirestore firebaseFirestore;
    private PostAdapter postAdapter;
    private ArrayList<Post> postList;
    private boolean updating;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_recently, container, false);
        Context context = container.getContext();

        //필터 버튼
        seoul = viewGroup.findViewById(R.id.seoul);
        gyeonggido = viewGroup.findViewById(R.id.gyeonggido);
        gangwondo = viewGroup.findViewById(R.id.gangwondo);
        chungcheongdo = viewGroup.findViewById(R.id.chungcheongdo);
        jeollado = viewGroup.findViewById(R.id.jeollado);
        gyeongsangdo = viewGroup.findViewById(R.id.gyeongsangdo);
        //버튼 선택시
        seoul.setOnClickListener(this);
        gyeonggido.setOnClickListener(this);
        gangwondo.setOnClickListener(this);
        chungcheongdo.setOnClickListener(this);
        jeollado.setOnClickListener(this);
        gyeongsangdo.setOnClickListener(this);

        //RecyclerView Adapter 연결
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postList);

        final RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postAdapter);
        postsUpdate(false);


        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seoul:
                if (Scheck == false) {
                    seoul.setTextColor(Color.parseColor("#ffffff"));
                    Scheck = true;
                } else {
                    seoul.setTextColor(Color.parseColor("#99ffffff"));
                    Scheck = false;
                }//else
                break;
            case R.id.gyeonggido:
                if (GGcheck == false) {
                    gyeonggido.setBackgroundResource(R.drawable.button1);
                    gyeonggido.setTextColor(Color.WHITE);
                    GGcheck = true;
                } else {
                    gyeonggido.setBackgroundResource(R.drawable.button2);
                    gyeonggido.setTextColor(getResources().getColorStateList(R.color.mainColor_2));
                    GGcheck = false;
                }//else
                break;
            case R.id.gangwondo:
                if (GWcheck == false) {
                    gangwondo.setBackgroundResource(R.drawable.button1);
                    gangwondo.setTextColor(Color.WHITE);
                    GWcheck = true;
                } else {
                    gangwondo.setBackgroundResource(R.drawable.button2);
                    gangwondo.setTextColor(getResources().getColorStateList(R.color.mainColor_2));
                    GWcheck = false;
                }//else
                break;
            case R.id.chungcheongdo:
                if (Ccheck == false) {
                    chungcheongdo.setBackgroundResource(R.drawable.button1);
                    chungcheongdo.setTextColor(Color.WHITE);
                    Ccheck = true;
                } else {
                    chungcheongdo.setBackgroundResource(R.drawable.button2);
                    chungcheongdo.setTextColor(getResources().getColorStateList(R.color.mainColor_2));
                    Ccheck = false;
                }//else
                break;
            case R.id.jeollado:
                if (Jcheck == false) {
                    jeollado.setBackgroundResource(R.drawable.button1);
                    jeollado.setTextColor(Color.WHITE);
                    Jcheck = true;
                } else {
                    jeollado.setBackgroundResource(R.drawable.button2);
                    jeollado.setTextColor(getResources().getColorStateList(R.color.mainColor_2));
                    Jcheck = false;
                }//else
                break;
            case R.id.gyeongsangdo:
                if (GScheck == false) {
                    gyeongsangdo.setBackgroundResource(R.drawable.button1);
                    gyeongsangdo.setTextColor(Color.WHITE);
                    GScheck = true;
                } else {
                    gyeongsangdo.setBackgroundResource(R.drawable.button2);
                    gyeongsangdo.setTextColor(getResources().getColorStateList(R.color.mainColor_2));
                    GScheck = false;
                }//else
                break;
        }//switch
    }//onClick

    //데이터 입력
    private void postsUpdate(final boolean clear) {
        updating = true;
        CollectionReference collectionReference = firebaseFirestore.collection("post");
        collectionReference
                .orderBy("date", Query.Direction.DESCENDING).limit(20)
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
                                        document.getData().get("title").toString(),
                                        "#"+document.getData().get("local").toString(),
                                        document.getDate("date"),
                                        document.getData().get("content").toString(),
                                        document.getData().get("url") == null ? null : document.getData().get("url").toString(),
                                        document.getId(),
                                        document.getData().get("uid").toString()
                            ));
                        }
                            postAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }


}

