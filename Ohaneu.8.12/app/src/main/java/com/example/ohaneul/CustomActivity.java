package com.example.ohaneul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ohaneul.adapter.RecyclerAdapter;

import java.util.Arrays;
import java.util.List;

public class CustomActivity extends AppCompatActivity {

    private Spinner localSpinner, timeSpinner;

    RecyclerAdapter adapter;
    Data data;
    ImageView favoriteImage;
    TextView favoriteText;
    boolean favoritechk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        localSpinner = findViewById(R.id.spinner_local);
        String[] strLocal = getResources().getStringArray(R.array.local);
        ArrayAdapter<String> localAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strLocal);
        localSpinner.setAdapter(localAdapter);

        timeSpinner = findViewById(R.id.spinner_time);
        String[] strTime = getResources().getStringArray(R.array.time);
        ArrayAdapter<String> timeAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strTime);
        timeSpinner.setAdapter(timeAdapter);

        //LayoutManager와 Adapter 객체를 만들어 RecyclerView에 연결
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        init();

        getData();

    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listProfileName = Arrays.asList(
                "무명", "아무개"
        );
        List<Integer> listProfileImage = Arrays.asList(
                R.drawable.pier,
                R.drawable.sunset
        );
        List<String> listTitle = Arrays.asList(
                "은하수", "노을"
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

    //뒤로가기
    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(UploadActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(CustomActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
