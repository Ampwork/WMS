package com.ampwork.workdonereportmanagement.clerk.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramRepose;
import com.ampwork.workdonereportmanagement.model.StudentDetailsModel;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStudentActivity extends AppCompatActivity {

    TextView tvTitle;
    TextInputLayout nameTextInputLayout, usnTextInputLayout, programTextInputLayout, semTextInputLayout,
            genderTextInputLayout, emailTextInputLayout, dobTextInputLayout, phoneTextInputLayout, addressTextInputLayout;
    TextInputEditText edName, edUsn, edDob, edEmail, edPhone, edAddress;
    AutoCompleteTextView autoTvSemester, autoTvProgram, autoTvGender;
    MaterialButton createBtn;
    PreferencesManager preferencesManager;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Api api;
    ProgressDialog progressDialog;
    List<ProgramRepose.ProgramModel> programModels = new ArrayList<>();
    StudentDetailsModel studentDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentDetailsModel = getIntent().getParcelableExtra("studentData");
        initializeViews();
        initializeToolBar();
        initializeTextWatcher();
        getProgramList();
        initializeClickEvents();

        if (studentDetailsModel != null) {
            createBtn.setText("Update");
            tvTitle.setText("Update Student Details");
            edName.setText(studentDetailsModel.getName());
            edUsn.setText(studentDetailsModel.getUsn());
            edUsn.setEnabled(false);
            autoTvProgram.setText(studentDetailsModel.getProgram());
            autoTvSemester.setText(studentDetailsModel.getSemester());
            autoTvGender.setText(studentDetailsModel.getGender());
            if (TextUtils.isEmpty(studentDetailsModel.getEmail())) {
                edEmail.setText("");
            } else {
                edEmail.setText(studentDetailsModel.getEmail());
            }
            if (TextUtils.isEmpty(studentDetailsModel.getDob())) {
                edDob.setText("");
            } else {
                edDob.setText(studentDetailsModel.getDob());
            }
            if (TextUtils.isEmpty(studentDetailsModel.getPhone())) {
                edPhone.setText("");
            } else {
                edPhone.setText(studentDetailsModel.getPhone());
            }
            if (TextUtils.isEmpty(studentDetailsModel.getAddress())) {
                edAddress.setText("");
            } else {
                edAddress.setText(studentDetailsModel.getAddress());
            }
        } else {
            createBtn.setText("Create");
            tvTitle.setText("Add New Student");
        }
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }


    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);

        nameTextInputLayout = findViewById(R.id.nameInputLayout);
        edName = findViewById(R.id.nameEd);
        usnTextInputLayout = findViewById(R.id.usnInputLayout);
        edUsn = findViewById(R.id.usnEd);
        programTextInputLayout = findViewById(R.id.programInputLayout);
        autoTvProgram = findViewById(R.id.programAutoComTv);
        semTextInputLayout = findViewById(R.id.semesterInputLayout);
        autoTvSemester = findViewById(R.id.semAutoComTv);
        genderTextInputLayout = findViewById(R.id.genderInputLayout);
        autoTvGender = findViewById(R.id.genderAutoComTv);
        emailTextInputLayout = findViewById(R.id.emailInputLayout);
        edEmail = findViewById(R.id.emailEd);
        dobTextInputLayout = findViewById(R.id.dobInputLayout);
        edDob = findViewById(R.id.dobEd);
        phoneTextInputLayout = findViewById(R.id.phoneInputLayout);
        edPhone = findViewById(R.id.phoneEd);
        addressTextInputLayout = findViewById(R.id.addressInputLayout);
        edAddress = findViewById(R.id.addressEd);

        createBtn = findViewById(R.id.createBtn);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.settingTitle);
        Toolbar toolbar = findViewById(R.id.settingbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeTextWatcher() {
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    nameTextInputLayout.setError(null);
                }

            }
        });

        edUsn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    usnTextInputLayout.setError(null);
                }

            }
        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    emailTextInputLayout.setError(null);
                }

            }
        });

        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    addressTextInputLayout.setError(null);
                }

            }
        });

        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    phoneTextInputLayout.setError(null);
                }

            }
        });
        edDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    dobTextInputLayout.setError(null);
                }

            }
        });
        autoTvSemester.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    semTextInputLayout.setError(null);
                }

            }
        });
        autoTvGender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    genderTextInputLayout.setError(null);
                }

            }
        });

        autoTvProgram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    programTextInputLayout.setError(null);
                }

            }
        });
    }


    private void initializeClickEvents() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
        dobTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });
    }

    private void launchDatePicker() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select DOB");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        dobTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {


                Date date = null;
                try {
                    String string = materialDatePicker.getHeaderText();//"Jan 2, 10";

                    DateFormat format = new SimpleDateFormat("MMM d, yy", Locale.ENGLISH);
                    date = format.parse(string);

                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                    String output = outputFormat.format(date);

                    edDob.setText(output);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void validateForm() {
        String name = edName.getText().toString();
        String usn = edUsn.getText().toString();
        String program = autoTvProgram.getText().toString();
        String semester = autoTvSemester.getText().toString();
        String dob = edDob.getText().toString();
        String email = edEmail.getText().toString();
        String phone = edPhone.getText().toString();
        String gender = autoTvGender.getText().toString();
        String address = edAddress.getText().toString();

        if (TextUtils.isEmpty(name) || name.length() < 4) {
            nameTextInputLayout.setError(getResources().getString(R.string.name_error_txt));
        } else if (TextUtils.isEmpty(usn)) {
            usnTextInputLayout.setError(getResources().getString(R.string.usn_error_txt));
        } else if (TextUtils.isEmpty(program) || program.equals("Select")) {
            programTextInputLayout.setError(getResources().getString(R.string.program_error_txt));
        } else if (TextUtils.isEmpty(semester) || semester.equals("Select")) {
            semTextInputLayout.setError(getResources().getString(R.string.sem_error_txt));
        } else if (TextUtils.isEmpty(gender) || gender.equals("Select")) {
            genderTextInputLayout.setError(getResources().getString(R.string.gender_error_txt));
        } else {
            StudentDetailsModel studentDetailsModel = new StudentDetailsModel(usn, name, program, semester, dob, email, phone, gender, address);
            if (studentDetailsModel==null)
            {
                //create new student data
                SaveStudentDetails(studentDetailsModel);
            }else {
                //update student data
                updateStudentDetails(studentDetailsModel);

            }

        }
    }



    private void SaveStudentDetails(StudentDetailsModel studentDetailsModel) {
        showProgressDialog("Adding student details...");
        Call<ApiResponse> call = api.addstudent(studentDetailsModel);
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
                            if (msg.equals("Student Added Successfully")) {
                                Toast.makeText(AddStudentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddStudentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddStudentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddStudentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddStudentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStudentDetails(StudentDetailsModel studentDetailsModel) {
        showProgressDialog("updating student details...");
        Call<ApiResponse> call = api.editstudent(studentDetailsModel);
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
                            if (msg.equals("Student Updated Successfully")) {
                                Toast.makeText(AddStudentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddStudentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddStudentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddStudentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddStudentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProgramList() {
        showProgressDialog("Please wait...");
        Call<ProgramRepose> call = api.getPrograms();
        call.enqueue(new Callback<ProgramRepose>() {
            @Override
            public void onResponse(Call<ProgramRepose> call, Response<ProgramRepose> response) {
                int statusCode = response.code();
                ProgramRepose repose = response.body();
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
                            Toast.makeText(AddStudentActivity.this, "Programs not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddStudentActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramRepose> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddStudentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPrograms(List<ProgramRepose.ProgramModel> programModels) {
        String[] subjects = new String[programModels.size()];
        int index = 0;
        for (ProgramRepose.ProgramModel value : programModels) {
            subjects[index] = (String) value.getProgramName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(AddStudentActivity.this, R.layout.list_item, subjects);
        autoTvProgram.setAdapter(sub_adapter);

        //load semester
        String[] sem = {"1", "2", "3", "4"};
        ArrayAdapter sem_adapter = new ArrayAdapter(AddStudentActivity.this, R.layout.list_item, sem);
        autoTvSemester.setAdapter(sem_adapter);

        //load gender
        String[] gender = {"Male", "Female"};
        ArrayAdapter gender_adapter = new ArrayAdapter(AddStudentActivity.this, R.layout.list_item, gender);
        autoTvGender.setAdapter(gender_adapter);
    }


    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddStudentActivity.this);
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