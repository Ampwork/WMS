package com.ampwork.workdonereportmanagement.clerk.activities.faculty;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.activities.programs.ProgramsHomeActivity;
import com.ampwork.workdonereportmanagement.clerk.adapter.FacultyListAdapter;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.model.UsersResponse;
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

public class FacultyDetailsActivity extends AppCompatActivity implements FacultyListAdapter.RecycleItemViewClicked {

    TextView tvTitle;
    RecyclerView recyclerView;
    Api api;
    ProgressDialog progressDialog;
    FloatingActionButton fabAddFaculty;
    List<UserInfo> userInfoList = new ArrayList<>();
    FacultyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);

        initializeViews();
        initializeToolBar();
        initializeRecyclerView();
        initializeClickEvents();

        getFacultyList();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }


    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        fabAddFaculty = findViewById(R.id.fab_add_faculty);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Faculty Details");
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }

    private void initializeClickEvents() {
        fabAddFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDetailsActivity.this, AddFacultyActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getFacultyList() {
        showProgressDialog("Please wait...");
        Call<UsersResponse> call = api.getFaculty();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        UsersResponse usersResponse = response.body();
                        String msg = usersResponse.getMessage();
                        boolean status = usersResponse.isStatus();
                        if (status) {
                            if (msg.equals("Users list")) {
                                userInfoList = usersResponse.getUserInfos();
                                if (userInfoList.size() > 0) {
                                    adapter = new FacultyListAdapter(FacultyDetailsActivity.this,
                                            userInfoList, FacultyDetailsActivity.this);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    Toast.makeText(FacultyDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(FacultyDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(FacultyDetailsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FacultyDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemViewSelected(UserInfo model) {
        AlertUser(model);
    }

    private void AlertUser(UserInfo model) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(FacultyDetailsActivity.this);
        builder.setTitle("Alert.!");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        deleteFaculty(model.getUserId());
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

    private void deleteFaculty(String userId) {
        showProgressDialog("Deleting faculty details please wait...");
        Call<ApiResponse> call = api.deleteFaculty(userId);
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
                                Toast.makeText(FacultyDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                getFacultyList();
                            } else {

                                Toast.makeText(FacultyDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(FacultyDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(FacultyDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FacultyDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        searchView.setQueryHint("Search by name/role or program");
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
                    adapter.getFilter().filter(newText);
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
            progressDialog = new ProgressDialog(FacultyDetailsActivity.this);
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