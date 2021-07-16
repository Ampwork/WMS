package com.ampwork.workdonereportmanagement.clerk.activities.programs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.AddReportSubjectActivity;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProgramActivity extends AppCompatActivity {

    TextInputEditText edProgram;
    TextInputLayout programInputLayout;
    MaterialButton btnCreate;
    ProgressDialog progressDialog;
    TextView tvTitle;
    Api api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);

        initializeViews();
        initializeClickEvent();
        initializeToolBar();
        //change status bar color
        AppUtility.changeStatusBarColor(this);

    }


    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        edProgram = findViewById(R.id.programEdt);
        programInputLayout = findViewById(R.id.programInputLayout);
        btnCreate = findViewById(R.id.createBtn);


        edProgram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    programInputLayout.setError(null);
                }

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
        tvTitle.setText("Create Program");
    }

    private void initializeClickEvent() {

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String program = edProgram.getText().toString();
                if (TextUtils.isDigitsOnly(program)) {
                    programInputLayout.setError(getResources().getString(R.string.prgm_error_txt));
                } else {

                    CreateProgram(program);
                }
            }
        });

    }

    private void CreateProgram(String programName) {
        showProgressDialog("Adding program please wait...");
        Call<ApiResponse> call = api.addProgram(programName);
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
                            if (msg.equals("Program added successfully")){
                                Toast.makeText(AddProgramActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else {
                                onBackPressed();
                                Toast.makeText(AddProgramActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            onBackPressed();
                            Toast.makeText(AddProgramActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddProgramActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddProgramActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddProgramActivity.this);
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