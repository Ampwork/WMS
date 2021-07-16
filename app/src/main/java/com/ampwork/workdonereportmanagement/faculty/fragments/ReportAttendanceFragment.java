package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.AddReportAttendanceStepOneActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.EditReportAttendanceActivity;
import com.ampwork.workdonereportmanagement.faculty.adapter.AttendanceReportChildAdapter;
import com.ampwork.workdonereportmanagement.faculty.adapter.AttendanceReportParentAdapter;
import com.ampwork.workdonereportmanagement.model.AttendanceReportResponse;
import com.ampwork.workdonereportmanagement.model.ReportAttendanceModel;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAttendanceFragment extends Fragment implements AttendanceReportChildAdapter.RecycleItemViewClicked {

    ProgressDialog progressDialog;

    RecyclerView recyclerView;


    Api api;
    String reportId, program, reportStatus, userId, selectedSemester;
    FloatingActionButton fabAdd;
    PreferencesManager preferencesManager;
    MaterialCardView filterCardView;
    AttendanceReportChildAdapter attendanceReportChildAdapter;
    List<ReportAttendanceModel> reportAttendanceModels = new ArrayList<>();
    String[] semArray = {"semester 1", "semester 2", "semester 3", "semester 4", "All"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reportId = getArguments().getString("reportId");
        program = getArguments().getString("program");
        reportStatus = getArguments().getString("status");

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report_attendance, container, false);

        initializeViews(root);
        getAttendanceReports();
        initializeClickEvent();
        return root;
    }


    private void initializeViews(View root) {
        api = ApiClient.getClient().create(Api.class);
        preferencesManager = new PreferencesManager(getActivity());
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);
        fabAdd = root.findViewById(R.id.fab_add_att);
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
                Intent intent = new Intent(getActivity(), AddReportAttendanceStepOneActivity.class);
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

    private void getAttendanceReports() {
        showProgressDialog("Please wait...");
        Call<AttendanceReportResponse> call = api.getAttendanceReport(reportId, userId);
        call.enqueue(new Callback<AttendanceReportResponse>() {
            @Override
            public void onResponse(Call<AttendanceReportResponse> call, Response<AttendanceReportResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        AttendanceReportResponse apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        boolean status = apiResponse.isStatus();
                        if (status) {
                            if (msg.equals("Records Found")) {
                                List<AttendanceReportResponse.Reports> reportModels = apiResponse.getReportsList();
                                List<ReportAttendanceModel> reportAttendanceModels = new ArrayList<>();

                                for ( int i=0;i<reportModels.size();i++)
                                {
                                    for (ReportAttendanceModel model : reportModels.get(i).getReportAttendanceModels()){
                                        reportAttendanceModels.add(model);
                                    }
                                }
                                AttendanceReportParentAdapter adapter = new AttendanceReportParentAdapter(getActivity(),
                                        reportModels,reportAttendanceModels,reportStatus);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<AttendanceReportResponse> call, Throwable t) {
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
                    attendanceReportChildAdapter.getFilter().filter(selectedSemester);
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

    @Override
    public void onResume() {
        super.onResume();
        getAttendanceReports();
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
    public void onItemViewSelected(ReportAttendanceModel addReportModel) {

    }
}
