package com.ampwork.workdonereportmanagement.clerk.activities.faculty;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
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
import com.ampwork.workdonereportmanagement.clerk.adapter.SubjectListAdapter;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.model.SubjectResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectsHomeActivity extends AppCompatActivity implements SubjectListAdapter.RecycleItemViewClicked {

    TextView tvTitle;
    RecyclerView recyclerView;
    MaterialCardView filterCardView;
    Api api;
    ProgressDialog progressDialog;
    FloatingActionButton fabAddSubject;
    SubjectListAdapter adapter;
    List<SubjectModel> subjectModelList = new ArrayList<>();
    List<ProgramResponse.ProgramModel> programModels = new ArrayList<>();
    String[] subjects;
    String selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_home);

        initializeViews();
        initializeRecyclerView();
        initializeToolBar();
        getSubjects();
        getProgramList();
        initializeClickEvent();

        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubjects();
    }

    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        filterCardView = findViewById(R.id.cardView);
        fabAddSubject = findViewById(R.id.fab_add_subject);


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
        tvTitle.setText("Subjects");
    }

    private void initializeClickEvent() {
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectsHomeActivity.this, AddSubjectActivity.class);
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


    private void getSubjects() {
        showProgressDialog("Please wait...");
        Call<SubjectResponse> call = api.getSubject();
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                int statusCode = response.code();
                SubjectResponse subjectResponse = response.body();
                String msg = subjectResponse.getMessage();
                boolean status = subjectResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Subjects list")) {
                                subjectModelList = subjectResponse.getSubjectModel();
                                if (subjectModelList.size() > 0) {
                                    adapter = new SubjectListAdapter(SubjectsHomeActivity.this,
                                            subjectModelList, SubjectsHomeActivity.this);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    Toast.makeText(SubjectsHomeActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(SubjectsHomeActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(SubjectsHomeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SubjectsHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProgramList() {
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
                                    loadPrograms(programModels);
                                }

                            }
                        } else {
                            Toast.makeText(SubjectsHomeActivity.this, "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(SubjectsHomeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SubjectsHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPrograms(List<ProgramResponse.ProgramModel> programModels) {
        subjects = new String[programModels.size()];
        int index = 0;
        for (ProgramResponse.ProgramModel value : programModels) {
            subjects[index] = (String) value.getProgramName();
            index++;
        }

    }

    private void openPopUpToSelectFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubjectsHomeActivity.this);
        builder.setTitle("Select Semester");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(subjects, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedSubject = subjects[which];
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(selectedSubject)) {
                    adapter.getFilter().filter(selectedSubject);
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
    public void onItemViewSelected(SubjectModel model, int id) {

        switch (id) {
            case 0:
                //delete
                AlertUser(model);
                break;

            case 1:
                // update
                Intent intent = new Intent(SubjectsHomeActivity.this, AddSubjectActivity.class);
                intent.putExtra("subjectData", (Parcelable) model);
                startActivity(intent);
                break;
        }
    }

    private void AlertUser(SubjectModel model) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SubjectsHomeActivity.this);
        builder.setTitle("Alert.!");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        deleteSubject(model.getSubject_code());
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

    private void deleteSubject(String subjectCode) {
        showProgressDialog("Deleting subject please wait...");
        Call<ApiResponse> call = api.deleteSubject(subjectCode);
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
                                Toast.makeText(SubjectsHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                getSubjects();
                            } else {

                                Toast.makeText(SubjectsHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(SubjectsHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(SubjectsHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SubjectsHomeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        searchView.setQueryHint("Search by subject or program");
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
            progressDialog = new ProgressDialog(SubjectsHomeActivity.this);
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