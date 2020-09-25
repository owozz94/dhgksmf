package com.example.ohaneul;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ohaneul.fragment.FavorActivity;
import com.example.ohaneul.fragment.HomeActivity;
import com.example.ohaneul.fragment.MypageActivity;
import com.example.ohaneul.fragment.RecentlyActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeActivity homeActivity = new HomeActivity();
    private FavorActivity favorActivity = new FavorActivity();
    private RecentlyActivity recentlyActivity = new RecentlyActivity();
    private MypageActivity mypageActivity = new MypageActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, homeActivity).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        //Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.tab1:
                    transaction.replace(R.id.frameLayout, homeActivity).commitAllowingStateLoss();
                    break;
                case R.id.tab2:
                    transaction.replace(R.id.frameLayout, favorActivity).commitAllowingStateLoss();
                    break;
                case R.id.tab3:
                    transaction.replace(R.id.frameLayout, recentlyActivity).commitAllowingStateLoss();
                    break;
                case R.id.tab4:
                    transaction.replace(R.id.frameLayout, mypageActivity).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }




}
