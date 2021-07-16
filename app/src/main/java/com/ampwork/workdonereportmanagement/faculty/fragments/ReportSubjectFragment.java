package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.AddDailyReportStepOneActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.AddReportSubjectActivity;
import com.ampwork.workdonereportmanagement.faculty.adapter.ReportSubjectAdapter;
import com.ampwork.workdonereportmanagement.model.DailyReportModel;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.model.ReportSubjectResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportSubjectFragment extends Fragment {

    String reportId, program, reportStatus;
    ProgressDialog progressDialog;

    Api api;
    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    MaterialCardView filterCardView;
    List<GenerateReportResponse.ReportSubject> reportSubjects = new ArrayList<>();
    ReportSubjectAdapter reportSubjectAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reportId = getArguments().getString("reportId");
        program = getArguments().getString("program");
        reportStatus = getArguments().getString("status");

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report_subject, container, false);

        initializeViews(root);
        initializeClickEvent();
        getReportSubject();
        return root;
    }



    private void initializeViews(View root) {
        api = ApiClient.getClient().create(Api.class);
        fabAdd = root.findViewById(R.id.fab_add_report);
        filterCardView = root.findViewById(R.id.cardView);

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        if (reportStatus.equals("Approved")) {
            fabAdd.setVisibility(View.GONE);
        } else if (reportStatus.equals("Accept")) {
            fabAdd.setVisibility(View.GONE);
        } else {
            fabAdd.setVisibility(View.VISIBLE);
        }
    }

    private void initializeClickEvent() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddReportSubjectActivity.class);
                intent.putExtra("reportId", reportId);
                intent.putExtra("program", program);
                startActivity(intent);
            }
        });

        filterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openPopUpToSelectFilter();
            }
        });
    }

    private void getReportSubject() {
        showProgressDialog("Please wait...");
        Call<ReportSubjectResponse> call = api.getReportSubject(reportId);
        call.enqueue(new Callback<ReportSubjectResponse>() {
            @Override
            public void onResponse(Call<ReportSubjectResponse> call, Response<ReportSubjectResponse> response) {
                int statusCode = response.code();
                switch (statusCode){
                    case 200:
                        hideProgressDialog();
                        ReportSubjectResponse apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        boolean status = apiResponse.isStatus();
                        if (status) {
                            if (msg.equals("Report Subject list")){
                                reportSubjects = apiResponse.getReportSubjects();
                                if (reportSubjects.size()>0){
                                    reportSubjectAdapter = new ReportSubjectAdapter(getActivity(),
                                            reportSubjects);
                                    recyclerView.setAdapter(reportSubjectAdapter);
                                }
                            }else {
                                Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Please try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ReportSubjectResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}
