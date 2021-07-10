package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.ProgramRepose;
import com.ampwork.workdonereportmanagement.model.ReportModel;
import com.ampwork.workdonereportmanagement.model.ReportResponse;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.model.UsersResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
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

public class AddNewReportActivity extends AppCompatActivity {

    TextView tvTitle, tvDate;
    TextInputLayout monthInputLayout, yearInputLayout, leaveInputLayout, programInputLayout, coordinatorInputLayout, superintendentInputLayout;
    AutoCompleteTextView autoTvMonth, autoTvYear, autoTvProgram, autoTvCoordinator, autoTvSup;
    TextInputEditText edLeave;
    MaterialButton btnCreate;
    Api api;
    PreferencesManager preferencesManager;
    String userId, userFullName, fName, lName, co_id, sup_id;
    ProgressDialog progressDialog;
    List<ProgramRepose.ProgramModel> programModels = new ArrayList<>();
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
    String[] years = {"2021", "2022", "2023", "2024", "2025"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_report);

        initializeViews();
        initializeToolBar();
        getProgramList();
        getCoordinator();
        getSuperintendent();
        loadMonthsAndYear();
        initializeTextWatcher();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);

    }


    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);
        fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);
        userFullName = fName + " " + lName;
        api = ApiClient.getClient().create(Api.class);
        tvDate = findViewById(R.id.tvDate);
        monthInputLayout = findViewById(R.id.monthInputLayout);
        autoTvMonth = findViewById(R.id.monthAutoComTv);
        yearInputLayout = findViewById(R.id.yearInputLayout);
        autoTvYear = findViewById(R.id.yearAutoComTv);
        leaveInputLayout = findViewById(R.id.leaveInputLayout);
        edLeave = findViewById(R.id.leaveEd);
        programInputLayout = findViewById(R.id.programInputLayout);
        autoTvProgram = findViewById(R.id.programAutoComTv);
        coordinatorInputLayout = findViewById(R.id.coordinatorInputLayout);
        autoTvCoordinator = findViewById(R.id.coordinatorAutoComTv);
        superintendentInputLayout = findViewById(R.id.supInputLayout);
        autoTvSup = findViewById(R.id.supAutoComTv);

        btnCreate = findViewById(R.id.createBtn);

        String dateTime = AppUtility.getCurrentDateTime();
        tvDate.setText(dateTime);
    }

    private void initializeToolBar() {
        Toolbar toolbar = findViewById(R.id.settingbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeTextWatcher() {
        edLeave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    leaveInputLayout.setError(null);
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
                    programInputLayout.setError(null);
                }

            }
        });
        autoTvYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    yearInputLayout.setError(null);
                }

            }
        });
        autoTvMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    monthInputLayout.setError(null);
                }

            }
        });
        autoTvSup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    superintendentInputLayout.setError(null);
                }

            }
        });
        autoTvCoordinator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    coordinatorInputLayout.setError(null);
                }

            }
        });
    }

    private void initializeClickEvents() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
        autoTvCoordinator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo model = (UserInfo) parent.getItemAtPosition(position);
                co_id = model.getUserId();
            }
        });
        autoTvSup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo model = (UserInfo) parent.getItemAtPosition(position);
                sup_id = model.getUserId();
            }
        });
    }


    private void getProgramList() {
        showProgressDialog("Please wait...");
        Call<ProgramRepose> call = api.getPrograms();
        call.enqueue(new Callback<ProgramRepose>() {
            @Override
            public void onResponse(Call<ProgramRepose> call, Response<ProgramRepose> response) {
                int statusCode = response.code();
                ProgramRepose repose = response.body();
                String msg = repose.getMessage();
                boolean status = repose.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Programs list")) {
                                assert response.body() != null;
                                programModels = response.body().getProgramModels();
                                if (programModels.size() > 0) {
                                    loadPrograms(programModels);
                                }

                            }
                        } else {
                            Toast.makeText(AddNewReportActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddNewReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramRepose> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddNewReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getSuperintendent() {
        showProgressDialog("Please wait...");
        Call<UsersResponse> call = api.getSuperintendent();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                int statusCode = response.code();
                UsersResponse repose = response.body();
                String msg = repose.getMessage();
                boolean status = repose.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Success")) {
                                assert response.body() != null;
                                List<UserInfo> userInfos = response.body().getUserInfos();
                                if (userInfos.size() > 0) {
                                    loadSup(userInfos);
                                }

                            }
                        } else {
                            Toast.makeText(AddNewReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddNewReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddNewReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getCoordinator() {
        showProgressDialog("Please wait...");
        Call<UsersResponse> call = api.getCoordinators();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                int statusCode = response.code();
                UsersResponse repose = response.body();
                String msg = repose.getMessage();
                boolean status = repose.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Success")) {
                                assert response.body() != null;
                                List<UserInfo> userInfos = response.body().getUserInfos();
                                if (userInfos.size() > 0) {
                                    loadCoordinators(userInfos);
                                }

                            }
                        } else {
                            Toast.makeText(AddNewReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddNewReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddNewReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadPrograms(List<ProgramRepose.ProgramModel> programModels) {
        String[] subjects = new String[programModels.size()];
        int index = 0;
        for (ProgramRepose.ProgramModel value : programModels) {
            subjects[index] = (String) value.getProgramName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(AddNewReportActivity.this, R.layout.list_item, subjects);
        autoTvProgram.setAdapter(sub_adapter);
    }


    private void loadMonthsAndYear() {
        ArrayAdapter month_adapter = new ArrayAdapter(AddNewReportActivity.this, R.layout.list_item, months);
        autoTvMonth.setAdapter(month_adapter);

        String year = AppUtility.getCurrentYear();
        autoTvYear.setText(year);
    }

    private void loadSup(List<UserInfo> userInfos) {
        /*String[] sup = new String[userInfos.size()];
        int index = 0;
        for (UserInfo value : userInfos) {
            sup[index] = (String) value.getFirstName()+" "+(String) value.getLastName();
            index++;
        }*/

        ArrayAdapter<UserInfo> sup_adapter = new ArrayAdapter<UserInfo>(AddNewReportActivity.this, R.layout.list_item, userInfos);
        autoTvSup.setAdapter(sup_adapter);

    }

    private void loadCoordinators(List<UserInfo> userInfos) {
       /* String[] coordinators = new String[userInfos.size()];
        int index = 0;
        for (UserInfo value : userInfos) {
            coordinators[index] = (String) value.getFirstName()+" "+(String) value.getLastName();
            index++;
        }*/

        ArrayAdapter<UserInfo> sup_adapter = new ArrayAdapter<UserInfo>(AddNewReportActivity.this, R.layout.list_item, userInfos);
        autoTvCoordinator.setAdapter(sup_adapter);
    }

    private void validateFields() {
        String year = autoTvYear.getText().toString();
        String program = autoTvProgram.getText().toString();
        String month = autoTvMonth.getText().toString();
        String leaves = edLeave.getText().toString();
        String coordinator = autoTvCoordinator.getText().toString();
        String sup = autoTvSup.getText().toString();


        if (TextUtils.isEmpty(month) || month.equals("Select")) {
            monthInputLayout.setError(getResources().getString(R.string.month_error_txt));
        } else if (TextUtils.isEmpty(year) || year.equals("Select")) {
            yearInputLayout.setError(getResources().getString(R.string.year_error_txt));
        } else if (TextUtils.isEmpty(program) || program.equals("Select")) {
            programInputLayout.setError(getResources().getString(R.string.program_error_txt));
        } else if (TextUtils.isEmpty(coordinator) || coordinator.equals("Select")) {
            coordinatorInputLayout.setError(getResources().getString(R.string.coordinator_error_txt));
        } else if (TextUtils.isEmpty(sup) || sup.equals("Select")) {
            superintendentInputLayout.setError(getResources().getString(R.string.sup_error_txt));
        } else {
            if (TextUtils.isEmpty(leaves)) {
                leaves = "0";
            }
            ReportModel reportModel = new ReportModel(userId, userFullName, month, year, program, leaves, co_id, sup_id);
            createReport(reportModel);
        }
    }

    private void createReport(ReportModel reportModel) {
        showProgressDialog("Creating report...");
        Call<ReportResponse> call = api.createReport(reportModel);
        call.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                int statusCode = response.code();
                ReportResponse apiResponse = response.body();
                String msg = apiResponse.getMessage();
                boolean status = apiResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Report Already added")) {
                                Toast.makeText(AddNewReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (msg.equals("Report Added Successfully")) {
                                onBackPressed();
                                Toast.makeText(AddNewReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AddNewReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        Toast.makeText(AddNewReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddNewReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddNewReportActivity.this);
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