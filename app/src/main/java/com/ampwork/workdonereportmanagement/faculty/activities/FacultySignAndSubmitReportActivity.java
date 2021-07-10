package com.ampwork.workdonereportmanagement.faculty.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.faculty.fragments.FacultyHomeFragment;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultySignAndSubmitReportActivity extends AppCompatActivity {

    TextView tvTitle;
    SignaturePad signaturePad;
    Button clearButton;
    MaterialButton submitBtn;
    File mSignFile;

    Api api;
    ProgressDialog progressDialog;
    String reportId, userId;
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_sign_and_submit_report);

        initializeViews();
        initializeToolBar();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }


    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);
        api = ApiClient.getClient().create(Api.class);
        reportId = getIntent().getExtras().getString("reportId");
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);
        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        clearButton = (Button) findViewById(R.id.clearButton);
        submitBtn = findViewById(R.id.submitSignBtn);

        clearButton.setEnabled(false);
        submitBtn.setEnabled(false);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Sign Report");
    }


    private void initializeClickEvents() {
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                submitBtn.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                submitBtn.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {

                } else {
                    Toast.makeText(FacultySignAndSubmitReportActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            mSignFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, mSignFile);
            SubmitReport(mSignFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }


    private void SubmitReport(File mSignFile) {
        showProgressDialog("Submitting your report...");
        MultipartBody.Part body = null;
        try {
            // File file = new File(imageString);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSignFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("sign", mSignFile.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        GenerateReportResponse.ReportResponseModel reportResponse = new GenerateReportResponse.ReportResponseModel(reportId, userId);
        Call<ApiResponse> call = api.facultyReportSubmit(reportResponse, body);
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
                            if (msg.equals("Report Submitted Successfully")) {
                                Toast.makeText(FacultySignAndSubmitReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FacultySignAndSubmitReportActivity.this, FacultyHomeFragment.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(FacultySignAndSubmitReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FacultySignAndSubmitReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(FacultySignAndSubmitReportActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(FacultySignAndSubmitReportActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(FacultySignAndSubmitReportActivity.this);
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