package com.ampwork.workdonereportmanagement.faculty.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.ReportAttendanceModel;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.model.SubjectResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportAttendanceStepOneActivity extends AppCompatActivity {

    TextView tvReportId, tvTitle;
    TextInputLayout semInputLayout, subjectInputLayout,dateInputLayout;
    TextInputEditText edDate;
    AutoCompleteTextView autoTvSubject, autoTvSemester;

    MaterialButton nextBtn;
    ProgressDialog progressDialog;

    PreferencesManager preferencesManager;
    Api api;
    String reportId, programName;
    List<SubjectModel> subjectModelList = new ArrayList<>();
    ReportAttendanceModel attendanceModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report_attendance_step_one);

        initializeViews();
        initializeToolBar();
        getSubjectList();
        initializeTextWatcher();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);


    }

    private void initializeViews() {
        reportId = getIntent().getExtras().getString("reportId");
        programName = getIntent().getExtras().getString("program");
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);

        tvReportId = findViewById(R.id.tvReportId);
        semInputLayout = findViewById(R.id.semesterInputLayout);
        subjectInputLayout = findViewById(R.id.subjectInputLayout);
        autoTvSubject = findViewById(R.id.SubjectAutoComTv);
        autoTvSemester = findViewById(R.id.semesterAutoComTv);
        dateInputLayout = findViewById(R.id.DateInputLayout);
        edDate = findViewById(R.id.DateEdt);
        nextBtn = findViewById(R.id.btnNext);

        tvReportId.setText(reportId);
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

    private void initializeTextWatcher() {
        edDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    dateInputLayout.setError(null);
                }

            }
        });
        autoTvSemester.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    semInputLayout.setError(null);
                }

            }
        });

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
                                    loadSubjects(subjectModelList);
                                }

                            }
                        } else {
                            Toast.makeText(AddReportAttendanceStepOneActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddReportAttendanceStepOneActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddReportAttendanceStepOneActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSubjects(List<SubjectModel> subjectModelList) {
        String[] subjects = new String[subjectModelList.size()];
        int index = 0;
        for (SubjectModel value : subjectModelList) {
            subjects[index] = (String) value.getSubjectName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(AddReportAttendanceStepOneActivity.this, R.layout.list_item, subjects);
        autoTvSubject.setAdapter(sub_adapter);

        //load semester
        String sem[] = {"1", "2", "3", "4"};
        ArrayAdapter sem_adapter = new ArrayAdapter(AddReportAttendanceStepOneActivity.this, R.layout.list_item, sem);
        autoTvSemester.setAdapter(sem_adapter);
    }


    private void initializeClickEvents() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

        dateInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });

    }

    private void launchDatePicker() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select Attendance date");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        dateInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {


                Date date = null;
                try {
                    String string = materialDatePicker.getHeaderText();//"Jan 2, 10";

                    DateFormat format = new SimpleDateFormat("MMM d, yy", Locale.ENGLISH);
                    date = format.parse(string);

                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                    String output = outputFormat.format(date);

                    edDate.setText(output);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void validateFields() {

        String date = edDate.getText().toString();
        String sem = autoTvSemester.getText().toString();
        String subject = autoTvSubject.getText().toString();

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            //dateInputLayout.setError(getResources().getString(R.string.date_error_txt));
        }else if (TextUtils.isEmpty(sem) || sem.equals("Select")) {
            semInputLayout.setError(getResources().getString(R.string.sem_error_txt));
        } else if (TextUtils.isEmpty(subject) || subject.equals("Select")) {
            subjectInputLayout.setError(getResources().getString(R.string.subject_error_txt));
        } else {
            Intent intent = new Intent(AddReportAttendanceStepOneActivity.this, AddReportAttendanceStepTwoActivity.class);
            intent.putExtra("reportId", reportId);
            intent.putExtra("sem", sem);
            intent.putExtra("subject", subject);
            intent.putExtra("date", date);
            intent.putExtra("program",programName);
            startActivity(intent);
        }
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddReportAttendanceStepOneActivity.this);
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