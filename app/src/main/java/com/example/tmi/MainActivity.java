package com.example.tmi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import com.example.tmi.fragments.JobFragment;
import com.example.tmi.fragments.DeadlineFragment;
import com.example.tmi.fragments.PopularFragment;
import com.example.tmi.fragments.ScrapFragment;
import com.example.tmi.fragments.LatestFragment;
import com.example.tmi.fragments.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private ImageButton setting;
    private  VPAdapter vpAdapter;
    public String UserUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = findViewById(R.id.settingBtn);

        Intent moveToSetting = new Intent(this, SettingActivity.class);

        setting.setOnClickListener(v -> {
            startActivity(moveToSetting);
        });

        searchView = findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 입력받은 문자열 처리
                Intent moveToSearchView = new Intent(getApplicationContext(), SearchViewActivity.class);
                moveToSearchView.putExtra("query", query);
                startActivity(moveToSearchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // 입력란의 문자열이 바뀔 때 처리
                return false;
            }
        });

        String txt_favorite = getString(R.string.tab_scrap);
        String txt_deadline = getString(R.string.tab_deadline);
        String txt_popular = getString(R.string.tab_popular);
        String txt_latest = getString(R.string.tab_latest);
        String txt_job = getString(R.string.tab_job);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new ScrapFragment(), txt_favorite);
        vpAdapter.addFragment(new LatestFragment(), txt_latest);
        vpAdapter.addFragment(new PopularFragment(), txt_popular);
        vpAdapter.addFragment(new DeadlineFragment(), txt_deadline);
        vpAdapter.addFragment(new JobFragment(), txt_job);

        viewPager.setAdapter(vpAdapter);


    }


    public void refresh(){
        vpAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage(R.string.exit);
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show();
    }


}