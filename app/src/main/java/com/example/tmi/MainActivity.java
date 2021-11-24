package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tmi.fragments.AbilityFragment;
import com.example.tmi.fragments.DeadlineFragment;
import com.example.tmi.fragments.DeptFragment;
import com.example.tmi.fragments.FavoriteFragment;
import com.example.tmi.fragments.LatestFragment;

public class MainActivity extends AppCompatActivity{

    FavoriteFragment favoriteFragment;
    LatestFragment latestFragment;
    DeptFragment deptFragment;
    DeadlineFragment deadlineFragment;
    AbilityFragment abilityFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_fav = findViewById(R.id.btn_tab_favorite);
        Button btn_latest = findViewById(R.id.btn_tab_latest);
        Button btn_dept = findViewById(R.id.btn_tab_dept);
        Button btn_deadline = findViewById(R.id.btn_tab_deadline);
        Button btn_ability = findViewById(R.id.btn_tab_ability);


        favoriteFragment = new FavoriteFragment();
        latestFragment = new LatestFragment();
        deptFragment = new DeptFragment();
        deadlineFragment = new DeadlineFragment();
        abilityFragment = new AbilityFragment();

        //initial fragment -> favorite
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, favoriteFragment).commit();


        // button click event

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, favoriteFragment).commit();
            }
        });

        btn_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, latestFragment).commit();
            }
        });

        btn_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, deptFragment).commit();
            }
        });

        btn_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, deadlineFragment).commit();
            }
        });

        btn_ability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, abilityFragment).commit();
            }
        });

    }


}