package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.activities.FacultyReportScreenOneActivity;

import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.model.SubjectResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class GenerateReportFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    TextView tvTitle;
    ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    private RadioButton monthlyRadioBtn, customRadioBtn;
    TextInputLayout programTextInputLayout, semesterTextInputLayout, subjectTextInputLayout,
            fromDateTextInputLayout, toDateTextInputLayout, monthlyTextInputLayout,yearTextInputLayout;
    TextInputEditText edFromDate, edToDate;
    AutoCompleteTextView autoTvSem, autoTvSubject, autoTvProgram, autoTvMonth,autoTvYear;
    MaterialButton reportBtn;
    ConstraintLayout parentLayout;
    LinearLayout dateRangeLayout,monthYearLayout;

    PreferencesManager preferencesManager;
    Api api;
    String userId, program;
    boolean isMonth = false;
    List<ProgramResponse.ProgramModel> programModels = new ArrayList<>();
    List<SubjectModel> subjectModelList = new ArrayList<>();

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
    String[] years = {"2021", "2022", "2023", "2024", "2025","2026","2027","2028","2029","2030"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_generate_report, container, false);


        initializeViews(root);
        initializeTextWatcher();
        getProgramList();

        initializeClickEvent();
        return root;
    }


    private void initializeViews(View root) {
        api = ApiClient.getClient().create(Api.class);
        preferencesManager = new PreferencesManager(getActivity());
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);

        tvTitle = root.findViewById(R.id.tvtitle);
        Toolbar toolbar = root.findViewById(R.id.toolbarcom);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Reports");

        parentLayout = root.findViewById(R.id.cl1);
        dateRangeLayout = root.findViewById(R.id.ll1);
        monthYearLayout = root.findViewById(R.id.llMonth);
        radioGroup = root.findViewById(R.id.radioGrp);
        monthlyRadioBtn = root.findViewById(R.id.monthRadio);
        customRadioBtn = root.findViewById(R.id.customRadio);


        programTextInputLayout = root.findViewById(R.id.pgmInputLayout);
        autoTvProgram = root.findViewById(R.id.pgmAutoComTv);
        semesterTextInputLayout = root.findViewById(R.id.semInputLayout);
        autoTvSem = root.findViewById(R.id.semAutoComTv);
        subjectTextInputLayout = root.findViewById(R.id.subjectInputLayout);
        autoTvSubject = root.findViewById(R.id.subjectAutoComTv);
        fromDateTextInputLayout = root.findViewById(R.id.fromDateInputLayout);
        edFromDate = root.findViewById(R.id.fromDateEdt);
        toDateTextInputLayout = root.findViewById(R.id.toDateInputLayout);
        monthlyTextInputLayout = root.findViewById(R.id.monthInputLayout);
        autoTvMonth = root.findViewById(R.id.monthAutoComTv);

        yearTextInputLayout = root.findViewById(R.id.yearInputLayout);
        autoTvYear = root.findViewById(R.id.yearAutoComTv);

        edToDate = root.findViewById(R.id.toDateEdt);
        reportBtn = root.findViewById(R.id.btnReport);

        program = autoTvProgram.getText().toString();


    }


    private void initializeTextWatcher() {
        edFromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    fromDateTextInputLayout.setError(null);
                }

            }
        });

        edToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    toDateTextInputLayout.setError(null);
                }

            }
        });

        autoTvSem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    semesterTextInputLayout.setError(null);
                }

            }
        });

        autoTvSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    subjectTextInputLayout.setError(null);
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

        autoTvMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    monthlyTextInputLayout.setError(null);
                }

            }
        });
    }


    private void initializeClickEvent() {
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

        fromDateTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFromDatePicker();
            }
        });

        toDateTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchToDatePicker();
            }
        });
        autoTvProgram.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                program = (String) parent.getItemAtPosition(position);
                if (program.equals("Select")) {
                    Toast.makeText(getActivity(), "Please select program", Toast.LENGTH_SHORT).show();
                } else {
                    getSubjectList(program);
                }
            }
        });
        autoTvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String subject = (String) parent.getItemAtPosition(position);

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.monthRadio) {
                    isMonth = true;
                    parentLayout.setVisibility(View.VISIBLE);
                    dateRangeLayout.setVisibility(View.GONE);
                    monthYearLayout.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.customRadio) {
                    isMonth = false;
                    parentLayout.setVisibility(View.VISIBLE);
                    dateRangeLayout.setVisibility(View.VISIBLE);
                    monthYearLayout.setVisibility(View.GONE);
                } else {
                    parentLayout.setVisibility(View.GONE);
                    dateRangeLayout.setVisibility(View.GONE);
                    monthYearLayout.setVisibility(View.GONE);
                }

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
                            Toast.makeText(getActivity(), "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPrograms(List<ProgramResponse.ProgramModel> programModels) {
        String[] subjects = new String[programModels.size()];
        int index = 0;
        for (ProgramResponse.ProgramModel value : programModels) {
            subjects[index] = (String) value.getProgramName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(getActivity(), R.layout.list_item, subjects);
        autoTvProgram.setAdapter(sub_adapter);

        ArrayAdapter month_adapter = new ArrayAdapter(getActivity(), R.layout.list_item, months);
        autoTvMonth.setAdapter(month_adapter);

        ArrayAdapter year_adapter = new ArrayAdapter(getActivity(), R.layout.list_item, years);
        autoTvYear.setAdapter(year_adapter);
    }

    private void getSubjectList(String program) {
        showProgressDialog("Please wait...");
        Call<SubjectResponse> call = api.getSubjects(program);

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
                                    loadSubjects(subjectModelList);
                                }

                            }
                        } else {
                            Toast.makeText(getActivity(), "Subject not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSubjects(List<SubjectModel> subjectModelList) {
        String[] subjects = new String[subjectModelList.size()];
        int index = 0;
        for (SubjectModel value : subjectModelList) {
            subjects[index] = (String) value.getSubjectName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(getActivity(), R.layout.list_item, subjects);
        autoTvSubject.setAdapter(sub_adapter);

        //load semester
        String sem[] = {"1", "2", "3", "4"};
        ArrayAdapter sem_adapter = new ArrayAdapter(getActivity(), R.layout.list_item, sem);
        autoTvSem.setAdapter(sem_adapter);
    }


    private void launchFromDatePicker() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select From date");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        fromDateTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getActivity().getSupportFragmentManager(), "Date Picker");

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
                    edFromDate.setText(output);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void launchToDatePicker() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select Report date");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        toDateTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getActivity().getSupportFragmentManager(), "Date Picker");

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
                    edToDate.setText(output);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void validateFields() {
        String program = autoTvProgram.getText().toString();
        String semester = autoTvSem.getText().toString();
        String subject = autoTvSubject.getText().toString();
        String fromDate = edFromDate.getText().toString();
        String toDate = edToDate.getText().toString();
        String month = autoTvMonth.getText().toString();
        String year = autoTvYear.getText().toString();

        if (TextUtils.isEmpty(program) || program.equals("Select")) {
            programTextInputLayout.setError(getResources().getString(R.string.program_error_txt));
        } else if (TextUtils.isEmpty(semester) || semester.equals("Select")) {
            semesterTextInputLayout.setError(getResources().getString(R.string.sem_error_txt));
        } else if (TextUtils.isEmpty(subject) || subject.equals("Select")) {
            subjectTextInputLayout.setError(getResources().getString(R.string.subject_error_txt));
        } else {
            if (isMonth) {
                if (TextUtils.isEmpty(month) || month.equals("Select")) {
                    monthlyTextInputLayout.setError(getResources().getString(R.string.month_error_txt));
                }else if (TextUtils.isEmpty(year) || year.equals("Select")){
                    yearTextInputLayout.setError(getResources().getString(R.string.year_error_txt));
                }
                else {
                    isMonth = true;
                    Intent intent = new Intent(getActivity(), FacultyReportScreenOneActivity.class);
                    intent.putExtra("isMonth", isMonth);
                    intent.putExtra("semester", semester);
                    intent.putExtra("subject", subject);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    startActivity(intent);
                }
            } else {
                if (TextUtils.isEmpty(fromDate)) {
                    fromDateTextInputLayout.setError(getResources().getString(R.string.date_error_txt));
                } else if (TextUtils.isEmpty(toDate)) {
                    toDateTextInputLayout.setError(getResources().getString(R.string.date_error_txt));
                } else {
                    Intent intent = new Intent(getActivity(), FacultyReportScreenOneActivity.class);
                    intent.putExtra("isMonth", isMonth);
                    intent.putExtra("semester", semester);
                    intent.putExtra("subject", subject);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toDate", toDate);
                    startActivity(intent);
                }

            }


        }
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            AppUtility.changeStatusBarColor(getActivity());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.navigation_generatereport).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(R.id.navigation_generatereport).setChecked(true);

        autoTvProgram.setText("Select");
        autoTvSem.setText("Select");
        autoTvSubject.setText("Select");
        autoTvMonth.setText("Select");
        autoTvYear.setText("Select");
        edFromDate.getText().clear();
        edToDate.getText().clear();

        monthlyRadioBtn.setChecked(false);
        customRadioBtn.setChecked(false);

        parentLayout.setVisibility(View.GONE);
        dateRangeLayout.setVisibility(View.GONE);

        getProgramList();

    }

}
