package com.ampwork.workdonereportmanagement.clerk.activities.faculty;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.ProgramResponse;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.FileCompressor;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFacultyActivity extends AppCompatActivity {

    TextView tvTitle;
    TextInputLayout fNameInputTextLayout, lNameInputTextLayout, emailInputTextLayout, phoneInputTextLayout,
            programInputTextLayout, roleInputTextLayout, passwordInputTextLayout, confirmInputTextLayout;
    TextInputEditText edFName, edLName, edEmail, edPhone, edPassword, edConfirmPassword;
    AutoCompleteTextView autoTvProgram, autoTvRole;
    MaterialButton createBtn, addSignBtn;
    ImageView ivSign, ivProfile;
    ProgressDialog progressDialog;
    RelativeLayout profileImageLayout;
    Api api;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    List<ProgramResponse.ProgramModel> programModels = new ArrayList<>();

    SignaturePad signaturePad;
    Button saveButton, clearButton;
    File mSignFile;

    static final int REQUEST_GALLERY_PHOTO = 1;
    File mPhotoFile;
    FileCompressor mCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        initializeViews();
        initializeToolBar();

        initializeTextWatcher();
        initializeClickEvents();
        getProgramList();

        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }



    private void initializeViews() {
        api = ApiClient.getClient().create(Api.class);
        mCompressor = new FileCompressor(this);


        fNameInputTextLayout = findViewById(R.id.fNameInputLayout);
        edFName = findViewById(R.id.fNameEdt);
        lNameInputTextLayout = findViewById(R.id.lNameInputLayout);
        edLName = findViewById(R.id.lNameEdt);
        emailInputTextLayout = findViewById(R.id.emailInputLayout);
        edEmail = findViewById(R.id.emailEd);
        phoneInputTextLayout = findViewById(R.id.phoneInputLayout);
        edPhone = findViewById(R.id.phoneEd);
        programInputTextLayout = findViewById(R.id.programInputLayout);
        autoTvProgram = findViewById(R.id.programAutoComTv);
        roleInputTextLayout = findViewById(R.id.roleInputLayout);
        autoTvRole = findViewById(R.id.roleAutoComTv);
        passwordInputTextLayout = findViewById(R.id.passwordInputLayout);
        edPassword = findViewById(R.id.passwordEd);
        confirmInputTextLayout = findViewById(R.id.ConfirmPasswordInputLayout);
        edConfirmPassword = findViewById(R.id.ConfirmPasswordEd);
        createBtn = findViewById(R.id.createBtn);

        ivSign = findViewById(R.id.signIv);
        addSignBtn = findViewById(R.id.signBtn);
        ivProfile = findViewById(R.id.profileImage);
        profileImageLayout = findViewById(R.id.profileImageLayout);
    }

    private void initializeToolBar() {
        tvTitle = findViewById(R.id.tvtitle);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Add Faculty");
    }



    private void initializeTextWatcher() {
        edFName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    fNameInputTextLayout.setError(null);
                }

            }
        });
        edLName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lNameInputTextLayout.setError(null);
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
                    emailInputTextLayout.setError(null);
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
                    phoneInputTextLayout.setError(null);
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
                    passwordInputTextLayout.setError(null);
                }

            }
        });
        edConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    confirmInputTextLayout.setError(null);
                }

            }
        });
        autoTvRole.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    roleInputTextLayout.setError(null);
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
                    programInputTextLayout.setError(null);
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

        addSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignaturePad();
            }
        });

        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        ProgramResponse repose = response.body();
                        String msg = repose.getMessage();
                        boolean status = repose.isStatus();
                        if (status) {
                            if (msg.equals("Programs list")) {
                                assert response.body() != null;
                                programModels = response.body().getProgramModels();
                                if (programModels.size() > 0) {
                                    loadPrograms(programModels);
                                }

                            }
                        } else {
                            Toast.makeText(AddFacultyActivity.this, "Programs not found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddFacultyActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProgramResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddFacultyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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

        ArrayAdapter sub_adapter = new ArrayAdapter(AddFacultyActivity.this, R.layout.list_item, subjects);
        autoTvProgram.setAdapter(sub_adapter);

        //load roles
        String[] role = {"faculty", "program coordinator", "superintendent"};
        ArrayAdapter role_adapter = new ArrayAdapter(AddFacultyActivity.this, R.layout.list_item, role);
        autoTvRole.setAdapter(role_adapter);

    }


    private void validateForm() {
        String fName = edFName.getText().toString();
        String lName = edLName.getText().toString();
        String email = edEmail.getText().toString();
        String phone = edPhone.getText().toString();
        String program = autoTvProgram.getText().toString();
        String role = autoTvRole.getText().toString();
        String password = edPassword.getText().toString();
        String confirmPassword = edConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(fName) || fName.length() < 3) {
            fNameInputTextLayout.setError(getResources().getString(R.string.fName_error_txt));
        } else if (TextUtils.isEmpty(lName)) {
            fNameInputTextLayout.setError(getResources().getString(R.string.lName_error_txt));
        } else if (TextUtils.isEmpty(email) || !email.matches(emailPattern)) {
            emailInputTextLayout.setError(getResources().getString(R.string.email_error_txt));
        } else if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            phoneInputTextLayout.setError(getResources().getString(R.string.phone_error_txt));
        } else if (TextUtils.isEmpty(program) || program.equals("Select")) {
            programInputTextLayout.setError(getResources().getString(R.string.select_program_error_txt));
        } else if (TextUtils.isEmpty(role) || role.equals("Select")) {
            roleInputTextLayout.setError(getResources().getString(R.string.role_error_txt));
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordInputTextLayout.setError(getResources().getString(R.string.password_error_txt));
        } else if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            confirmInputTextLayout.setError(getResources().getString(R.string.confirm_psw_error_txt));
        } else {
            UserInfo userInfo = new UserInfo(fName, lName, email, phone, password, role, program);
            if (mSignFile == null && mPhotoFile == null) {
                createFacultyDetails(userInfo);
            } else {
                if (mSignFile != null) {
                    createFacultyDetailsWithImage(fName, lName, email, phone, password, role, program,
                            mSignFile, "sign");
                } else if (mSignFile != null && mPhotoFile != null) {
                    createFacultyDetailsWithSignAndProfile(fName, lName, email, phone, password, role, program,
                            mSignFile, mPhotoFile, "profile", "sign");
                } else {
                    createFacultyDetailsWithImage(fName, lName, email, phone, password, role, program,
                            mSignFile, "profile");
                }

            }

        }
    }

    // save data with profile and sign image
    private void createFacultyDetailsWithSignAndProfile(String fName, String lName, String email,
                                                        String phone, String password, String role,
                                                        String program, File mSignFile, File mPhotoFile,
                                                        String profile,
                                                        String sign) {

        showProgressDialog("Adding faculty details please wait...");
        MultipartBody.Part bodySign = null;
        MultipartBody.Part bodyProfile = null;
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSignFile);
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile);
            bodySign = MultipartBody.Part.createFormData(sign, mSignFile.getName(), requestFile);
            bodyProfile = MultipartBody.Part.createFormData(profile, mPhotoFile.getName(), requestFile1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ApiResponse> call = api.addFacultyWithSignAndImage(fName, lName, email, phone, password, role, program,
                bodySign, bodyProfile);

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
                            if (msg.equals("Record added successfully")) {
                                Toast.makeText(AddFacultyActivity.this, "Faculty details added successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddFacultyActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddFacultyActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddFacultyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddFacultyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // save data with sign / profile image
    private void createFacultyDetailsWithImage(String fName, String lName, String email, String phone,
                                               String password, String role, String program, File mSignFile,
                                               String name) {
        showProgressDialog("Adding faculty details please wait...");
        MultipartBody.Part body = null;
        try {
            // File file = new File(imageString);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSignFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData(name, mSignFile.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ApiResponse> call = api.addFacultyWithImage(fName, lName, email, phone, password, role, program, body);
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
                            if (msg.equals("Record added successfully")) {
                                Toast.makeText(AddFacultyActivity.this, "Faculty details added successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddFacultyActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddFacultyActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddFacultyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddFacultyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // save data without image
    private void createFacultyDetails(UserInfo userInfo) {
        showProgressDialog("Adding faculty details please wait...");
        Call<ApiResponse> call = api.addFaculty(userInfo);
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
                            if (msg.equals("Record added successfully")) {
                                Toast.makeText(AddFacultyActivity.this, "Faculty details added successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddFacultyActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddFacultyActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 500:
                        hideProgressDialog();
                        Toast.makeText(AddFacultyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddFacultyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddFacultyActivity.this);
        builder.setTitle("Choose your picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Choose from Gallery")) {
                    dispatchGalleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
     * Select image from gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();

                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    ivProfile.setImageURI(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;

    }

    private void openSignaturePad() {
        Dialog dialog = new Dialog(AddFacultyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_sign_layout);
        dialog.setCancelable(true);


        signaturePad = (SignaturePad) dialog.findViewById(R.id.signaturePad);
        saveButton = (Button) dialog.findViewById(R.id.saveButton);
        clearButton = (Button) dialog.findViewById(R.id.clearButton);

        //disable both buttons at start
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        //change screen orientation to landscape mode
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveButton.setEnabled(true);
                clearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code for saving the signature here
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    ivSign.setImageBitmap(signatureBitmap);
                } else {
                    Toast.makeText(AddFacultyActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

        dialog.show();
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


    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            mSignFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, mSignFile);

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(AddFacultyActivity.this);
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