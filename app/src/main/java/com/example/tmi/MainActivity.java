package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import com.example.tmi.fragments.JobFragment;
import com.example.tmi.fragments.DeadlineFragment;
import com.example.tmi.fragments.PopularFragment;
import com.example.tmi.fragments.FavoriteFragment;
import com.example.tmi.fragments.LatestFragment;
import com.example.tmi.fragments.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private ImageButton setting;
    TextView textView_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = findViewById(R.id.settingBtn);

//        Intent moveToSetting = new Intent(this, SettingActivity.class);
//
//        setting.setOnClickListener(v -> {
//            startActivity(moveToSetting);
//        });

        searchView = findViewById(R.id.search_view);

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

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FavoriteFragment(), txt_favorite);
        vpAdapter.addFragment(new LatestFragment(), txt_latest);
        vpAdapter.addFragment(new PopularFragment(), txt_popular);
        vpAdapter.addFragment(new DeadlineFragment(), txt_deadline);
        vpAdapter.addFragment(new JobFragment(), txt_job);

        viewPager.setAdapter(vpAdapter);


        new Thread(() -> {
            ContestCrawler crawler = new ContestCrawler();
            try {
                crawler.LoadExhibition();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();




//        SaveInfo saveInfo = new SaveInfo(crawler.Title, crawler.First_category, crawler.DDay, crawler.Second_category, crawler.StartDate, crawler.DueDate,
//                crawler.Team, crawler.NumPerson, crawler.MaxNum, crawler.Link,crawler.Image_Link);
//        saveInfo.infoUpload();

    }
}