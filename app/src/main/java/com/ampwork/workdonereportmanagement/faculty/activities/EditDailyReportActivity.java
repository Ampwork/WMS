package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.SubjectModel;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

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

public class EditDailyReportActivity extends AppCompatActivity {

    TextView tvReportId, tvTitle, tvTotalHour;
    TextInputLayout dateInputLayout, semInputLayout, subjectInputLayout, typeInputLayout, fromTimeInputLayout, toTimeInputLayout,
            totalPresentInputLayout, totalAbsentInputLayout, descInputLayout;
    TextInputEditText edDate, edSemester, edType, edFromTime, edToTime, edTotalPresent, edTotalAbsent, edDesc;
    MaterialButton buttonUpdate;
    AutoCompleteTextView autoTvSubject;
    ProgressDialog progressDialog;

    PreferencesManager preferencesManager;
    Api api;
    String selectedDate, type, fromTime, toTime, desc, day, noOfHours,
            fromSelectedTime, toSelectedTime, totalPresent, totalAbsent, subject, semester, reportId;
    String id, prevDate, prevType, prevSemester, prevSubject, prevFromTime, prevToTime, prevDesc, prevDay, prevNoOfHour,
            prevTotalPresent, prevTotalAbsent;
    List<SubjectModel> subjectModelList = new ArrayList<>();
    AddReportModel addReportModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_report);

        initializeViews();
        initializeToolBar();
        //getSubjectList();
        setData();
        initializeTextWatcher();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);

    }


    private void initializeViews() {
        addReportModel = getIntent().getParcelableExtra("data");
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);

        tvReportId = findViewById(R.id.tvReportId);
        semInputLayout = findViewById(R.id.semesterInputLayout);
        subjectInputLayout = findViewById(R.id.subjectInputLayout);
        autoTvSubject = findViewById(R.id.SubjectAutoComTv);
        edSemester = findViewById(R.id.semesterEd);
        dateInputLayout = findViewById(R.id.DateInputLayout);
        edDate = findViewById(R.id.DateEdt);
        typeInputLayout = findViewById(R.id.typeInputLayout);
        edType = findViewById(R.id.typeEd);
        fromTimeInputLayout = findViewById(R.id.fromTimeInputLayout);
        edFromTime = findViewById(R.id.fromTimeEd);
        toTimeInputLayout = findViewById(R.id.toTimeInputLayout);
        edToTime = findViewById(R.id.toTimeEd);
        descInputLayout = findViewById(R.id.descInputLayout);
        edDesc = findViewById(R.id.descEd);
        totalAbsentInputLayout = findViewById(R.id.totalAbsentInputLayout);
        edTotalAbsent = findViewById(R.id.totalAbsentEd);
        totalPresentInputLayout = findViewById(R.id.totalPresentInputLayout);
        edTotalPresent = findViewById(R.id.totalPresentEd);
        buttonUpdate = findViewById(R.id.updateBtn);
        tvTotalHour = findViewById(R.id.tvHour);


    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Edit Daily Report");
    }


    private void initializeTextWatcher() {
        edSemester.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    semInputLayout.setError(null);
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
                    subjectInputLayout.setError(null);
                }

            }
        });
        edDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    dateInputLayout.setError(null);
                }

            }
        });
        edTotalPresent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    totalPresentInputLayout.setError(null);
                }

            }
        });
        edTotalAbsent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    totalAbsentInputLayout.setError(null);
                }

            }
        });
        edDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    descInputLayout.setError(null);
                }

            }
        });
        edFromTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    fromTimeInputLayout.setError(null);
                }

            }
        });
        edToTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    toTimeInputLayout.setError(null);
                }

            }
        });
        edType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    typeInputLayout.setError(null);
                }

            }
        });
    }


    private void setData() {
        id = addReportModel.getId();
        Log.e("id", "....." + id);
        reportId = addReportModel.getReportid();
        prevDate = addReportModel.getDate();
        prevDay = addReportModel.getDay();
        prevSemester = addReportModel.getSemester();
        prevSubject = addReportModel.getSubject();
        prevDesc = addReportModel.getDescription();
        prevFromTime = addReportModel.getFrom_time();
        prevToTime = addReportModel.getTo_time();
        prevType = addReportModel.getType();
        prevTotalAbsent = addReportModel.getTotal_absent();
        prevTotalPresent = addReportModel.getTotal_present();
        prevNoOfHour = addReportModel.getNo_of_hours();

        tvReportId.setText(reportId);
        edDate.setText(prevDate);
        day = prevDay;
        noOfHours = prevNoOfHour;
        edSemester.setText(prevSemester);
        autoTvSubject.setText(prevSubject);
        edType.setText(prevType);
        edDesc.setText(prevDesc);
        edFromTime.setText(prevFromTime);
        edToTime.setText(prevToTime);
        edTotalAbsent.setText(prevTotalAbsent);
        edTotalPresent.setText(prevTotalPresent);
        tvTotalHour.setText("Total duration(in hour) : " + prevNoOfHour);
        fromSelectedTime = edFromTime.getText().toString();
        toSelectedTime = edToTime.getText().toString();

        edDate.setEnabled(false);
        edSemester.setEnabled(false);
        autoTvSubject.setEnabled(false);
    }

    private void initializeClickEvents() {
       /* dateInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });*/
        fromTimeInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFromTimePicker();
            }
        });
        toTimeInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchToTimePicker();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    private void launchDatePicker() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select Report date");
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        dateInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
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
                    day = simpledateformat.format(date);
                    edDate.setText(output);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void launchFromTimePicker() {
        final MaterialTimePicker materialTimePicker = (new MaterialTimePicker.Builder()).setTitleText((CharSequence) "SELECT FROM TIME").setHour(12).setMinute(10).setTimeFormat(TimeFormat.CLOCK_12H).build();
        materialTimePicker.show(getSupportFragmentManager(), "fromTime");
        materialTimePicker.addOnPositiveButtonClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                int pickedHour = materialTimePicker.getHour();
                int pickedMinute = materialTimePicker.getMinute();
                String formattedTime = pickedHour > 12 ? (pickedMinute < 10 ? materialTimePicker.getHour() - 12 + ":0" + materialTimePicker.getMinute() + " pm" : "" + (materialTimePicker.getHour() - 12) + ':' + materialTimePicker.getMinute() + " pm") : (pickedHour == 12 ? (pickedMinute < 10 ? materialTimePicker.getHour() + ":0" + materialTimePicker.getMinute() + " pm" : "" + materialTimePicker.getHour() + ':' + materialTimePicker.getMinute() + " pm") : (pickedHour == 0 ? (pickedMinute < 10 ? materialTimePicker.getHour() + 12 + ":0" + materialTimePicker.getMinute() + " am" : "" + (materialTimePicker.getHour() + 12) + ':' + materialTimePicker.getMinute() + " am") : (pickedMinute < 10 ? materialTimePicker.getHour() + ":0" + materialTimePicker.getMinute() + " am" : "" + materialTimePicker.getHour() + ':' + materialTimePicker.getMinute() + " am")));
                edFromTime.setText((CharSequence) formattedTime);
                fromSelectedTime = pickedHour + ":" + pickedMinute;
            }
        }));
    }

    private void launchToTimePicker() {
        final MaterialTimePicker materialTimePicker = (new MaterialTimePicker.Builder()).setTitleText((CharSequence) "SELECT To TIME").setHour(12).setMinute(10).setTimeFormat(TimeFormat.CLOCK_12H).build();
        materialTimePicker.show(getSupportFragmentManager(), "toTime");
        materialTimePicker.addOnPositiveButtonClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                int pickedHour = materialTimePicker.getHour();
                int pickedMinute = materialTimePicker.getMinute();
                String formattedTime = pickedHour > 12 ? (pickedMinute < 10 ?
                        materialTimePicker.getHour() - 12 + ":0" + materialTimePicker.getMinute()
                                + " pm" : "" + (materialTimePicker.getHour() - 12) + ':' +
                        materialTimePicker.getMinute() + " pm") : (pickedHour == 12 ?
                        (pickedMinute < 10 ? materialTimePicker.getHour() + ":0" +
                                materialTimePicker.getMinute() + " pm" : "" +
                                materialTimePicker.getHour() + ':' + materialTimePicker.getMinute() + " pm")
                        : (pickedHour == 0 ? (pickedMinute < 10 ? materialTimePicker.getHour() + 12 + ":0" +
                        materialTimePicker.getMinute() + " am" : "" + (materialTimePicker.getHour() + 12) +
                        ':' + materialTimePicker.getMinute() + " am") :
                        (pickedMinute < 10 ? materialTimePicker.getHour() + ":0" + materialTimePicker.getMinute() + " am" : "" + materialTimePicker.getHour() + ':' + materialTimePicker.getMinute() + " am")));
                edToTime.setText((CharSequence) formattedTime);
                toSelectedTime = pickedHour + ":" + pickedMinute;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date date1 = null, date2 = null;
                try {
                    date1 = format.parse(fromSelectedTime);
                    date2 = format.parse(toSelectedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long difference = date2.getTime() - date1.getTime();
                long hour = difference / (60 * 60 * 1000) % 24;
                //long min = difference / (60 * 1000) % 60;
                tvTotalHour.setVisibility(View.VISIBLE);
                noOfHours = String.valueOf(hour);
                tvTotalHour.setText("Total duration(in hour) : " + noOfHours);

            }
        }));
    }


    private void loadSubjects(List<SubjectModel> subjectModelList) {
        String[] subjects = new String[subjectModelList.size()];
        int index = 0;
        for (SubjectModel value : subjectModelList) {
            subjects[index] = (String) value.getSubjectName();
            index++;
        }

        ArrayAdapter sub_adapter = new ArrayAdapter(EditDailyReportActivity.this, R.layout.list_item, subjects);
        autoTvSubject.setAdapter(sub_adapter);
    }

    private void validateForm() {
        selectedDate = edDate.getText().toString();
        semester = edSemester.getText().toString();
        subject = autoTvSubject.getText().toString();
        type = edType.getText().toString();
        fromTime = edFromTime.getText().toString();
        toTime = edToTime.getText().toString();
        desc = edDesc.getText().toString();
        totalPresent = edTotalPresent.getText().toString();
        totalAbsent = edTotalAbsent.getText().toString();

        if (TextUtils.isEmpty(selectedDate)) {
            dateInputLayout.setError(getResources().getString(R.string.date_error_txt));
        } else if (TextUtils.isEmpty(semester) || semester.equals("0")) {
            semInputLayout.setError(getResources().getString(R.string.sem_error_txt));
        } else if (TextUtils.isEmpty(subject) || subject.equals("Select")) {
            subjectInputLayout.setError(getResources().getString(R.string.subject_error_txt));
        } else if (TextUtils.isEmpty(type)) {
            typeInputLayout.setError(getResources().getString(R.string.date_error_txt));
        } else if (TextUtils.isEmpty(fromTime)) {
            fromTimeInputLayout.setError(getResources().getString(R.string.from_error_txt));
        } else if (TextUtils.isEmpty(toTime)) {
            toTimeInputLayout.setError(getResources().getString(R.string.to_error_txt));
        } else {
            if (selectedDate.equalsIgnoreCase(prevDate) && semester.equalsIgnoreCase(prevSemester)
                    && subject.equalsIgnoreCase(prevSubject) && type.equals(prevType) &&
                    fromTime.equalsIgnoreCase(prevFromTime) && toTime.equalsIgnoreCase(prevToTime) &&
                    totalAbsent.equalsIgnoreCase(prevTotalAbsent) && totalPresent.equalsIgnoreCase(prevTotalPresent)
                    && desc.equalsIgnoreCase(prevDesc)) {
                Toast.makeText(this, "No data changes found", Toast.LENGTH_SHORT).show();
            } else {
                showProgressDialog("Adding daily report...");
                SaveReportData();
            }
        }
    }

    private void SaveReportData() {
        AddReportModel model = new AddReportModel(selectedDate, day, type, semester, fromTime, toTime,
                noOfHours, desc, reportId, subject, totalPresent, totalAbsent);
        Log.e("model", "....." + model.toString());
        Call<ApiResponse> call = api.addReport(model);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                ApiResponse apiResponse = response.body();
                String msg = apiResponse.getMessage();
                boolean status = apiResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Report Updated Successfully")) {
                                Toast.makeText(EditDailyReportActivity.this, "Report Updated Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } else {
                            Toast.makeText(EditDailyReportActivity.this, "Please try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(EditDailyReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(EditDailyReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(EditDailyReportActivity.this);
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