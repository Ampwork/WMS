package com.ampwork.workdonereportmanagement.hod.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.hod.adapters.ReportReceivedAdapter;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.model.SubmitReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HodHomeActivity extends AppCompatActivity implements ReportReceivedAdapter.RecycleItemViewClicked {

    TextView tvUserName, tvUserPhone, tvUserEmail;
    ProgressDialog progressDialog;
    CircleImageView profileImageView;
    TextDrawable drawable;
    ImageButton profileButton;
    RecyclerView recyclerView;

    PreferencesManager preferencesManager;
    Api api;
    String userId, reportId;
    boolean isFromNotification = false;
    ReportReceivedAdapter adapter;

    List<GenerateReportResponse.ReportResponseModel> reportResponseModels = new ArrayList<>();
    List<AddReportModel> addReportModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_home);

        initializeViews();
        setData();
        initializeRecyclerView();
        initializeClickEvent();
        getReports();
    }


    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);
        reportId = preferencesManager.getStringValue(AppConstants.PREF_HOD_NOTIFICATION_REPORT_ID);
        isFromNotification = preferencesManager.getBooleanValue(AppConstants.PREF_IS_FROM_NOTIFICATION);
        tvUserEmail = findViewById(R.id.tvEmailId);
        tvUserName = findViewById(R.id.tvName);
        tvUserPhone = findViewById(R.id.tvPhone);

        profileImageView = findViewById(R.id.imageView3);
        profileButton = findViewById(R.id.settings_btn);
    }


    private void setData() {
        String path = preferencesManager.getStringValue(AppConstants.PREF_PROFILE_URL);
        String fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        String lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);


        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        drawable = TextDrawable.builder().beginConfig()
                .width(150)
                .height(150)
                .bold()
                .endConfig()
                .buildRound(fName.substring(0, 1) + lName.substring(0, 1), color);
        if (!path.isEmpty()) {
            Picasso.with(this)
                    .load(path)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(profileImageView);

        } else {
            profileImageView.setImageDrawable(drawable);
        }
        tvUserName.setText(fName + " " + lName);
        tvUserPhone.setText(preferencesManager.getStringValue(AppConstants.PREF_PHONE));
        tvUserEmail.setText(preferencesManager.getStringValue(AppConstants.PREF_EMAIL));
    }


    private void initializeClickEvent() {
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HodHomeActivity.this, HodProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }


    private void getReports() {
        showProgressDialog("Please wait...");
        Call<SubmitReportResponse> call = api.getReportByCoordinatorId(userId);
        call.enqueue(new Callback<SubmitReportResponse>() {
            @Override
            public void onResponse(Call<SubmitReportResponse> call, Response<SubmitReportResponse> response) {
                int statusCode = response.code();
                SubmitReportResponse apiResponse = response.body();
                String msg = apiResponse.getMessage();
                boolean status = apiResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Record Found")) {

                                reportResponseModels = apiResponse.getReportResponseModels();
                                List<GenerateReportResponse.ReportResponseModel> reportResponseList = new ArrayList<>();
                                if (!reportResponseModels.isEmpty()) {
                                    if (isFromNotification && !TextUtils.isEmpty(reportId)) {
                                        for (int i = 0; i < reportResponseModels.size(); i++) {
                                            if (reportResponseModels.get(i).getId().equals(reportId)) {
                                                reportResponseList.add(reportResponseModels.get(i));
                                            }
                                        }
                                        adapter = new ReportReceivedAdapter(HodHomeActivity.this, reportResponseList, HodHomeActivity.this);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        adapter = new ReportReceivedAdapter(HodHomeActivity.this, reportResponseModels, HodHomeActivity.this);
                                        recyclerView.setAdapter(adapter);
                                    }

                                }

                            } else {
                                Toast.makeText(HodHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(HodHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        Toast.makeText(HodHomeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        break;
                }
            }

            @Override
            public void onFailure(Call<SubmitReportResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(HodHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(HodHomeActivity.this);
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


        addReportModels = reportModel.getAddReportModels();
        Intent intent = new Intent(HodHomeActivity.this, ShowDetailedReportActivity.class);
        // intent.putExtra("dataList",(Parcelable) addReportModels);
        intent.putExtra("data", (Parcelable) reportModel);
        startActivity(intent);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getReports();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferencesManager.setBooleanValue(AppConstants.PREF_IS_FROM_NOTIFICATION, false);
        preferencesManager.setStringValue(AppConstants.PREF_HOD_NOTIFICATION_REPORT_ID, "");
    }
}