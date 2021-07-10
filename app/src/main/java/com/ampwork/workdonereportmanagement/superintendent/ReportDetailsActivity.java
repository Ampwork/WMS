package com.ampwork.workdonereportmanagement.superintendent;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.adapter.MonthlyReportAdapter;
import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDetailsActivity extends AppCompatActivity {

    TextView tvViewReport, tvTitle;
    RecyclerView recyclerView;
    MaterialButton btnSubmit;

    ProgressDialog progressDialog;
    Api api;
    MonthlyReportAdapter reportAdapter;
    String userId;

    PreferencesManager preferenceManager;

    GenerateReportResponse.ReportResponseModel reportResponseModel;
    List<AddReportModel> addReportModels;

    File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/report.xlsx");


    private static final String[] titles = {
            " Date", "Semester/Work", "From", "To", "T/P/FW/PW", "No. of Hours/Theory Eqv. hours",
            "Description of the work: Paper Code:--- , Content (Topic Covered) and any other assignments/work carried out"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

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
        tvTitle.setText("Faculty Reports");
    }


    private void initializeViews() {
        preferenceManager = new PreferencesManager(this);
        reportResponseModel = getIntent().getParcelableExtra("data");
        addReportModels = reportResponseModel.getAddReportModels();
        api = ApiClient.getClient().create(Api.class);
        tvViewReport = findViewById(R.id.tvReportExcel);
        btnSubmit = findViewById(R.id.submitBtn);

        userId = preferenceManager.getStringValue(AppConstants.PREF_ID);

        String status = reportResponseModel.getStatus();
        if (status.equals("Approved")) {
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.GONE);
        }
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        if (!addReportModels.isEmpty()) {
            reportAdapter = new MonthlyReportAdapter(ReportDetailsActivity.this, addReportModels);
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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReportDetailsActivity.this);
        //builder.setTitle("Submit report");
        builder.setMessage("Are you sure want to submit report?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String reportId = reportResponseModel.getId();
                        String name = reportResponseModel.getUsername();
                        String month = reportResponseModel.getMonth();
                        String program = reportResponseModel.getProgram();
                        Intent intent = new Intent(ReportDetailsActivity.this, SupSignReportActivity.class);
                        intent.putExtra("reportId", reportId);
                        intent.putExtra("name", name);
                        intent.putExtra("month", month);
                        intent.putExtra("program", program);
                        startActivity(intent);

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


    private void AlertUser() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReportDetailsActivity.this);
        //builder.setTitle("Download Report");
        builder.setMessage("Are you sure want to download report?");
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

        //set data


        int rowCount = 9;

        for (AddReportModel aBook : addReportModels) {
            Row row = sheet.createRow(rowCount++);
            writeBook(aBook, row, wb);
        }

        for (int i = 3; i < 8; i++) {
            sheet.setColumnWidth(i, 15 * 256);  //15 characters wide
        }

        sheet.setColumnWidth(9, 50 * 256);

        //row leaves
        int rowIndex = 9 + addReportModels.size() + 1;


        Row rowLeaves = sheet.createRow(rowIndex);
        rowLeaves.setHeightInPoints(20);
        Cell leavesCell = rowLeaves.createCell(3);
        leavesCell.setCellValue("Total Number of Leaves availed in this month :" + leaves);
        leavesCell.setCellStyle(styles.get("sub"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 6));

        //row remark
        Row rowRemark = sheet.createRow(rowIndex + 1);
        rowRemark.setHeightInPoints(40);
        Cell remarkCell = rowRemark.createCell(3);
        remarkCell.setCellValue("Co-ordinators Remark :" + "\n" + coRemark);
        CellRangeAddress mergedCell = new CellRangeAddress(rowIndex + 1, rowIndex + 1, 3, 9);
        sheet.addMergedRegion(mergedCell);

        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedCell, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedCell, sheet, wb);

        remarkCell.setCellStyle(styles.get("remarkCell"));

        //row remark
        Row rowRemark1 = sheet.createRow(rowIndex + 2);
        rowRemark1.setHeightInPoints(40);
        Cell remarkCell1 = rowRemark1.createCell(3);
        remarkCell1.setCellValue("Registrar Remarks : " + "\n" + supRemark);
        CellRangeAddress mergeCell = new CellRangeAddress(rowIndex + 2, rowIndex + 2, 3, 9);
        sheet.addMergedRegion(mergeCell);

        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergeCell, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergeCell, sheet, wb);

        remarkCell1.setCellStyle(styles.get("remarkCell"));


        //row sign co
        Row rowSignCo = sheet.createRow(rowIndex + 4);
        rowSignCo.setHeightInPoints(30);
        Cell signCoCell = rowSignCo.createCell(3);
        signCoCell.setCellValue("Programme Co-ordinator");
        signCoCell.setCellStyle(styles.get("sub"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex + 3, rowIndex + 3, 3, 5));

        //row sign co
        rowSignCo.setHeightInPoints(30);
        Cell signFacultyCell = rowSignCo.createCell(6);
        signFacultyCell.setCellValue("Faculty/Project Assistant");
        signFacultyCell.setCellStyle(styles.get("sub"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex + 3, rowIndex + 3, 6, 8));

        //row sup sign
        rowSignCo.setHeightInPoints(30);
        Cell signSupCell = rowSignCo.createCell(9);
        signSupCell.setCellValue(" Superintendent ");
        signSupCell.setCellStyle(styles.get("sub"));


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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReportDetailsActivity.this);
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

    private void writeBook(AddReportModel aBook, Row row, Workbook wb) {

        Map<String, CellStyle> styles = createStyles(wb);

        Cell cell = row.createCell(3);
        row.setHeightInPoints(20);
        cell.setCellValue(aBook.getDate());
        cell.setCellStyle(styles.get("cell"));

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

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ReportDetailsActivity.this);
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