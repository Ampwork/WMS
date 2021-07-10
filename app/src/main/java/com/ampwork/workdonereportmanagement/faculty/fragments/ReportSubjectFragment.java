package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.AddDailyReportStepOneActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.AddReportSubjectActivity;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReportSubjectFragment extends Fragment {

    String reportId, program, reportStatus;
    ProgressDialog progressDialog;

    Api api;
    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    MaterialCardView filterCardView;
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
}
