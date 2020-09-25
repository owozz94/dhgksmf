package com.example.ohaneul.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.ohaneul.Post;
import com.example.ohaneul.R;
import com.example.ohaneul.UploadActivity;
import com.example.ohaneul.adapter.HorizontalSlideImgAdapter;
import com.example.ohaneul.adapter.ImagePagerAdapter;
import com.example.ohaneul.adapter.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Fragment {

    ViewGroup viewGroup;
    private FloatingActionButton fab_main;

    private static final String TAG = "HomeActivity";
    private FirebaseFirestore firebaseFirestore;
    private HorizontalSlideImgAdapter HorizontalImgAdapter;
    private ArrayList<Post> postList;
    private boolean updating;


    private ArrayList<Integer> imageList;
    private static final int DP = 24;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
        final Context context = container.getContext();


        //슬라이드쇼
        this.InitializeData();
        final ViewPager viewPager = viewGroup.findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);
        TabLayout tabLayout = (TabLayout) viewGroup.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(new ImagePagerAdapter(context, imageList));
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == imageList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        //업로드 버튼
        fab_main = (FloatingActionButton) viewGroup.findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

        //가로 이미지 스크롤
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        HorizontalImgAdapter = new HorizontalSlideImgAdapter(getActivity(), postList);

        final RecyclerView recyclerView = viewGroup.findViewById(R.id.horizontalImageSlide);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(HorizontalImgAdapter);
        horizontalImgUpdate(false);


        return viewGroup;
    }

    private void InitializeData() {
        imageList = new ArrayList();

        imageList.add(R.drawable.milky_way);
        imageList.add(R.drawable.sunset);
        imageList.add(R.drawable.pier);
    }

    private void horizontalImgUpdate(final boolean clear) {
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
                                        document.getData().get("url") == null ? null : document.getData().get("url").toString(),
                                        document.getData().get("title").toString(),
                                        document.getId(),
                                        document.getData().get("uid").toString()
                                ));
                            }
                            HorizontalImgAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }

}

