package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.DailyReportAdapter;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.DailyReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyReportsActivity extends AppCompatActivity implements DailyReportAdapter.RecycleItemViewClicked {

    TextView tvTitle;
    ProgressDialog progressDialog;

    RecyclerView recyclerView;
    DailyReportAdapter reportAdapter;
    List<AddReportModel> reportModels = new ArrayList<>();
    Api api;
    String reportId, program, reportStatus;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reports);


        initializeViews();
        initializeRecyclerView();
        getDailyReports();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReportsActivity.this, AddDailyReportStepOneActivity.class);
                intent.putExtra("reportId", reportId);
                intent.putExtra("program", program);
                startActivity(intent);
            }
        });

        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }


    private void initializeViews() {
        reportId = getIntent().getExtras().getString("reportId");
        program = getIntent().getExtras().getString("program");
        reportStatus = getIntent().getExtras().getString("status");
        api = ApiClient.getClient().create(Api.class);
        fabAdd = findViewById(R.id.fab_add_report);
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Daily Reports");

        if (reportStatus.equals("Approved")) {
            fabAdd.setVisibility(View.GONE);
        } else if (reportStatus.equals("Accept")) {
            fabAdd.setVisibility(View.GONE);
        } else {
            fabAdd.setVisibility(View.VISIBLE);
        }
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }


    private void getDailyReports() {
        showProgressDialog("Please wait...");
        Call<DailyReportResponse> call = api.getDailyReports(reportId);
        call.enqueue(new Callback<DailyReportResponse>() {
            @Override
            public void onResponse(Call<DailyReportResponse> call, Response<DailyReportResponse> response) {
                int statusCode = response.code();
                DailyReportResponse apiResponse = response.body();
                String msg = apiResponse.getMessage();
                boolean status = apiResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Daily Reports")) {
                                reportModels = apiResponse.getAddReportModels();
                                reportAdapter = new DailyReportAdapter(DailyReportsActivity.this,
                                        reportModels, DailyReportsActivity.this, reportStatus);
                                recyclerView.setAdapter(reportAdapter);
                            }
                        } else {
                            if (msg.equals("Reports not found")) {
                                Toast.makeText(DailyReportsActivity.this, "Reports not found", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DailyReportsActivity.this, "Please try after sometime...", Toast.LENGTH_SHORT).show();
                            }

                        }
                        break;
                    case 500:
                        Toast.makeText(DailyReportsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        break;
                }

            }

            @Override
            public void onFailure(Call<DailyReportResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(DailyReportsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by subject");
        EditText searchedittext = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchedittext.setTextColor(Color.WHITE);
        searchedittext.setHintTextColor(Color.parseColor("#50F3F9FE"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    reportAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                }
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tvTitle.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tvTitle.setVisibility(View.VISIBLE);
                return true;
            }
        });
        return true;
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(DailyReportsActivity.this);
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
    protected void onResume() {
        super.onResume();
        getDailyReports();
    }

    @Override
    public void onItemViewSelected(AddReportModel reportModel) {
        Intent editIntent = new Intent(DailyReportsActivity.this, EditDailyReportActivity.class);
        editIntent.putExtra("data", (Parcelable) reportModel);
        startActivity(editIntent);
    }
}