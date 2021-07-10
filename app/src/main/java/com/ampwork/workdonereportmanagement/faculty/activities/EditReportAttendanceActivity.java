package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ReportAttendanceModel;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReportAttendanceActivity extends AppCompatActivity {

    ReportAttendanceModel attendanceModel;
    TextView tvTitle;
    TextInputLayout semInputLayout, subjectInputLayout, dateInputLayout, statusInputLayout;
    TextInputEditText edDate;
    AutoCompleteTextView autoTvSubject, autoTvSemester, autoTvStatus;
    MaterialButton updateBtn;
    ProgressDialog progressDialog;
    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report_attendance);

        initializeViews();
        initializeToolBar();
        setData();
    }


    private void initializeViews() {
        attendanceModel = getIntent().getParcelableExtra("data");
        api = ApiClient.getClient().create(Api.class);
        semInputLayout = findViewById(R.id.semesterInputLayout);
        subjectInputLayout = findViewById(R.id.subjectInputLayout);
        autoTvSubject = findViewById(R.id.SubjectAutoComTv);
        autoTvSemester = findViewById(R.id.semesterAutoComTv);
        dateInputLayout = findViewById(R.id.DateInputLayout);
        edDate = findViewById(R.id.DateEdt);
        statusInputLayout = findViewById(R.id.statusInputLayout);
        autoTvStatus = findViewById(R.id.statusAutoComTv);

        updateBtn = findViewById(R.id.updateButton);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Edit Report Attendance");
    }

    private void setData() {

        edDate.setText(attendanceModel.getAtt_date());
        autoTvSemester.setText(attendanceModel.getSemester());
        autoTvSubject.setText(attendanceModel.getSubject());
        autoTvStatus.setText(attendanceModel.getStatus());
        dateInputLayout.setEnabled(false);

        String status[] = {"present", "absent"};
        ArrayAdapter status_adapter = new ArrayAdapter(EditReportAttendanceActivity.this, R.layout.list_item, status);
        autoTvStatus.setAdapter(status_adapter);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = autoTvStatus.getText().toString();
                if (status.equalsIgnoreCase(attendanceModel.getStatus())) {
                    Toast.makeText(EditReportAttendanceActivity.this, "No data changes found", Toast.LENGTH_SHORT).show();
                } else {
                    List<ReportAttendanceModel> reportAttendanceModels = new ArrayList<>();
                    reportAttendanceModels.add(new ReportAttendanceModel(attendanceModel.getReport_id(),
                            attendanceModel.getStudent_id(), attendanceModel.getAtt_date(),
                            attendanceModel.getSubject(), attendanceModel.getSemester(), status));
                    sendReportDetails(reportAttendanceModels);
                }

            }
        });
    }

    private void sendReportDetails(List<ReportAttendanceModel> reportAttendanceModels) {
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
                                Toast.makeText(EditReportAttendanceActivity.this, "Attendance Updated Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(EditReportAttendanceActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditReportAttendanceActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(EditReportAttendanceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(EditReportAttendanceActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(EditReportAttendanceActivity.this);
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