package com.ampwork.workdonereportmanagement.faculty.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.fragments.ReportSubjectFragment;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.model.SubjectReportResponse;
import com.ampwork.workdonereportmanagement.model.SubjectResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportSubjectActivity extends AppCompatActivity {

    TextView tvReportId, tvTitle;
    TextInputLayout percInputLayout, subjectInputLayout,descInputLayout;
    TextInputEditText edPercentage,edDescription;
    AutoCompleteTextView  autoTvSubject;

    MaterialButton SubmitBtn;
    String reportId,programName,subjectCode;
    ProgressDialog progressDialog;

    Fragment fragment = new ReportSubjectFragment();
    Api api;
    List<SubjectModel> subjectModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report_subject);


        initializeViews();
        initializeToolBar();
        initializeTextWatcher();
        getSubjectList();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }



    private void initializeViews() {
        reportId = getIntent().getExtras().getString("reportId");
        programName = getIntent().getExtras().getString("program");

        api = ApiClient.getClient().create(Api.class);

        tvReportId = findViewById(R.id.tvReportId);

        subjectInputLayout = findViewById(R.id.subjectInputLayout);
        autoTvSubject = findViewById(R.id.SubjectAutoComTv);
        percInputLayout = findViewById(R.id.percInputLayout);
        edPercentage = findViewById(R.id.percEd);
        descInputLayout = findViewById(R.id.descInputLayout);
        edDescription = findViewById(R.id.descEd);
        SubmitBtn = findViewById(R.id.btnSubmit);
        tvReportId.setText(reportId);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Add Subject Report");
    }

    private void getSubjectList() {
        showProgressDialog("Please wait...");
        Call<SubjectResponse> call = api.getSubjects(programName);

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                int statusCode = response.code();
                SubjectResponse subjectResponse = response.body();
                String msg = subjectResponse.getMessage();
                boolean status = subjectResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Subjects list")) {
                                subjectModelList = subjectResponse.getSubjectModel();
                                if (subjectModelList.size() > 0) {
                                    ArrayAdapter<SubjectModel> sub_adapter = new ArrayAdapter<SubjectModel>(AddReportSubjectActivity.this, R.layout.list_item, subjectModelList);
                                    autoTvSubject.setAdapter(sub_adapter);
                                }

                            }
                        } else {
                            Toast.makeText(AddReportSubjectActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddReportSubjectActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddReportSubjectActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initializeTextWatcher() {
        autoTvSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    subjectInputLayout.setError(null);
                }

            }
        });

        edDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    descInputLayout.setError(null);
                }

            }
        });

        edPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    percInputLayout.setError(null);
                }

            }
        });
    }

    private void initializeClickEvents() {

        autoTvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectModel s = (SubjectModel) parent.getItemAtPosition(position);
                subjectCode = s.getSubject_code();
            }
        });

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = autoTvSubject.getText().toString();
                String perc = edPercentage.getText().toString();
                String desc = edDescription.getText().toString();
                if (subject.equals("Select")){
                    subjectInputLayout.setError(getResources().getString(R.string.subject_error_txt));
                }else if (TextUtils.isEmpty(perc)){
                    percInputLayout.setError(getResources().getString(R.string.perc_error_txt));
                }else {
                    SubjectReportResponse.SubjectReport subjectReport = new SubjectReportResponse.SubjectReport(reportId,subject,subjectCode,desc,perc);
                    SaveDetails(subjectReport);
                }
            }
        });
    }

    private void SaveDetails(SubjectReportResponse.SubjectReport subjectReport) {
        showProgressDialog("Saving data please wait...");
        Call<ApiResponse> call = api.addReportSubject(subjectReport);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                switch (statusCode){
                    case 200:
                        hideProgressDialog();
                        ApiResponse apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        boolean status = apiResponse.isStatus();
                        if (status){
                            if (msg.equals("Attendance Added Successfully")){
                                Toast.makeText(AddReportSubjectActivity.this, "Report added successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else {
                                onBackPressed();
                                Toast.makeText(AddReportSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            onBackPressed();
                            Toast.makeText(AddReportSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddReportSubjectActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddReportSubjectActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddReportSubjectActivity.this);
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