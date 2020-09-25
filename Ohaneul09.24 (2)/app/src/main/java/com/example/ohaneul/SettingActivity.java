package com.example.ohaneul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Set;

import io.perfmark.Tag;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    TextView logout_view,leave_view,user_info;



    //데이터베이스에서 데이터를 읽거나 쓰려면 DatabaseReference의 인스턴스가 필요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        logout_view = findViewById(R.id.logout_view);
        leave_view = findViewById(R.id.leave_view);
        user_info = findViewById(R.id.user_info);
        user_info.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, UserInfo_Setting.class);
                startActivity(i);
            }
        });

        leave_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("회원탈퇴").setMessage("정말 탈퇴하시겠습니까?")
                        .setPositiveButton("탈퇴하기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(SettingActivity.this,"계정이 삭제되었습니다. 다음에 또 봐요 ㅠㅠ"
                                                ,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(intent);
                                        finish();

                                    }
                                });

                            }
                        })//setPositiveButton
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                Toast.makeText(SettingActivity.this,"사랑합니다♡",Toast.LENGTH_SHORT).show();;
                            }
                        });builder.show();
            }//onClick
        });//logoutButton.setOnClickListener

        //로그아웃
        logout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(SettingActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(i);
                                finish();
                            }
                        })//setPositiveButton
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).show();//setNegativeButton
            }//onClick
        });//logoutButton.setOnClickListener



    }
}
