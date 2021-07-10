package com.ampwork.workdonereportmanagement.faculty.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.fragments.AddReportFragment;
import com.ampwork.workdonereportmanagement.faculty.fragments.FacultyHomeFragment;
import com.ampwork.workdonereportmanagement.faculty.fragments.GenerateReportFragment;
import com.ampwork.workdonereportmanagement.faculty.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FacultyNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Fragment homeFragment = new FacultyHomeFragment();
    Fragment profileFragment = new ProfileFragment();
    Fragment addReportFragment = new AddReportFragment();
    Fragment generateReportFragment = new GenerateReportFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_navigation);


        Toolbar toolbar = findViewById(R.id.toolbarnav);
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragments(homeFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = homeFragment;
                break;
            case R.id.navigation_addreport:
                fragment = addReportFragment;
                break;
            case R.id.navigation_generatereport:
                fragment = generateReportFragment;
                break;
            case R.id.navigation_profile:
                fragment = profileFragment;
                break;
        }
        return loadFragments(fragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof FacultyHomeFragment) {

            finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else
            super.onBackPressed();
    }

    private boolean loadFragments(Fragment fragment) {
        if (fragment != null) {
            Log.d("navigation", "loadFragments: Frag is loaded");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();

            return true;
        }
        return false;
    }
}