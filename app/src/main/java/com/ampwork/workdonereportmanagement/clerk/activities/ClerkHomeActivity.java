package com.ampwork.workdonereportmanagement.clerk.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.adapter.StudentCountAdapter;
import com.ampwork.workdonereportmanagement.model.ClerkResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClerkHomeActivity extends AppCompatActivity implements StudentCountAdapter.RecycleItemViewClicked {

    TextView tvUserName, tvUserPhone, tvUserEmail;
    ProgressDialog progressDialog;
    CircleImageView profileImageView;
    TextDrawable drawable;
    ImageButton profileButton;
    RecyclerView recyclerView;

    PreferencesManager preferencesManager;
    Api api;
    FloatingActionButton fabAdd;
    List<ClerkResponse.ProgramCount> programCounts = new ArrayList<>();

    StudentCountAdapter studentCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_home);

        initializeViews();
        setData();
        initializeRecyclerView();
        initializeClickEvent();
        getDashboardData();
    }


    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);
        tvUserEmail = findViewById(R.id.tvEmailId);
        tvUserName = findViewById(R.id.tvName);
        tvUserPhone = findViewById(R.id.tvPhone);

        profileImageView = findViewById(R.id.imageView3);
        profileButton = findViewById(R.id.settings_btn);
        fabAdd = findViewById(R.id.fab_add);
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
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClerkHomeActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }


    private void getDashboardData() {
        showProgressDialog("Please wait...");
        Call<ClerkResponse> call = api.getcount();
        call.enqueue(new Callback<ClerkResponse>() {
            @Override
            public void onResponse(Call<ClerkResponse> call, Response<ClerkResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ClerkResponse clerkResponse = response.body();
                        String msg = clerkResponse.getMessage();
                        boolean status = clerkResponse.isStatus();
                        if (status) {
                            if (msg.equals("Data Found")) {
                                programCounts = clerkResponse.getProgramCount();

                                if (programCounts.size() > 0) {
                                    studentCountAdapter = new StudentCountAdapter(ClerkHomeActivity.this, programCounts,ClerkHomeActivity.this);
                                    recyclerView.setAdapter(studentCountAdapter);
                                } else {

                                    Toast.makeText(ClerkHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ClerkHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ClerkHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(ClerkHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;

                }


            }

            @Override
            public void onFailure(Call<ClerkResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ClerkHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDashboardData();
    }


    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ClerkHomeActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(msg);
            progressDialog.show();
        } else {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemViewSelected(ClerkResponse.ProgramCount programCount) {

        Intent intent = new Intent(ClerkHomeActivity.this,DepartmentWiseStudentDetailsActivity.class);
        intent.putExtra("program",programCount.getProgram());
        startActivity(intent);
    }
}