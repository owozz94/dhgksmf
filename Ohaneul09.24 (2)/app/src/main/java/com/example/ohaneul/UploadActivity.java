package com.example.ohaneul;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    ImageView image;
    EditText title, content;
    Button upload;
    private String TAG;
    private Uri filePath;
    private Spinner spinner;
    private String localText, local;

    FirebaseStorage storage;
    StorageReference storageRef;

    String folderdate, filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //firebase 접근
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
        }

        image = findViewById(R.id.add_image);
        title = findViewById(R.id.titleText);
        content = findViewById(R.id.contentText);
        upload = findViewById(R.id.upload);
        spinner = findViewById(R.id.spinner);

        //지역 카테고리
        String[] str = getResources().getStringArray(R.array.local);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItemPosition() > 0) {
                    localText = spinner.getSelectedItem().toString();
                    local = "";
                    if (spinner.getSelectedItemPosition() == 1){
                        local = "seoul";
                    } else if (spinner.getSelectedItemPosition() == 2){
                        local = "gyeonggido";
                    } else if (spinner.getSelectedItemPosition() == 3){
                        local = "gangwondo";
                    } else if (spinner.getSelectedItemPosition() == 4){
                        local = "chungcheongdo";
                    } else if (spinner.getSelectedItemPosition() == 5){
                        local = "jeollado";
                    } else {
                        local = "gyeongsangdo";
                    }//local
                }//categoryText
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(UploadActivity.this, "지역을 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
        });

        //Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //이미지를 선택
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        //내용&사진 업로드
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

                String titleText = title.getText().toString();
                String contentText = content.getText().toString();

                if (titleText != null && contentText != null && spinner.getSelectedItemPosition() > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("uid",user.getUid());
                    map.put("title", titleText);
                    map.put("content", contentText);
                    map.put("date", new Timestamp(new Date()));
                    map.put("local", localText);
                    map.put("deleteUrl", "local/" + local + "/" + folderdate + "/" + filename);
                    map.put("url", "https://firebasestorage.googleapis.com/v0/b/ohaneul-bc2ed.appspot.com/o/local%2F"
                                    +local+"%2F"+folderdate+"%2F"+filename+"?alt=media");

                    // Add a new document with a generated ID
                    db.collection("post").add(map);

                    onBackPressed();
                }else {
                    Toast.makeText(UploadActivity.this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }//if (titleText != null && contentText != null)
            }
        });
    }

    //업로드할 파일이 있으면 수행
    private void uploadFile() {
        if (filePath != null) {
            //storage
            storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            SimpleDateFormat formatter_folderdate = new SimpleDateFormat("yyyy");
            Date now = new Date();
            filename = formatter.format(now);
            folderdate = formatter_folderdate.format(now);
            //storage 주소와 폴더 파일명을 지정해 준다.
            storageRef = storage.getReferenceFromUrl("gs://ohaneul-bc2ed.appspot.com/").child("local/" + local + "/" + folderdate + "/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //뒤로가기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
