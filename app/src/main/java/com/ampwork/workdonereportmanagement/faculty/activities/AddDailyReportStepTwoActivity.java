package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.ampwork.workdonereportmanagement.faculty.fragments.AddReportFragment;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
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
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDailyReportStepTwoActivity extends AppCompatActivity {

    TextView tvTitle, tvTotalHour;
    TextInputLayout dateInputLayout, typeInputLayout, fromTimeInputLayout, toTimeInputLayout,
            totalPresentInputLayout, totalAbsentInputLayout, descInputLayout;
    TextInputEditText edDate,  edFromTime, edToTime, edTotalPresent, edTotalAbsent, edDesc;
    MaterialButton buttonAdd;
    AutoCompleteTextView autoTvType;
    ProgressDialog progressDialog;
    Api api;
    PreferencesManager preferencesManager;
    String selectedDate, type, fromTime, toTime, desc, day, noOfHours,
            fromSelectedTime, toSelectedTime, totalPresent, totalAbsent, subject, semester, reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_report_step_two);

        initializeViews();
        initializeToolBar();
        initializeTextWatcher();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Add Daily Report");
    }

    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        reportId = getIntent().getExtras().getString("reportId");
        subject = getIntent().getExtras().getString("subject");
        semester = getIntent().getExtras().getString("sem");
        dateInputLayout = findViewById(R.id.DateInputLayout);
        edDate = findViewById(R.id.DateEdt);
        typeInputLayout = findViewById(R.id.typeInputLayout);
        autoTvType = findViewById(R.id.typeAutoComTv);
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
        buttonAdd = findViewById(R.id.addBtn);
        tvTotalHour = findViewById(R.id.tvHour);

        String[] types = {"Theory(T)", "Practical(P)", "Project work(PW)", "Field work(FW)"};
        ArrayAdapter type_adapter = new ArrayAdapter(AddDailyReportStepTwoActivity.this, R.layout.list_item, types);
        autoTvType.setAdapter(type_adapter);
    }

    private void initializeTextWatcher() {
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
        autoTvType.addTextChangedListener(new TextWatcher() {
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

    private void initializeClickEvents() {
        dateInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDatePicker();
            }
        });
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
        buttonAdd.setOnClickListener(new View.OnClickListener() {
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

    private void validateForm() {
        selectedDate = edDate.getText().toString();
        type = autoTvType.getText().toString();
        fromTime = edFromTime.getText().toString();
        toTime = edToTime.getText().toString();
        desc = edDesc.getText().toString();
        totalPresent = edTotalPresent.getText().toString();
        totalAbsent = edTotalAbsent.getText().toString();

        if (TextUtils.isEmpty(selectedDate)) {
            dateInputLayout.setError(getResources().getString(R.string.date_error_txt));
        } else if (TextUtils.isEmpty(type) || type.equals("Select")) {
            typeInputLayout.setError(getResources().getString(R.string.type_error_txt));
        } else if (TextUtils.isEmpty(fromTime)) {
            fromTimeInputLayout.setError(getResources().getString(R.string.from_error_txt));
        } else if (TextUtils.isEmpty(toTime)) {
            toTimeInputLayout.setError(getResources().getString(R.string.to_error_txt));
        } else {
            showProgressDialog("Adding daily report...");
            SaveReportData();
        }
    }

    private void SaveReportData() {
        AddReportModel model = new AddReportModel(selectedDate, day, type, semester, fromTime, toTime,
                noOfHours, desc, reportId, subject, totalPresent, totalAbsent);
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
                            if (msg.equals("Report Added Successfully")) {
                                Toast.makeText(AddDailyReportStepTwoActivity.this, "Report Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddDailyReportStepTwoActivity.this, FacultyNavigationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(AddDailyReportStepTwoActivity.this, "Please try after sometime...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddDailyReportStepTwoActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddDailyReportStepTwoActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddDailyReportStepTwoActivity.this);
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