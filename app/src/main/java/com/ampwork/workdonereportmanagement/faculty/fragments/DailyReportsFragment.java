package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.AddDailyReportStepOneActivity;
import com.ampwork.workdonereportmanagement.faculty.adapter.DailyReportChildAdapter;
import com.ampwork.workdonereportmanagement.faculty.adapter.DailyReportParentAdapter;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.DailyReportModel;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DailyReportsFragment extends Fragment {


    ProgressDialog progressDialog;

    RecyclerView recyclerView;
    DailyReportChildAdapter reportAdapter;

    Api api;
    String reportId, program, reportStatus;
    FloatingActionButton fabAdd;
    MaterialCardView filterCardView;
    String[] semArray = {"semester 1", "semester 2", "semester 3", "semester 4", "All"};
    String selectedSemester;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reportId = getArguments().getString("reportId");
        program = getArguments().getString("program");
        reportStatus = getArguments().getString("status");

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_daily_report, container, false);

        initializeViews(root);
        getDailyReports();
        initializeClickEvent();
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
                Intent intent = new Intent(getActivity(), AddDailyReportStepOneActivity.class);
                intent.putExtra("reportId", reportId);
                intent.putExtra("program", program);
                startActivity(intent);
            }
        });

        filterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpToSelectFilter();
            }
        });
    }

    private void getDailyReports() {
        showProgressDialog("Please wait...");
        Call<DailyReportModel> call = api.getDailyReports(reportId);
        call.enqueue(new Callback<DailyReportModel>() {
            @Override
            public void onResponse(Call<DailyReportModel> call, Response<DailyReportModel> response) {
                int statusCode = response.code();

                switch (statusCode) {
                    case 200:
                        DailyReportModel apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        boolean status = apiResponse.isStatus();
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Daily Reports")) {
                                List<DailyReportModel.ReportsModel> reportModels = apiResponse.getReportsModels();
                                List<AddReportModel> addReportModels = new ArrayList<>();

                                 for ( int i=0;i<reportModels.size();i++)
                                {
                                    for (AddReportModel addReportModel : reportModels.get(i).getAddReportModels()){
                                        addReportModels.add(addReportModel);
                                    }
                                }
                                DailyReportParentAdapter DailyReportParentAdapter = new DailyReportParentAdapter(getActivity(),
                                        reportModels,addReportModels,reportStatus);
                                recyclerView.setAdapter(DailyReportParentAdapter);
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
            public void onFailure(Call<DailyReportModel> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openPopUpToSelectFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Semester");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(semArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        selectedSemester = "1";
                        break;
                    case 1:
                        selectedSemester = "2";
                        break;
                    case 2:
                        selectedSemester = "3";
                        break;
                    case 3:
                        selectedSemester = "4";
                        break;
                    case 4:
                        selectedSemester = "All";
                        break;

                    default:
                        break;
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(selectedSemester)) {
                    reportAdapter.getFilter().filter(selectedSemester);
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();
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
