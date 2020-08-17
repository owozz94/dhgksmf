package com.example.ohaneul;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.ohaneul.adapter.ImagePagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Fragment {

    ViewGroup viewGroup;

    private ArrayList<Integer> imageList;
    private static final int DP = 24;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    private LinearLayoutManager linearLayoutManager;
    private String TAG;
    private FloatingActionButton fab_main;
    ImageView imageData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
        final Context context = container.getContext();
        imageData = (ImageView) viewGroup.findViewById(R.id.imageData);


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

        //---------------------

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final TextView data = viewGroup.findViewById(R.id.data);
        db.collection("users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               // Log.d(TAG, document.getId() + " => " + document.getData());
                                data.setText(document.get("name").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //storageRef.child("gs://ohaneul-bc2ed.appspot.com/local/seoul/2020").getDownloadUrl().getResult();
        final String[] imageRef = new String[1];
       db.collection("local").document("seoul").collection("post")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                               imageRef[0] = document.get("url").toString();
                                StorageReference ref = FirebaseStorage.getInstance().getReference(imageRef[0]);
                                Glide.with(context).load(ref).into(imageData);
                            }
                        } else {
                           Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return viewGroup;
    }

   // private void downloadImg(StorageReference database) {
   //     String fileName = "Image";
    //    File fileDir = getExternalFilesDir()
   // }

    private void InitializeData() {
        imageList = new ArrayList();

        imageList.add(R.drawable.milky_way);
        imageList.add(R.drawable.sunset);
        imageList.add(R.drawable.pier);
    }
}