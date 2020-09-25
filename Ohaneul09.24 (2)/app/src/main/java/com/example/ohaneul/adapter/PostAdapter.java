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
import com.example.ohaneul.Post;
import com.example.ohaneul.DetailActivity;
import com.example.ohaneul.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MainViewHolder>{

    TextView titleText;
    TextView localText;
    TextView dateText;
    TextView contentText;
    ImageView imageContent;
    ImageView bookmark;
    boolean bookmarkChk = true;
    List<Integer> favorite = Arrays.asList(
            R.drawable.ic_bookmark,
            R.drawable.ic_bookmark_border
    );

    private ArrayList<Post> mDataset;
    private Activity activity;
    private static final String TAG = "PostAdapter";

    static class MainViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        MainViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    public PostAdapter(Activity activity, ArrayList<Post> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public PostAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_recently, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return mainViewHolder;
    }

    //반복할 레이아웃 연결
    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        LinearLayout linearLayout = holder.linearLayout;
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        titleText = linearLayout.findViewById(R.id.title_text);
        localText = linearLayout.findViewById(R.id.local_text);
        dateText = linearLayout.findViewById(R.id.date_text);
        contentText = linearLayout.findViewById(R.id.content_text);
        bookmark = linearLayout.findViewById(R.id.bookmark_image);
        imageContent = linearLayout.findViewById(R.id.image_content);

        SimpleDateFormat sfd = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초");

        //데이터 가져옴
        final Post post = mDataset.get(position);
        //데이터 넣음
        titleText.setText(post.getTitle());
        localText.setText(post.getLocal());
        dateText.setText(sfd.format(post.getDate()));
        contentText.setText(post.getContent());
        if(mDataset.get(position).getImage() != null){
            Glide.with(activity).load(mDataset.get(position).getImage()).into(imageContent);
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

        //클릭시 Detail로 이동
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
