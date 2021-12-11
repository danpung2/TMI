package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView textView_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_view);
        textView_test = findViewById(R.id.textView_test);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 입력받은 문자열 처리
                textView_test.setText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // 입력란의 문자열이 바뀔 때 처리
                return false;
            }
        });




        String txt_favorite = getString(R.string.tab_favorite);
        String txt_deadline = getString(R.string.tab_deadline);
        String txt_popular = getString(R.string.tab_popular);
        String txt_latest = getString(R.string.tab_latest);
        String txt_job = getString(R.string.tab_job);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FavoriteFragment(), txt_favorite);
        vpAdapter.addFragment(new DeadlineFragment(), txt_latest);
        vpAdapter.addFragment(new PopularFragment(), txt_popular);
        vpAdapter.addFragment(new LatestFragment(), txt_deadline);
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