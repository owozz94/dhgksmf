package com.example.ohaneul.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ohaneul.DetailActivity;
import com.example.ohaneul.Post;
import com.example.ohaneul.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavorAdapter extends RecyclerView.Adapter<FavorAdapter.MainViewHolder>{

    TextView titleText;
    ImageView thumbnail;

    private ArrayList<Post> mDataset;
    private Activity activity;
    private static final String TAG = "FavorAdapter";

    static class MainViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        MainViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    public FavorAdapter(Activity activity, ArrayList<Post> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public FavorAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_favor, parent, false);
        final FavorAdapter.MainViewHolder mainViewHolder = new FavorAdapter.MainViewHolder(linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return mainViewHolder;
    }


    //반복할 레이아웃 연결
    @Override
    public void onBindViewHolder(@NonNull final FavorAdapter.MainViewHolder holder, int position) {
        LinearLayout linearLayout = holder.linearLayout;
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        titleText = linearLayout.findViewById(R.id.title_favor);
        thumbnail = linearLayout.findViewById(R.id.thumbnail);

        final Post post = mDataset.get(position);
        titleText.setText(post.getTitle());
        if(mDataset.get(position).getThumbnail() != null){
            Glide.with(activity).load(mDataset.get(position).getThumbnail()).into(thumbnail);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("document",post.getDocID());
                intent.putExtra("uid",post.getUid());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
