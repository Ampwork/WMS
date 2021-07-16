package com.ampwork.workdonereportmanagement.clerk.activities.programs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.adapter.ProgramsAdapter;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramsHomeActivity extends AppCompatActivity implements ProgramsAdapter.RecycleItemViewClicked {

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    ProgressDialog progressDialog;
    Api api;
    TextView tvTitle;
    List<ProgramResponse.ProgramModel> programModels = new ArrayList<>();
    ProgramsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs_home);

        initializeViews();
        initializeRecyclerView();
        getPrograms();
        initializeToolBar();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getPrograms();
    }

    private void initializeViews() {
        fabAdd = findViewById(R.id.fab_add);
        api = ApiClient.getClient().create(Api.class);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgramsHomeActivity.this, AddProgramActivity.class);
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

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Programs");
    }

    private void getPrograms() {
        showProgressDialog("Please wait...");
        Call<ProgramResponse> call = api.getPrograms();
        call.enqueue(new Callback<ProgramResponse>() {
            @Override
            public void onResponse(Call<ProgramResponse> call, Response<ProgramResponse> response) {
                int statusCode = response.code();
                ProgramResponse repose = response.body();
                String msg = repose.getMessage();
                boolean status = repose.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Programs list")) {
                                assert response.body() != null;
                                programModels = response.body().getProgramModels();
                                if (programModels.size() > 0) {
                                    adapter = new ProgramsAdapter(ProgramsHomeActivity.this,
                                            programModels, ProgramsHomeActivity.this);
                                    recyclerView.setAdapter(adapter);
                                }

                            }
                        } else {
                            Toast.makeText(ProgramsHomeActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(ProgramsHomeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ProgramsHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemViewSelected(ProgramResponse.ProgramModel model) {
        AlertUser(model);
    }

    private void AlertUser(ProgramResponse.ProgramModel model) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProgramsHomeActivity.this);
        builder.setTitle("Alert.!");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        deleteProgramName(model.getProgramId());
                    }
                });

        // Setting Negative "NO" Btn
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProgramName(String programId) {
        showProgressDialog("Deleting program please wait...");
        Call<ApiResponse> call = api.deleteProgram(programId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ApiResponse apiResponse = response.body();
                        String msg = apiResponse.getMessage();
                        boolean status = apiResponse.isStatus();
                        if (status) {
                            if (msg.equals("Record Deleted")) {
                                Toast.makeText(ProgramsHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                getPrograms();
                            } else {

                                Toast.makeText(ProgramsHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(ProgramsHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(ProgramsHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ProgramsHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ProgramsHomeActivity.this);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}