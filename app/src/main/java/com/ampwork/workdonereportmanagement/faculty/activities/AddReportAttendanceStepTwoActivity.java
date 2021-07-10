package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.StudentAttendanceAdapter;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ReportAttendanceModel;
import com.ampwork.workdonereportmanagement.model.StudentDetailsModel;
import com.ampwork.workdonereportmanagement.model.StudentResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportAttendanceStepTwoActivity extends AppCompatActivity {

    TextView tvTitle;
    Api api;
    String reportId, subject, semester, date, program;
    List<StudentDetailsModel> studentDetailsModels = new ArrayList<>();
    ProgressDialog progressDialog;
    StudentAttendanceAdapter adapter;
    RecyclerView recyclerView;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report_attendance_step_two);

        initializeViews();
        initializeToolBar();
        getStudentList();
        initializeRecyclerView();
        initializeClickEvent();
    }


    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        reportId = getIntent().getExtras().getString("reportId");
        subject = getIntent().getExtras().getString("subject");
        semester = getIntent().getExtras().getString("sem");
        date = getIntent().getExtras().getString("date");
        program = getIntent().getExtras().getString("program");
        submit = findViewById(R.id.btnSubmit);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Add Report Attendance");
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }


    private void initializeClickEvent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<StudentDetailsModel> studentDetailsModels = new ArrayList<>();
                List<ReportAttendanceModel> reportAttendanceModels = new ArrayList<>();
                studentDetailsModels = adapter.updatedList();

                for (int i = 0; i < studentDetailsModels.size(); i++) {
                    if (studentDetailsModels.get(i).isChecked()) {
                        reportAttendanceModels.add(new ReportAttendanceModel(reportId,
                                studentDetailsModels.get(i).getStudentId(), date, subject, semester, "present"));
                    } else {
                        reportAttendanceModels.add(new ReportAttendanceModel(reportId,
                                studentDetailsModels.get(i).getStudentId(), date, subject, semester, "absent"));
                    }

                }

                if (reportAttendanceModels.isEmpty()) {
                    Toast.makeText(AddReportAttendanceStepTwoActivity.this, "Please mark/unmark student status", Toast.LENGTH_SHORT).show();
                } else {
                    AddReportDetails(reportAttendanceModels);
                }
            }
        });
    }

    private void AddReportDetails(List<ReportAttendanceModel> reportAttendanceModels) {
        showProgressDialog("Please wait...");
        Call<ApiResponse> call = api.addReportAttendance(reportAttendanceModels);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ApiResponse apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        boolean status = apiResponse.isStatus();
                        if (status) {
                            if (msg.equals("Attendance Added Successfully")) {
                                Toast.makeText(AddReportAttendanceStepTwoActivity.this, "Attendance Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddReportAttendanceStepTwoActivity.this, FacultyNavigationActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(AddReportAttendanceStepTwoActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddReportAttendanceStepTwoActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddReportAttendanceStepTwoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddReportAttendanceStepTwoActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStudentList() {
        showProgressDialog("Please wait...");
        Call<StudentResponse> call = api.getStudentsBySemester(program, semester);
        call.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(Call<StudentResponse> call, Response<StudentResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        StudentResponse studentResponse = response.body();
                        String msg = studentResponse.getMessage();
                        boolean status = studentResponse.isStatus();
                        if (status) {
                            studentDetailsModels = studentResponse.getStudentDetailsModels();
                            if (msg.equals("Student data")) {

                                adapter = new StudentAttendanceAdapter(AddReportAttendanceStepTwoActivity.this,
                                        studentDetailsModels);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(AddReportAttendanceStepTwoActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddReportAttendanceStepTwoActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddReportAttendanceStepTwoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                Toast.makeText(AddReportAttendanceStepTwoActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddReportAttendanceStepTwoActivity.this);
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


}