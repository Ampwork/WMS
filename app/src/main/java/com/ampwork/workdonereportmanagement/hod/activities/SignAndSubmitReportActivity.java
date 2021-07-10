package com.ampwork.workdonereportmanagement.hod.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.ampwork.workdonereportmanagement.model.ReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignAndSubmitReportActivity extends AppCompatActivity {

    TextView tvTitle;
    TextInputLayout statusInputLayout, remarksInputLayout;
    TextInputEditText edRemarks;
    AutoCompleteTextView autoTvStatus;
    MaterialButton btnSubmit;
    ProgressDialog progressDialog;

    PreferencesManager preferencesManager;
    Api api;
    String reportId, userId, program, month, fullName, coStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_and_submit_report);

        initializeViews();
        initializeToolBar();
        initializeClickEvent();
        initializeTextWatcher();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }


    private void initializeViews() {
        reportId = getIntent().getExtras().getString("reportId");
        program = getIntent().getExtras().getString("program");
        fullName = getIntent().getExtras().getString("name");
        month = getIntent().getExtras().getString("month");
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);

        statusInputLayout = findViewById(R.id.statusInputLayout);
        autoTvStatus = findViewById(R.id.statusAutoComTv);
        remarksInputLayout = findViewById(R.id.remarkInputLayout);
        edRemarks = findViewById(R.id.remarkEd);
        btnSubmit = findViewById(R.id.submitBtn);

        String[] status = {"Approved", "Review"};
        ArrayAdapter status_adapter = new ArrayAdapter(SignAndSubmitReportActivity.this, R.layout.list_item, status);
        autoTvStatus.setAdapter(status_adapter);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Submit Report");
    }

    private void initializeTextWatcher() {
        autoTvStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    statusInputLayout.setError(null);
                }

            }
        });
        edRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    remarksInputLayout.setError(null);
                }

            }
        });
    }

    private void initializeClickEvent() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coStatus = autoTvStatus.getText().toString();
                String remarks = edRemarks.getText().toString();
                if (TextUtils.isEmpty(coStatus) || coStatus.equals("Select")) {
                    statusInputLayout.setError(getResources().getString(R.string.status_error_txt));
                } else {
                    ReportResponse reportResponse = new ReportResponse(userId, reportId, coStatus, remarks);
                    SendReportData(reportResponse);
                }
            }
        });
    }

    private void SendReportData(ReportResponse reportResponse) {
        showProgressDialog("Submitting your report...");
        Call<ApiResponse> call = api.coordinatorSubmit(reportResponse);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        ApiResponse apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        String supDeviceId = apiResponse.getSup_device();
                        String facultyId = apiResponse.getUser_device();

                        boolean status = apiResponse.isStatus();
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Report Submitted Successfully")) {
                                Toast.makeText(SignAndSubmitReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                                if (!TextUtils.isEmpty(supDeviceId)) {
                                    pushNotificationToSup(supDeviceId, coStatus);
                                }
                                if (!TextUtils.isEmpty(facultyId)) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            pushNotificationToFaculty(facultyId, coStatus);
                                        }
                                    }, 500);

                                }
                                NavigateActivity();

                            } else {
                                Toast.makeText(SignAndSubmitReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignAndSubmitReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(SignAndSubmitReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SignAndSubmitReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void NavigateActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignAndSubmitReportActivity.this, HodHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void pushNotificationToSup(String token, String coStatus) {

        String fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        String lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);
        String coName = fName + " " + lName;
        String message = fullName + "\nSubmitted the report of month " + month + "\n" +
                "Program :" + program + "\n" +
                "Submitted By :" + coName + "\n" +
                "Status : " + coStatus;

        String title = "New Report Received";
        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            data.put("title", title);
            data.put("message", message);
            data.put("reportId", reportId);

            json.put("to", token);
            json.put("data", data);

            new PushNotificationToAllTask().execute(String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void pushNotificationToFaculty(String facultyId, String coStatus) {
        String fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        String lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);
        String coName = fName + " " + lName;

        String message = "Hi, " + fullName + "\n" + "your report is submitted by " + coName + "\n" +
                "Status :" + coStatus;
        String title = "Report Status";
        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            data.put("title", title.trim());
            data.put("message", message);
            data.put("reportId", reportId);
            //data.put("notifyto", "wms");

            json.put("to", facultyId);
            json.put("data", data);

            new PushNotificationToAllTask().execute(String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PushNotificationToAllTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            OkHttpClient okHttpClient = new OkHttpClient();
            String jsonData = strings[0];
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonData); // new

            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .addHeader("Authorization", "key=AAAA0LqK75g:APA91bF6MJVo7nTC3YcVc2u5imz-cT8qSyEe3MaCOBVRel_fJye_RMFurvqzfqPKTe-KTK70aAzKfrUK1P5FBvyzsC-NoVZt0U0JnbyhbZs3s4yt5IdtDSP-PDPYvQvxh286UEFPzDMs")
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            okhttp3.Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("notification", "..........." + response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SignAndSubmitReportActivity.this);
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