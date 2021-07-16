package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.FacultyReportAdapter;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyReportScreenOneActivity extends AppCompatActivity implements FacultyReportAdapter.RecycleItemViewClicked {

    TextView tvViewReport, tvTitle;
    RecyclerView recyclerView;

    PreferencesManager preferenceManager;
    ProgressDialog progressDialog;
    Api api;
    FacultyReportAdapter reportAdapter;
    String userId, semester, subject, fromDate, toDate, month;
    boolean isMonth = false;
    List<GenerateReportResponse.ReportResponseModel> reportResponseModelList;
    List<GenerateReportResponse.WeeksModel> weeksModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_report_screen_one);

        initializeViews();
        initializeToolBar();
        initializeRecyclerView();
    }

    private void initializeViews() {
        preferenceManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);
        tvViewReport = findViewById(R.id.tvReportExcel);


        userId = preferenceManager.getStringValue(AppConstants.PREF_ID);
        isMonth = getIntent().getExtras().getBoolean("isMonth");
        if (isMonth) {
            semester = getIntent().getExtras().getString("semester");
            subject = getIntent().getExtras().getString("subject");
            month = getIntent().getExtras().getString("month");
        } else {
            semester = getIntent().getExtras().getString("semester");
            subject = getIntent().getExtras().getString("subject");
            fromDate = getIntent().getExtras().getString("fromDate");
            toDate = getIntent().getExtras().getString("toDate");
        }

    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Reports");
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        if (isMonth) {
            // get monthly report
            getMonthlyReport();

        } else {
            //get custom report
            getReport();
        }
    }


    private void getReport() {
        showProgressDialog("Getting reports please wait...");
        GenerateReportResponse request = new GenerateReportResponse(userId, semester, subject, fromDate, toDate);
        Call<GenerateReportResponse> call = api.getFacultyReport(request);
        call.enqueue(new Callback<GenerateReportResponse>() {
            @Override
            public void onResponse(Call<GenerateReportResponse> call, Response<GenerateReportResponse> response) {
                int statusCode = response.code();
                GenerateReportResponse reportResponse = response.body();
                String msg = reportResponse.getMessage();
                boolean status = reportResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Report Found")) {
                                reportResponseModelList = reportResponse.getReportResponseModelList();
                                reportAdapter = new FacultyReportAdapter(FacultyReportScreenOneActivity.this,
                                        reportResponseModelList, FacultyReportScreenOneActivity.this);
                                recyclerView.setAdapter(reportAdapter);

                            }
                        } else {
                            Toast.makeText(FacultyReportScreenOneActivity.this, "No Report Found", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(FacultyReportScreenOneActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<GenerateReportResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FacultyReportScreenOneActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getMonthlyReport() {
        showProgressDialog("Getting reports please wait...");
        GenerateReportResponse request = new GenerateReportResponse(userId, semester, subject, month);
        Call<GenerateReportResponse> call = api.generateMonthlyReport(request);
        call.enqueue(new Callback<GenerateReportResponse>() {
            @Override
            public void onResponse(Call<GenerateReportResponse> call, Response<GenerateReportResponse> response) {
                int statusCode = response.code();

                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        GenerateReportResponse reportResponse = response.body();
                        String msg = reportResponse.getMessage();
                        boolean status = reportResponse.isStatus();
                        if (status) {
                            if (msg.equals("Report Found")) {
                                reportResponseModelList = reportResponse.getReportResponseModelList();
                                reportAdapter = new FacultyReportAdapter(FacultyReportScreenOneActivity.this,
                                        reportResponseModelList, FacultyReportScreenOneActivity.this);
                                recyclerView.setAdapter(reportAdapter);
                                Log.e("weeks","...."+reportResponse.getWeeksModel());
                                weeksModels = reportResponse.getWeeksModel();
                                /*for (int i=0;i<weeksModels.size();i++)
                                {
                                    Log.e("week","........"+weeksModels.get(i).getWeeknumber());
                                    Iterator<JsonElement> iterator = weeksModels.get(i).getJsonObject().iterator();
                                    while(iterator.hasNext()) {
                                        System.out.println(iterator.next());
                                    }


                                }*/
                            }
                        } else {
                            Toast.makeText(FacultyReportScreenOneActivity.this, "No Report Found", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(FacultyReportScreenOneActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<GenerateReportResponse> call, Throwable t) {
                hideProgressDialog();
                Log.e("week","....."+t.getMessage());
                Toast.makeText(FacultyReportScreenOneActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(FacultyReportScreenOneActivity.this);
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
    public void onItemViewSelected(GenerateReportResponse.ReportResponseModel reportModel) {


        Intent intent = new Intent(FacultyReportScreenOneActivity.this, FacultyReportScreenTwoActivity.class);
        intent.putExtra("isMonth", isMonth);
        intent.putExtra("semester", semester);
        intent.putExtra("subject", subject);
        intent.putExtra("month", month);
        intent.putExtra("list", (Parcelable) reportModel);
        intent.putExtra("weeks", (Serializable) weeksModels);
        startActivity(intent);
    }
}