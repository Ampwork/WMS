package com.ampwork.workdonereportmanagement.clerk.activities.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.adapter.StudentCountAdapter;
import com.ampwork.workdonereportmanagement.model.ClerkResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeActivity extends AppCompatActivity implements StudentCountAdapter.RecycleItemViewClicked {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    FloatingActionButton fabAdd;

    Api api;
    List<ClerkResponse.ProgramCount> programCounts = new ArrayList<>();
    StudentCountAdapter studentCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);


        initializeViews();
        initializeRecyclerView();
        getDashboardData();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initializeViews() {
        fabAdd = findViewById(R.id.fab_add);
        api = ApiClient.getClient().create(Api.class);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeActivity.this, AddStudentActivity.class);
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
                                    studentCountAdapter = new StudentCountAdapter(StudentHomeActivity.this, programCounts, StudentHomeActivity.this);
                                    recyclerView.setAdapter(studentCountAdapter);
                                } else {

                                    Toast.makeText(StudentHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(StudentHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(StudentHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(StudentHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;

                }


            }

            @Override
            public void onFailure(Call<ClerkResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(StudentHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            progressDialog = new ProgressDialog(StudentHomeActivity.this);
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

        Intent intent = new Intent(StudentHomeActivity.this, DepartmentWiseStudentDetailsActivity.class);
        intent.putExtra("program", programCount.getProgram());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}