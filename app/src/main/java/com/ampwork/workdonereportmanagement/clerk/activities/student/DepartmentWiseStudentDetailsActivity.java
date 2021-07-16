package com.ampwork.workdonereportmanagement.clerk.activities.student;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.adapter.StudentDetailsAdapter;
import com.ampwork.workdonereportmanagement.model.StudentDetailsModel;
import com.ampwork.workdonereportmanagement.model.StudentResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentWiseStudentDetailsActivity extends AppCompatActivity implements StudentDetailsAdapter.RecycleItemViewClicked {

    TextView tvTitle;
    String program;
    MaterialCardView filterCardView;
    RecyclerView recyclerView;
    String[] semArray = {"semester 1", "semester 2", "semester 3", "semester 4","All"};
    String selectedSemester;
    Api api;
    ProgressDialog progressDialog;
    StudentDetailsAdapter adapter;
    List<StudentDetailsModel> studentDetailsModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_wise_student_details);

        initializeViews();
        initializeToolBar();
        initializeRecyclerView();
        initializeClickEvent();
        getStudentsDetails();
    }


    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        program = getIntent().getExtras().getString("program");
        filterCardView = findViewById(R.id.cardView);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Student Details");
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }

    private void initializeClickEvent() {
        filterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpToSelectFilter();
            }
        });
    }

    private void openPopUpToSelectFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DepartmentWiseStudentDetailsActivity.this);
        builder.setTitle("Select Semester");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(semArray, 0, new DialogInterface.OnClickListener() {
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
                        selectedSemester="All";
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(selectedSemester)){
                    adapter.getFilter().filter(selectedSemester);
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


    private void getStudentsDetails() {
        showProgressDialog("Loading student details...");
        Call<StudentResponse> call = api.getStudentsByProgram(program);
        call.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(Call<StudentResponse> call, Response<StudentResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        StudentResponse studentResponse = response.body();
                        String msg = studentResponse.getMessage();
                        boolean status = studentResponse.isStatus();
                        if (status) {
                            studentDetailsModels = studentResponse.getStudentDetailsModels();
                            if (msg.equals("Student data")) {
                                adapter = new StudentDetailsAdapter(DepartmentWiseStudentDetailsActivity.this,
                                        studentDetailsModels, DepartmentWiseStudentDetailsActivity.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(DepartmentWiseStudentDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DepartmentWiseStudentDetailsActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(DepartmentWiseStudentDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<StudentResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(DepartmentWiseStudentDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(DepartmentWiseStudentDetailsActivity.this);
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
    public void onItemViewSelected(StudentDetailsModel detailsModel) {
        Intent intent = new Intent(DepartmentWiseStudentDetailsActivity.this, AddStudentActivity.class);
        intent.putExtra("studentData", (Parcelable) detailsModel);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentsDetails();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}