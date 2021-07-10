package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.AddNewReportActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.DailyReportsActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.ReportsMainActivity;
import com.ampwork.workdonereportmanagement.faculty.adapter.ReportAdapter;
import com.ampwork.workdonereportmanagement.model.ReportModel;
import com.ampwork.workdonereportmanagement.model.ReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportFragment extends Fragment implements ReportAdapter.RecycleItemViewClicked {

    BottomNavigationView bottomNavigationView;
    TextView tvTitle;
    ProgressDialog progressDialog;
    ReportAdapter reportAdapter;
    RecyclerView recyclerView;
    List<ReportModel> reportModels = new ArrayList<>();
    Api api;
    String userId;
    PreferencesManager preferencesManager;
    FloatingActionButton fabAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_report, container, false);


        tvTitle = root.findViewById(R.id.tvtitle);
        Toolbar toolbar = root.findViewById(R.id.toolbarcom);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Added Reports");

        initializeViews(root);
        initializeRecyclerView();
        getReportList();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewReportActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }


    private void initializeViews(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        fabAdd = root.findViewById(R.id.fab_add);
        preferencesManager = new PreferencesManager(getActivity());
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);
    }

    private void initializeRecyclerView() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

    }


    private void getReportList() {
        showProgressDialog("Please wait...");
        api = ApiClient.getClient().create(Api.class);
        Call<ReportResponse> call = api.getReports(userId);
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
                            if (msg.equals("Added Report")) {
                                reportModels = apiResponse.getReportModels();
                                reportAdapter = new ReportAdapter(getActivity(), reportModels, AddReportFragment.this);
                                recyclerView.setAdapter(reportAdapter);
                            }
                        } else {
                            if (msg.equals("Reports not found")) {
                                Toast.makeText(getActivity(), "Reports not found", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Please try after sometime...", Toast.LENGTH_SHORT).show();
                            }

                        }
                        break;
                    case 500:
                        Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            AppUtility.changeStatusBarColor(getActivity());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.navigation_addreport).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getReportList();
        bottomNavigationView.getMenu().findItem(R.id.navigation_addreport).setChecked(true);
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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
    public void onItemViewSelected(ReportModel reportModel) {
        Intent intent = new Intent(getActivity(), ReportsMainActivity.class);
        intent.putExtra("reportId", reportModel.getReportId());
        intent.putExtra("program", reportModel.getProgram());
        intent.putExtra("status", reportModel.getStatus());
        startActivity(intent);
    }
}
