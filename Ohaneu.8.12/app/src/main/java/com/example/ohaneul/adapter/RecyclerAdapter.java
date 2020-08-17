package com.example.ohaneul.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohaneul.Data;
import com.example.ohaneul.R;
import com.example.ohaneul.Data;
import com.example.ohaneul.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    boolean favoritechk = true;
    ImageView favoriteImage;
    TextView favoriteText;
    List<Integer> favorite = Arrays.asList(
            R.drawable.ic_favorite,
            R.drawable.ic_favorite_border
    );

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView profileName;
        private ImageView profileImage;
        private TextView titleText;
        private TextView contentText;
        ImageView favoriteImage;
        TextView favoriteText;
        private ImageView imageContent;

        ItemViewHolder(View itemView) {
            super(itemView);

            profileName = itemView.findViewById(R.id.profile_name);
            profileImage = itemView.findViewById(R.id.profile_image);
            titleText = itemView.findViewById(R.id.title_text);
            contentText = itemView.findViewById(R.id.content_text);
            favoriteImage = itemView.findViewById(R.id.favorite_image);
            favoriteText = itemView.findViewById(R.id.favorite_text);
            imageContent = itemView.findViewById(R.id.image_content);

            //좋아요 기능
            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favoritechk == true) {
                        favoriteImage.setImageResource(R.drawable.ic_favorite);
                        //favoriteCount + 1
                        favoriteText.setText("좋아요 1개");
                        favoritechk = false;
                    } else {
                        favoriteImage.setImageResource(R.drawable.ic_favorite_border);
                        //favoriteCount - 1
                        favoriteText.setText("좋아요 0개");
                        favoritechk = true;
                    }
                }
            });
        }

        void onBind(Data data) {
            profileName.setText(data.getProfileName());
            profileImage.setImageResource(data.getProfileImage());
            titleText.setText(data.getTitle());
            contentText.setText(data.getContent());
            favoriteImage.setImageResource(data.getFavoriteImage());
            favoriteText.setText(data.getFavoriteText());
            imageContent.setImageResource(data.getImage());
        }
    }
}