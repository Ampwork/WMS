package com.ampwork.workdonereportmanagement.clerk.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.activities.faculty.FacultyHomeActivity;
import com.ampwork.workdonereportmanagement.clerk.activities.programs.ProgramsHomeActivity;
import com.ampwork.workdonereportmanagement.clerk.activities.student.StudentHomeActivity;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClerkHomeActivity extends AppCompatActivity {

    TextView tvUserName, tvUserPhone, tvUserEmail;

    CircleImageView profileImageView;
    TextDrawable drawable;
    ImageButton profileButton;


    PreferencesManager preferencesManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_home);

        initializeViews();
        initializeBottomNavigationBar();
        setData();

        initializeClickEvent();

    }

    private void initializeBottomNavigationBar() {
        final BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        navView.getMenu().setGroupCheckable(0, false, true);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_student) {
                    Intent studentIntent = new Intent(ClerkHomeActivity.this, StudentHomeActivity.class);
                    startActivity(studentIntent);
                } else if (item.getItemId() == R.id.navigation_programs) {
                    Intent programIntent = new Intent(ClerkHomeActivity.this, ProgramsHomeActivity.class);
                    startActivity(programIntent);
                } else if (item.getItemId() == R.id.navigation_faculty) {
                    Intent facultyIntent = new Intent(ClerkHomeActivity.this, FacultyHomeActivity.class);
                    startActivity(facultyIntent);

                }
                return true;
            }
        });
    }


    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);

        tvUserEmail = findViewById(R.id.tvEmailId);
        tvUserName = findViewById(R.id.tvName);
        tvUserPhone = findViewById(R.id.tvPhone);

        profileImageView = findViewById(R.id.imageView3);
        profileButton = findViewById(R.id.settings_btn);

    }


    private void setData() {
        String path = preferencesManager.getStringValue(AppConstants.PREF_PROFILE_URL);
        String fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        String lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);


        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        drawable = TextDrawable.builder().beginConfig()
                .width(150)
                .height(150)
                .bold()
                .endConfig()
                .buildRound(fName.substring(0, 1) + lName.substring(0, 1), color);
        if (!path.isEmpty()) {
            Picasso.with(this)
                    .load(path)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(profileImageView);

        } else {
            profileImageView.setImageDrawable(drawable);
        }
        tvUserName.setText(fName + " " + lName);
        tvUserPhone.setText(preferencesManager.getStringValue(AppConstants.PREF_PHONE));
        tvUserEmail.setText(preferencesManager.getStringValue(AppConstants.PREF_EMAIL));
    }


    private void initializeClickEvent() {
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClerkHomeActivity.this, ClerkProfileActivity.class);
                startActivity(intent);
            }
        });

    }


}