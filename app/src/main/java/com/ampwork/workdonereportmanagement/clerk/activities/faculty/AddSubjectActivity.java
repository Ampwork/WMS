package com.ampwork.workdonereportmanagement.clerk.activities.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.activities.student.AddStudentActivity;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSubjectActivity extends AppCompatActivity {

    TextView tvTitle;
    TextInputLayout subjectNameTextInputLayout,subjectCodeTextInputLayout, programTextInputLayout, semTextInputLayout;
    TextInputEditText edSubjectName,edSubjectCode;
    AutoCompleteTextView autoTvSemester, autoTvProgram;
    MaterialButton addSubjectBtn;
    List<ProgramResponse.ProgramModel> programModels = new ArrayList<>();
    ProgressDialog progressDialog;

    Api api;
    SubjectModel subjectModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        initializeViews();
        initializeToolBar();
        intializeData();
        initializeClickEvent();
        initializeTextWatcher();
        getProgramList();

        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }

    private void intializeData() {
        subjectModel  = getIntent().getParcelableExtra("subjectData");
        if (subjectModel!=null){
            edSubjectName.setText(subjectModel.getSubjectName());
            edSubjectCode.setText(subjectModel.getSubject_code());
            autoTvProgram.setText(subjectModel.getProgramName());
            autoTvSemester.setText(subjectModel.getSemester());
            tvTitle.setText("Update Subject");
            addSubjectBtn.setText("Update subject");
        }else {
            tvTitle.setText("Create Subject");

        }
    }


    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        subjectNameTextInputLayout = findViewById(R.id.subjectInputLayout);
        edSubjectName = findViewById(R.id.subjectEdt);
        subjectCodeTextInputLayout = findViewById(R.id.subjectCodeInputLayout);
        edSubjectCode = findViewById(R.id.subjectCodeEdt);
        programTextInputLayout = findViewById(R.id.programInputLayout);
        autoTvProgram = findViewById(R.id.programAutoComTv);
        semTextInputLayout = findViewById(R.id.semesterInputLayout);
        autoTvSemester = findViewById(R.id.semAutoComTv);

        addSubjectBtn = findViewById(R.id.createBtn);
    }

    private void initializeClickEvent() {
        addSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }


    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    private void initializeTextWatcher() {
        edSubjectCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    subjectCodeTextInputLayout.setError(null);
                }

            }
        });
        edSubjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    subjectNameTextInputLayout.setError(null);
                }

            }
        });

        autoTvProgram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    programTextInputLayout.setError(null);
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
                    semTextInputLayout.setError(null);
                }

            }
        });
    }
    private void getProgramList() {
        showProgressDialog("Please wait...");
        Call<ProgramResponse> call = api.getPrograms();
        call.enqueue(new Callback<ProgramResponse>() {
            @Override
            public void onResponse(Call<ProgramResponse> call, Response<ProgramResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ProgramResponse repose = response.body();
                        String msg = repose.getMessage();
                        boolean status = repose.isStatus();
                        if (status) {
                            if (msg.equals("Programs list")) {
                                assert response.body() != null;
                                programModels = response.body().getProgramModels();
                                if (programModels.size() > 0) {
                                    loadPrograms(programModels);
                                }

                            }
                        } else {
                            Toast.makeText(AddSubjectActivity.this, "Programs not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddSubjectActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddSubjectActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPrograms(List<ProgramResponse.ProgramModel> programModels) {
        String[] subjects = new String[programModels.size()];
        int index = 0;
        for (ProgramResponse.ProgramModel value : programModels) {
            subjects[index] = (String) value.getProgramName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(AddSubjectActivity.this, R.layout.list_item, subjects);
        autoTvProgram.setAdapter(sub_adapter);

        //load semester
        String[] sem = {"1", "2", "3", "4"};
        ArrayAdapter sem_adapter = new ArrayAdapter(AddSubjectActivity.this, R.layout.list_item, sem);
        autoTvSemester.setAdapter(sem_adapter);


    }

    private void validateForm() {
        String subjectName = edSubjectName.getText().toString();
        String subjectCode = edSubjectCode.getText().toString();
        String program = autoTvProgram.getText().toString();
        String semester = autoTvSemester.getText().toString();

        if (TextUtils.isEmpty(subjectName)){
            subjectNameTextInputLayout.setError(getResources().getString(R.string.subject_name_error_txt));
        }else if (TextUtils.isEmpty(subjectCode)){
            subjectCodeTextInputLayout.setError(getResources().getString(R.string.subject_code_error_txt));
        }else if (TextUtils.isEmpty(program) || program.equals("Select")) {
            programTextInputLayout.setError(getResources().getString(R.string.program_error_txt));
        } else if (TextUtils.isEmpty(semester) || semester.equals("Select")) {
            semTextInputLayout.setError(getResources().getString(R.string.sem_error_txt));
        }else {
            SubjectModel modelData = new SubjectModel(subjectName,program,subjectCode,semester);
            if (subjectModel!=null){
                //update
                updateSubject(modelData);

            }else {
                //create

                createSubject(modelData);
            }

        }
    }



    private void createSubject(SubjectModel modelData) {
        showProgressDialog("Adding subject please wait...");
        Call<ApiResponse> call = api.addSubject(modelData);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ApiResponse repose = response.body();
                        String msg = repose.getMessage();
                        boolean status = repose.isStatus();
                        if (status) {
                            if (msg.equals("Subject added successfully")) {
                                Toast.makeText(AddSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else {
                                Toast.makeText(AddSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddSubjectActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddSubjectActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSubject(SubjectModel modelData) {
        showProgressDialog("updating subject please wait...");
        Call<ApiResponse> call = api.updateSubject(modelData);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ApiResponse repose = response.body();
                        String msg = repose.getMessage();
                        boolean status = repose.isStatus();
                        if (status) {
                            if (msg.equals("Subject Updated successfully")) {
                                Toast.makeText(AddSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else {
                                Toast.makeText(AddSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddSubjectActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddSubjectActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddSubjectActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddSubjectActivity.this);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}