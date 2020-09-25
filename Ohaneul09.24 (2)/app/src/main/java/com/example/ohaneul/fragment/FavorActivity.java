package com.example.ohaneul.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohaneul.Post;
import com.example.ohaneul.R;
import com.example.ohaneul.adapter.FavorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavorActivity extends Fragment {
    ViewGroup viewGroup;
    private static final String TAG = "FavorActivity";
    private FirebaseFirestore firebaseFirestore;
    private FavorAdapter favorAdapter;
    private ArrayList<Post> postList;
    private boolean updating;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.activity_favor, container, false);

        //RecyclerView Adapter 연결
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        favorAdapter = new FavorAdapter(getActivity(), postList);

        final RecyclerView recyclerView = RootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(favorAdapter);
        postsUpdate(false);


        return RootView;
    }

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
                                        document.getData().get("url") == null ? null : document.getData().get("url").toString(),
                                        document.getData().get("title").toString(),
                                        document.getId(),
                                        document.getData().get("uid").toString()
                                ));
                            }
                            favorAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }

}


