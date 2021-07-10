package com.ampwork.workdonereportmanagement.faculty.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.fragments.DailyReportsFragment;
import com.ampwork.workdonereportmanagement.faculty.fragments.ReportAttendanceFragment;
import com.ampwork.workdonereportmanagement.faculty.fragments.ReportSubjectFragment;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportsMainActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    String reportId,program,reportStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_main);

        initializeSharedData();
        initializeViews();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }

    private void initializeSharedData() {
        reportId = getIntent().getExtras().getString("reportId");
        program = getIntent().getExtras().getString("program");
        reportStatus = getIntent().getExtras().getString("status");
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addTitle("Daily Reports");
        adapter.addTitle("Report Attendance");
        adapter.addTitle("Report Subject");
        viewPager.setAdapter(adapter);
    }





    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DailyReportsFragment tab1 = new DailyReportsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("reportId", reportId);
                    bundle.putString("program", program);
                    bundle.putString("status", reportStatus);
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    ReportAttendanceFragment tab2 = new ReportAttendanceFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("reportId", reportId);
                    bundle1.putString("program", program);
                    bundle1.putString("status", reportStatus);
                    tab2.setArguments(bundle1);
                    return tab2;
                case 2:
                    ReportSubjectFragment tab3 = new ReportSubjectFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("reportId", reportId);
                    bundle2.putString("program", program);
                    bundle2.putString("status", reportStatus);
                    tab3.setArguments(bundle2);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mFragmentTitleList.size();
        }

        public void addTitle(String title) {
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }


}