package com.example.tmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tmi.fragments.AbilityFragment;
import com.example.tmi.fragments.DeadlineFragment;
import com.example.tmi.fragments.DeptFragment;
import com.example.tmi.fragments.FavoriteFragment;
import com.example.tmi.fragments.LatestFragment;
import com.example.tmi.fragments.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String txt_favorite = getString(R.string.tab_favorite);
        String txt_latest = getString(R.string.tab_latest);
        String txt_dept = getString(R.string.tab_dept);
        String txt_deadline = getString(R.string.tab_deadline);
        String txt_ability = getString(R.string.tab_ability);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FavoriteFragment(), txt_favorite);
        vpAdapter.addFragment(new LatestFragment(), txt_latest);
        vpAdapter.addFragment(new DeptFragment(), txt_dept);
        vpAdapter.addFragment(new DeadlineFragment(), txt_deadline);
        vpAdapter.addFragment(new AbilityFragment(), txt_ability);

        viewPager.setAdapter(vpAdapter);

    }
}