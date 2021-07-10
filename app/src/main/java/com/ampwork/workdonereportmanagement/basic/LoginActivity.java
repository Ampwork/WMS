package com.ampwork.workdonereportmanagement.basic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.activities.ClerkHomeActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.FacultyNavigationActivity;
import com.ampwork.workdonereportmanagement.hod.activities.HodHomeActivity;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.superintendent.SuperintendentHomeActivity;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailLayout, passwordLayout;
    TextInputEditText edEmailId, edPassword;
    MaterialButton loginBtn;
    Api api;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String fcmToken, email, password;
    PreferencesManager preferencesManager;
    ProgressDialog progressDialog;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        CheckPermission();
        initializeTextWatcher();
        initializeClickEvents();
    }


    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        preferencesManager = new PreferencesManager(this);

        emailLayout = findViewById(R.id.emailInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);

        edEmailId = findViewById(R.id.emailEd);
        edPassword = findViewById(R.id.passwordEd);
        loginBtn = findViewById(R.id.loginBtn);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                fcmToken = instanceIdResult.getToken();
            }
        });
    }


    private void initializeTextWatcher() {
        edEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    emailLayout.setError(null);
                }

            }
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    passwordLayout.setError(null);
                }

            }
        });
    }

    private void initializeClickEvents() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edEmailId.getText().toString();
                password = edPassword.getText().toString();
                if (email.isEmpty() && !email.matches(emailPattern)) {
                    emailLayout.setError(getResources().getString(R.string.email_error_txt));
                } else if (password.length() < 6) {
                    passwordLayout.setError(getResources().getString(R.string.password_error_txt));
                } else {
                    showProgressDialog("Verifying login details...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            verifyLogin();
                        }
                    }, 800);

                }
            }
        });
    }

    private void verifyLogin() {

        UserInfo userInfo = new UserInfo(email, password, fcmToken);
        Call<ApiResponse> call = api.login(userInfo);
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
                        if (!status) {
                            if (msg.equals("Invalid Credentials")) {
                                Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            UserInfo userInfoRes = apiResponse.getUserInfo();

                            if (msg.equals("Logged In Successfully") && userInfoRes != null) {
                                preferencesManager.setBooleanValue(AppConstants.PREF_IS_LOGIN, true);
                                preferencesManager.setStringValue(AppConstants.PREF_ID, userInfoRes.getUserId());
                                preferencesManager.setStringValue(AppConstants.PREF_FIRST_NAME, userInfoRes.getFirstName());
                                preferencesManager.setStringValue(AppConstants.PREF_LAST_NAME, userInfoRes.getLastName());
                                preferencesManager.setStringValue(AppConstants.PREF_EMAIL, userInfoRes.getEmail());
                                preferencesManager.setStringValue(AppConstants.PREF_PHONE, userInfoRes.getPhone());
                                preferencesManager.setStringValue(AppConstants.PREF_PASSWORD, userInfoRes.getPassword());
                                preferencesManager.setStringValue(AppConstants.PREF_ROLE, userInfoRes.getRole());
                                preferencesManager.setStringValue(AppConstants.PREF_PROGRAM, userInfoRes.getProgram());

                                preferencesManager.setStringValue(AppConstants.PREF_ABOUT, userInfoRes.getAbout());
                                preferencesManager.setStringValue(AppConstants.PREF_FCM_TOKEN, fcmToken);
                                if (!TextUtils.isEmpty(userInfoRes.getProfileUrl())) {
                                    String path = Api.BASE_URL + userInfoRes.getProfileUrl();
                                    preferencesManager.setStringValue(AppConstants.PREF_PROFILE_URL, path);
                                } else {
                                    preferencesManager.setStringValue(AppConstants.PREF_PROFILE_URL, userInfoRes.getProfileUrl());
                                }

                                if (!TextUtils.isEmpty(userInfoRes.getSignUrl())) {
                                    String path = Api.BASE_URL + userInfoRes.getSignUrl();
                                    preferencesManager.setStringValue(AppConstants.PREF_SIGN_URL, path);
                                } else {
                                    preferencesManager.setStringValue(AppConstants.PREF_SIGN_URL, userInfoRes.getSignUrl());
                                }
                                switch (userInfoRes.getRole()) {
                                    case "faculty":
                                        Intent facultyIntent = new Intent(LoginActivity.this, FacultyNavigationActivity.class);
                                        facultyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(facultyIntent);
                                        break;
                                    case "program coordinator":
                                        Intent hodIntent = new Intent(LoginActivity.this, HodHomeActivity.class);
                                        hodIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(hodIntent);
                                        break;
                                    case "superintendent":
                                        Intent supIntent = new Intent(LoginActivity.this, SuperintendentHomeActivity.class);
                                        supIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(supIntent);
                                        break;

                                    case "clerk":
                                        Intent clerkIntent = new Intent(LoginActivity.this, ClerkHomeActivity.class);
                                        clerkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(clerkIntent);
                                        break;
                                }
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();

                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Log.e("fail","........"+t.getMessage());
                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckPermission() {
        if (CheckingPermissionIsEnabledOrNot()) {

        } else {
            RequestMultiplePermission();

        }
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);
    }


    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean WriteExternalPermisssion = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (WriteExternalPermisssion && ReadExternalPermission) {

                        Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();

                    } else {

                    }
                }

                break;
        }
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(LoginActivity.this);
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