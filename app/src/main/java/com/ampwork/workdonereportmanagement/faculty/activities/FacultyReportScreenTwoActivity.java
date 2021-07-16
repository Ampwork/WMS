package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.MonthlyReportAdapter;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.model.ReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyReportScreenTwoActivity extends AppCompatActivity {

    TextView tvViewReport, tvTitle;
    RecyclerView recyclerView;
    MaterialButton btnSubmit;

    ProgressDialog progressDialog;
    Api api;
    MonthlyReportAdapter reportAdapter;
    String userId, semester, subject, fromDate, toDate, month;
    boolean isMonth = false;

    PreferencesManager preferenceManager;

    GenerateReportResponse.ReportResponseModel reportResponseModel;
    List<AddReportModel> addReportModels;
    List<GenerateReportResponse.WeeksModel> weeksModels;
    List<GenerateReportResponse.ReportSubject> reportSubjects;
    File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/report.xlsx");


    private static final String[] titles = {
            " Date", "Semester/Work", "From", "To", "T/P/FW/PW", "No. of Hours/Theory Eqv. hours",
            "Description of the work: Paper Code:--- , Content (Topic Covered) and any other assignments/work carried out"};

    private static final String[] paper_titles = {"Paper Code", "Paper Title", "% of Syllabus Covered in this month", "Cumulative Syllbus Covered"};

    int totalDaysOfMonth, totalNumberOfWeeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_report);

        initializeViews();
        initializeToolBar();
        initializeRecyclerView();
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
        tvTitle.setText("Reports");
    }


    private void initializeViews() {
        preferenceManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);
        tvViewReport = findViewById(R.id.tvReportExcel);
        btnSubmit = findViewById(R.id.submitBtn);

        userId = preferenceManager.getStringValue(AppConstants.PREF_ID);
        isMonth = getIntent().getExtras().getBoolean("isMonth");
        reportResponseModel = getIntent().getParcelableExtra("list");
        addReportModels = reportResponseModel.getAddReportModels();

        if (isMonth) {


            semester = getIntent().getExtras().getString("semester");
            subject = getIntent().getExtras().getString("subject");
            month = getIntent().getExtras().getString("month");
            reportSubjects = reportResponseModel.getReportSubjects();

            totalDaysOfMonth = AppUtility.checkMonth(month);
            int value = AppUtility.checkMonthValue(month);
            int year = Integer.parseInt(reportResponseModel.getYear());
            totalNumberOfWeeks = AppUtility.getNumberOfWeeks(year, value);

            Log.e("days", "1......." + totalNumberOfWeeks);

        } else {
            semester = getIntent().getExtras().getString("semester");
            subject = getIntent().getExtras().getString("subject");
            fromDate = getIntent().getExtras().getString("fromDate");
            toDate = getIntent().getExtras().getString("toDate");
        }

        if (reportResponseModel.getStatus().equals("Accept") || reportResponseModel.getStatus().equals("Approved")) {
            btnSubmit.setVisibility(View.GONE);
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }

    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        if (isMonth) {
            // get monthly report
            reportAdapter = new MonthlyReportAdapter(FacultyReportScreenTwoActivity.this, addReportModels);
            recyclerView.setAdapter(reportAdapter);


        } else {
            //get custom report
            reportAdapter = new MonthlyReportAdapter(FacultyReportScreenTwoActivity.this, addReportModels);
            recyclerView.setAdapter(reportAdapter);

        }
    }


    private void initializeClickEvents() {
        tvViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertUser();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUserToContinue();
            }
        });
    }

    private void AlertUserToContinue() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(FacultyReportScreenTwoActivity.this);
        builder.setTitle("Submit report");
        builder.setMessage("Are you sure you want to submit report?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String reportId = reportResponseModel.getId();
                        /*Intent intent = new Intent(FacultyReportActivity.this,FacultySignAndSubmitReportActivity.class);
                        intent.putExtra("reportId",reportId);
                        startActivity(intent);*/
                        Log.e("user", "id..." + userId);
                        Log.e("report", "id..." + reportId);
                        ReportResponse reportResponse = new ReportResponse(userId, reportId);
                        sendReport(reportResponse);
                    }
                });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendReport(ReportResponse reportResponse) {
        showProgressDialog("Submitting your report...");
        Call<ApiResponse> call = api.facultyReportSubmits(reportResponse);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                ApiResponse apiResponse = response.body();
                String msg = apiResponse.getMessage();
                String coDeviceId = apiResponse.getCo_device();
                Log.e("co device", "....." + coDeviceId);
                Log.e("co device", "....." + msg);
                boolean status = apiResponse.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (status) {
                            if (msg.equals("Report Submitted Successfully")) {
                                Toast.makeText(FacultyReportScreenTwoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                if (!TextUtils.isEmpty(coDeviceId)) {
                                    pushNotification(coDeviceId);
                                }
                                NavigateActivity();

                            } else {
                                Toast.makeText(FacultyReportScreenTwoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FacultyReportScreenTwoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(FacultyReportScreenTwoActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FacultyReportScreenTwoActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void NavigateActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FacultyReportScreenTwoActivity.this, FacultyNavigationActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private void AlertUser() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(FacultyReportScreenTwoActivity.this);
        //builder.setTitle("Download Report");
        builder.setMessage("Are you sure you want to download report?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        createExcelSheet();
                    }
                });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by date");
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
                    reportAdapter.getFilter().filter(newText);
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


    private void createExcelSheet() {
        showProgressDialog("Preparing report...");
        String month = reportResponseModel.getMonth();
        String year = reportResponseModel.getYear();
        String program = reportResponseModel.getProgram();
        String faculty = reportResponseModel.getUsername();
        String leaves = reportResponseModel.getLeaves();
        String coRemark = reportResponseModel.getCo_remark();
        String supRemark = reportResponseModel.getSup_remark();

        Workbook wb = new HSSFWorkbook();
        Map<String, CellStyle> styles = createStyles(wb);
        Sheet sheet = wb.createSheet("Report");

        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(50);
        Cell headerCell = headerRow.createCell(3);
        headerCell.setCellValue("");
        headerCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$1:$J$1"));

        //row title
        Row titleRow = sheet.createRow(1);
        titleRow.setHeightInPoints(30);
        Cell titleCell = titleRow.createCell(3);
        titleCell.setCellValue("Karnataka State Rural Development and Panchayat Raj University, Gadag");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$2:$J$2"));


        //row subTitle
        Row subTitle = sheet.createRow(2);
        subTitle.setHeightInPoints(20);
        Cell subTitleCell = subTitle.createCell(3);
        subTitleCell.setCellValue("Work Done Report for the Month of " + month + "," + year);
        subTitleCell.setCellStyle(styles.get("subTitle"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$3:$J$3"));

        //row program
        Row rowProgram = sheet.createRow(4);
        rowProgram.setHeightInPoints(20);
        Cell programCell = rowProgram.createCell(3);
        programCell.setCellValue("Program : " + program);
        programCell.setCellStyle(styles.get("sub"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$5:$G$5"));

        //row faculty name
        Row rowFaculty = sheet.createRow(5);
        rowFaculty.setHeightInPoints(20);
        Cell facultyCell = rowFaculty.createCell(3);
        facultyCell.setCellValue("Name of the Faculty/Project Assistant : " + faculty);
        facultyCell.setCellStyle(styles.get("sub"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$6:$I$6"));


        //row date
        //Row rowDate = sheet.createRow(6);
        rowFaculty.setHeightInPoints(20);
        Cell dateCell = rowFaculty.createCell(9);
        dateCell.setCellValue("Date : " + "31-06-2021");
        dateCell.setCellStyle(styles.get("sub"));

        //header row
        Row titleHeaderRow = sheet.createRow(8);
        titleHeaderRow.setHeightInPoints(40);
        Cell headerTitleCell;
        int cellCount = 3;
        for (int i = 0; i < titles.length; i++) {
            headerTitleCell = titleHeaderRow.createCell(cellCount);
            headerTitleCell.setCellValue(titles[i]);
            headerTitleCell.setCellStyle(styles.get("header"));
            cellCount++;
        }


        if (isMonth) {

            //set data
            int rowCount = 9;
            int monthValue = AppUtility.checkMonthValue(month);






            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthValue - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            for (int i=0;i<weeksModels.size();i++)
            {
                Row row = sheet.createRow(rowCount++);
                writeBook(row, wb);
                Cell cell = row.createCell(3);


                Log.e("week","........"+weeksModels.get(i).getWeeknumber());
                Iterator<JsonElement> iterator = weeksModels.get(i).getJsonObject().iterator();
                while(iterator.hasNext()) {
                    System.out.println(iterator.next());

                    row.setHeightInPoints(20);
                    cell.setCellValue(iterator.next().toString());
                    cell.setCellStyle(styles.get("cell"));
                }

                row.setHeightInPoints(20);
                cell.setCellStyle(styles.get("cell"));
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 3, 9));
            }

            /*for (int i = 1; i < maxDay; i++) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                String dateStr = df.format(cal.getTime());
                Row row = sheet.createRow(rowCount++);
                writeBook(row, wb);


                Cell cell = row.createCell(3);
                row.setHeightInPoints(20);
                cell.setCellValue(dateStr);
                cell.setCellStyle(styles.get("cell"));

                for (AddReportModel aBook : addReportModels) {
                    if (aBook.getDate().equals(dateStr)) {
                        cell = row.createCell(4);
                        row.setHeightInPoints(20);
                        cell.setCellValue(aBook.getSemester());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(5);
                        row.setHeightInPoints(20);
                        cell.setCellValue(aBook.getFrom_time());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(6);
                        row.setHeightInPoints(20);
                        cell.setCellValue(aBook.getTo_time());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(7);
                        row.setHeightInPoints(20);
                        cell.setCellValue(aBook.getType());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(8);
                        row.setHeightInPoints(20);
                        cell.setCellValue(aBook.getNo_of_hours());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(9);
                        row.setHeightInPoints(20);
                        cell.setCellValue(aBook.getDescription());
                        cell.setCellStyle(styles.get("cell"));
                    } else {
                        cell = row.createCell(4);
                        row.setHeightInPoints(20);
                        // cell.setCellValue(aBook.getSemester());
                        cell.setCellStyle(styles.get("cell"));

                        cell = row.createCell(5);
                        row.setHeightInPoints(20);
                        //cell.setCellValue(aBook.getFrom_time());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(6);
                        row.setHeightInPoints(20);
                        //cell.setCellValue(aBook.getTo_time());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(7);
                        row.setHeightInPoints(20);
                        //cell.setCellValue(aBook.getType());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(8);
                        row.setHeightInPoints(20);
                        //cell.setCellValue(aBook.getNo_of_hours());
                        cell.setCellStyle(styles.get("cell"));


                        cell = row.createCell(9);
                        row.setHeightInPoints(20);
                        // cell.setCellValue(aBook.getDescription());
                        cell.setCellStyle(styles.get("cell"));
                    }

                }


            }*/

            for (int i = 3; i < 8; i++) {
                sheet.setColumnWidth(i, 15 * 256);  //15 characters wide
            }

            sheet.setColumnWidth(9, 50 * 256);

            //row leaves
            int rowIndex = 9 + totalDaysOfMonth + 1;

            //paper allotted header
            Row paperAlloted = sheet.createRow(rowIndex);
            paperAlloted.setHeightInPoints(20);
            Cell paperCell = paperAlloted.createCell(3);
            paperCell.setCellValue("Paper Allotted :");
            paperCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 9));

            //row paper code
            Row rowPaperCode = sheet.createRow(rowIndex + 1);
            rowPaperCode.setHeightInPoints(40);
            Cell paperCodeCell = rowPaperCode.createCell(3);
            paperCodeCell.setCellValue("Paper Code ");
            paperCodeCell.setCellStyle(styles.get("header"));

            // row paper title

            rowPaperCode.setHeightInPoints(40);
            Cell paperTitleCell = rowPaperCode.createCell(4);
            paperTitleCell.setCellValue("Paper Title");
            paperTitleCell.setCellStyle(styles.get("header"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + 1, rowIndex + 1, 4, 5));

            // perc row
            rowPaperCode.setHeightInPoints(40);
            Cell paperPercCell = rowPaperCode.createCell(6);
            paperPercCell.setCellValue("% of Syllabus Covered in this month");
            paperPercCell.setCellStyle(styles.get("header"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + 1, rowIndex + 1, 6, 8));

            // subject covered row
            rowPaperCode.setHeightInPoints(40);
            Cell paperCoveredCell = rowPaperCode.createCell(9);
            paperCoveredCell.setCellValue("Cumulative Syllabus Covered");
            paperCoveredCell.setCellStyle(styles.get("header"));


            int rowData = rowIndex + 2;
            int mergeIndex = rowData;
            for (GenerateReportResponse.ReportSubject aBook : reportSubjects) {
                Row row = sheet.createRow(rowData++);
                writeSubjects(aBook, row, wb, sheet, mergeIndex);
            }

            int rowIndex1 = rowData + reportSubjects.size() + 1;

            Row rowLeaves = sheet.createRow(rowIndex1 + 4);
            rowLeaves.setHeightInPoints(20);
            Cell leavesCell = rowLeaves.createCell(3);
            leavesCell.setCellValue("Total Number of Leaves availed in this month :" + leaves);
            leavesCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex1 + 2, rowIndex1 + 2, 3, 6));

            //row remark
            Row rowRemark = sheet.createRow(rowIndex1 + 3);
            rowRemark.setHeightInPoints(40);
            Cell remarkCell = rowRemark.createCell(3);
            remarkCell.setCellValue("Co-ordinators Remark :" + "\n" + coRemark);
            CellRangeAddress mergedCell = new CellRangeAddress(rowIndex1 + 3, rowIndex1 + 3, 3, 9);
            sheet.addMergedRegion(mergedCell);

            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedCell, sheet, wb);

            remarkCell.setCellStyle(styles.get("remarkCell"));

            //row remark
            Row rowRemark1 = sheet.createRow(rowIndex1 + 4);
            rowRemark1.setHeightInPoints(40);
            Cell remarkCell1 = rowRemark1.createCell(3);
            remarkCell1.setCellValue("Registrar Remarks : " + "\n" + supRemark);
            CellRangeAddress mergeCell = new CellRangeAddress(rowIndex1 + 4, rowIndex1 + 4, 3, 9);
            sheet.addMergedRegion(mergeCell);

            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergeCell, sheet, wb);

            remarkCell1.setCellStyle(styles.get("remarkCell"));


            //row sign co
            Row rowSignCo = sheet.createRow(rowIndex1 + 6);
            rowSignCo.setHeightInPoints(30);
            Cell signCoCell = rowSignCo.createCell(3);
            signCoCell.setCellValue("Programme Co-ordinator");
            signCoCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex1 + 5, rowIndex1 + 5, 3, 5));

            //row sign co
            rowSignCo.setHeightInPoints(30);
            Cell signFacultyCell = rowSignCo.createCell(6);
            signFacultyCell.setCellValue("Faculty/Project Assistant");
            signFacultyCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex1 + 5, rowIndex1 + 5, 6, 8));

            //row sup sign
            rowSignCo.setHeightInPoints(30);
            Cell signSupCell = rowSignCo.createCell(9);
            signSupCell.setCellValue(" Superintendent ");
            signSupCell.setCellStyle(styles.get("sub"));
        } else {

            //set data
            int rowCount = 9;
            for (AddReportModel aBook : addReportModels) {
                Row row = sheet.createRow(rowCount++);
                // writeBook(aBook, row, wb);
            }
            for (int i = 3; i < 8; i++) {
                sheet.setColumnWidth(i, 15 * 256);  //15 characters wide
            }

            sheet.setColumnWidth(9, 50 * 256);

            //row leaves
            int rowIndex = 9 + addReportModels.size() + 1;


            Row rowLeaves = sheet.createRow(rowIndex + 4);
            rowLeaves.setHeightInPoints(20);
            Cell leavesCell = rowLeaves.createCell(3);
            leavesCell.setCellValue("Total Number of Leaves availed in this month :" + leaves);
            leavesCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + 2, rowIndex + 2, 3, 6));

            //row remark
            Row rowRemark = sheet.createRow(rowIndex + 3);
            rowRemark.setHeightInPoints(40);
            Cell remarkCell = rowRemark.createCell(3);
            remarkCell.setCellValue("Co-ordinators Remark :" + "\n" + coRemark);
            CellRangeAddress mergedCell = new CellRangeAddress(rowIndex + 3, rowIndex + 3, 3, 9);
            sheet.addMergedRegion(mergedCell);

            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedCell, sheet, wb);

            remarkCell.setCellStyle(styles.get("remarkCell"));

            //row remark
            Row rowRemark1 = sheet.createRow(rowIndex + 4);
            rowRemark1.setHeightInPoints(40);
            Cell remarkCell1 = rowRemark1.createCell(3);
            remarkCell1.setCellValue("Registrar Remarks : " + "\n" + supRemark);
            CellRangeAddress mergeCell = new CellRangeAddress(rowIndex + 4, rowIndex + 4, 3, 9);
            sheet.addMergedRegion(mergeCell);

            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergeCell, sheet, wb);

            remarkCell1.setCellStyle(styles.get("remarkCell"));


            //row sign co
            Row rowSignCo = sheet.createRow(rowIndex + 6);
            rowSignCo.setHeightInPoints(30);
            Cell signCoCell = rowSignCo.createCell(3);
            signCoCell.setCellValue("Programme Co-ordinator");
            signCoCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + 5, rowIndex + 5, 3, 5));

            //row sign co
            rowSignCo.setHeightInPoints(30);
            Cell signFacultyCell = rowSignCo.createCell(6);
            signFacultyCell.setCellValue("Faculty/Project Assistant");
            signFacultyCell.setCellStyle(styles.get("sub"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + 5, rowIndex + 5, 6, 8));

            //row sup sign
            rowSignCo.setHeightInPoints(30);
            Cell signSupCell = rowSignCo.createCell(9);
            signSupCell.setCellValue(" Superintendent ");
            signSupCell.setCellStyle(styles.get("sub"));
        }


        FileOutputStream outputStream = null;

        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            outputStream = new FileOutputStream(filePath);
            wb.write(outputStream);
            showDialogUser();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Downloading failed", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void showDialogUser() {
        hideProgressDialog();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(FacultyReportScreenTwoActivity.this);
        builder.setTitle("Download Completed.");
        builder.setMessage("File saved in downloads");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void writeBook(Row row, Workbook wb) {

        Map<String, CellStyle> styles = createStyles(wb);

        Cell cell = row.createCell(3);
        row.setHeightInPoints(20);
        //cell.setCellValue(aBook.getDate());
        cell.setCellStyle(styles.get("cell"));

        cell = row.createCell(4);
        row.setHeightInPoints(20);
        // cell.setCellValue(aBook.getSemester());
        cell.setCellStyle(styles.get("cell"));

        cell = row.createCell(5);
        row.setHeightInPoints(20);
        // cell.setCellValue(aBook.getFrom_time());
        cell.setCellStyle(styles.get("cell"));


        cell = row.createCell(6);
        row.setHeightInPoints(20);
        //  cell.setCellValue(aBook.getTo_time());
        cell.setCellStyle(styles.get("cell"));


        cell = row.createCell(7);
        row.setHeightInPoints(20);
        // cell.setCellValue(aBook.getType());
        cell.setCellStyle(styles.get("cell"));


        cell = row.createCell(8);
        row.setHeightInPoints(20);
        //  cell.setCellValue(aBook.getNo_of_hours());
        cell.setCellStyle(styles.get("cell"));


        cell = row.createCell(9);
        row.setHeightInPoints(20);
        // cell.setCellValue(aBook.getDescription());
        cell.setCellStyle(styles.get("cell"));


    }

    private void writeSubjects(GenerateReportResponse.ReportSubject aBook, Row row, Workbook wb, Sheet sheet, int mergeIndex) {
        Map<String, CellStyle> styles = createStyles(wb);

        Cell cell = row.createCell(3);
        row.setHeightInPoints(20);
        cell.setCellValue(aBook.getSubject_code());
        cell.setCellStyle(styles.get("cell"));

        cell = row.createCell(4);
        row.setHeightInPoints(20);
        cell.setCellValue(aBook.getSubject_name());
        cell.setCellStyle(styles.get("cell"));
        CellRangeAddress mergedCell = new CellRangeAddress(mergeIndex, mergeIndex, 4, 5);
        sheet.addMergedRegion(mergedCell);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedCell, sheet, wb);

        cell = row.createCell(6);
        row.setHeightInPoints(20);
        cell.setCellValue(aBook.getPercentage());
        cell.setCellStyle(styles.get("cell"));
        CellRangeAddress mergedCell1 = new CellRangeAddress(mergeIndex, mergeIndex, 6, 8);
        sheet.addMergedRegion(mergedCell1);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedCell1, sheet, wb);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedCell1, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedCell1, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedCell1, sheet, wb);

        cell = row.createCell(9);
        row.setHeightInPoints(20);
        cell.setCellValue("");
        cell.setCellStyle(styles.get("cell"));
    }

    /**
     * Create a library of cell styles
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontName("Calibri");
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font subTitleFont = wb.createFont();
        subTitleFont.setFontName("Calibri");
        subTitleFont.setFontHeightInPoints((short) 11);
        subTitleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(subTitleFont);
        styles.put("subTitle", style);

        Font subFont = wb.createFont();
        subFont.setFontName("Calibri");
        subFont.setFontHeightInPoints((short) 11);
        subFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(subFont);
        styles.put("sub", style);


        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("remarkCell", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }

    private void pushNotification(String token) {
        String fName = preferenceManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        String lName = preferenceManager.getStringValue(AppConstants.PREF_LAST_NAME);
        String fullName = fName + " " + lName;
        String month = reportResponseModel.getMonth();
        String program = reportResponseModel.getProgram();
        String reportId = reportResponseModel.getId();


        String message = fullName + "\nSubmitted the report of month " + month + "\n" + "Program :" + program;
        String title = "New Report Received";
        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            data.put("title", title.trim());
            data.put("message", message);
            data.put("reportId", reportId);
            //data.put("notifyto", "wms");

            json.put("to", token);
            json.put("data", data);

            new PushNotificationToAllTask().execute(String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PushNotificationToAllTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            OkHttpClient okHttpClient = new OkHttpClient();
            String jsonData = strings[0];
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonData); // new

            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .addHeader("Authorization", "key=AAAA0LqK75g:APA91bF6MJVo7nTC3YcVc2u5imz-cT8qSyEe3MaCOBVRel_fJye_RMFurvqzfqPKTe-KTK70aAzKfrUK1P5FBvyzsC-NoVZt0U0JnbyhbZs3s4yt5IdtDSP-PDPYvQvxh286UEFPzDMs")
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            okhttp3.Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("notification", "..........." + response.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(FacultyReportScreenTwoActivity.this);
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