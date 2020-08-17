package com.example.ohaneul;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohaneul.adapter.RecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import static android.view.View.*;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecentlyActivity extends Fragment implements View.OnClickListener {

    ViewGroup viewGroup;

    Button seoul, gyeonggido, gangwondo, chungcheongdo, jeollado, gyeongsangdo;
    boolean Scheck, GGcheck, GWcheck, Ccheck, Jcheck, GScheck= false;

    RecyclerAdapter adapter;
    Data data;
    ImageView favoriteImage;
    TextView favoriteText;
    boolean favoritechk = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_recently, container, false);
        Context context = container.getContext();

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


        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);

        //LayoutManager와 Adapter 객체를 만들어 RecyclerView에 연결
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        init();

        getData();

        return viewGroup;
    }

    private void init() {
        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collectionGroup("post")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                            Log.d(TAG, snap.getId() + " => " + snap.getData());
                        }
                    }
                });

        // 임의의 데이터입니다.
        List<String> listProfileName = Arrays.asList(
                "무명", "아무개"
        );
        List<Integer> listProfileImage = Arrays.asList(
                R.drawable.pier,
                R.drawable.sunset
        );
        List<String> listTitle = Arrays.asList(
                "제목", "타이틀"
        );
        List<String> listContent = Arrays.asList(
                "은하수입니다.", "노을입니다."
        );
        List<Integer> listImage = Arrays.asList(
                R.drawable.milky_way,
                R.drawable.sunset
        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            data = new Data();
            data.setProfileName(listProfileName.get(i));
            data.setProfileImage(listProfileImage.get(i));
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            favoriteImage = data.setFavoriteImage(R.drawable.ic_favorite_border);
            favoriteText = data.setFavoriteText("좋아요 0개");
            data.setImage(listImage.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seoul:
                if (Scheck == false) {
                    seoul.setBackgroundResource(R.drawable.button1);
                    seoul.setTextColor(Color.WHITE);
                    Scheck = true;
                } else {
                    seoul.setBackgroundResource(R.drawable.button2);
                    seoul.setTextColor(getResources().getColorStateList(R.color.mainColor_2));
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

}

