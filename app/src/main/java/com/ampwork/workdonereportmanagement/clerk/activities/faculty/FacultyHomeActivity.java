package com.ampwork.workdonereportmanagement.clerk.activities.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.card.MaterialCardView;

public class FacultyHomeActivity extends AppCompatActivity {

    MaterialCardView subjectCardView,facultyCardView;
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);

        initializeViews();
        initializeClickEvent();
        //initializeToolBar();

        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }



    private void initializeViews() {
        subjectCardView = findViewById(R.id.cardViewSubject);
        facultyCardView = findViewById(R.id.cardViewFaculty);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("");
    }

    private void initializeClickEvent() {
        subjectCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyHomeActivity.this,SubjectsHomeActivity.class);
                startActivity(intent);
            }
        });

        facultyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyHomeActivity.this,FacultyDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}