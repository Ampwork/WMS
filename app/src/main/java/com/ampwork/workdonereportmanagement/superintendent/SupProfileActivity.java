package com.ampwork.workdonereportmanagement.superintendent;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.basic.LoginActivity;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.network.Api;
import com.ampwork.workdonereportmanagement.network.ApiClient;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.FileCompressor;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.amulyakhare.textdrawable.TextDrawable;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupProfileActivity extends AppCompatActivity {

    CircleImageView profileImageView;
    TextView tvFullName, tvUserRole;
    ImageView ivLogout, ivSign;
    TextInputLayout emailLayout, phoneLayout, aboutLayout, oldPasswordLayout, newPasswordLayout;
    TextInputEditText edEmailId, edPhone, edAbout, edOldPassword, edNewPassword;
    MaterialCheckBox changePswCheckBox;
    MaterialButton updateBtn, addSignBtn;
    PreferencesManager preferencesManager;
    TextDrawable drawable;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Api api;
    String userId, fName, lName, email, phone, about, password, profileUrl, signUrl, fcmToken;
    String enteredEmailId, enteredPhone, enteredAbout, enteredOldPassword, enteredNewPassword;
    ProgressDialog progressDialog;

    static final int REQUEST_GALLERY_PHOTO = 1;
    File mPhotoFile;
    FileCompressor mCompressor;

    SignaturePad signaturePad;
    Button saveButton, clearButton;
    File mSignFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_profile);

        initializeViews();
        setData();
        initializeTextWatcher();
        initializeClickEvents();
        //change status bar color
        AppUtility.changeStatusBarColor(this);
    }

    private void initializeViews() {
        preferencesManager = new PreferencesManager(this);
        mCompressor = new FileCompressor(this);
        api = ApiClient.getClient().create(Api.class);

        profileImageView = findViewById(R.id.imageView3);
        tvFullName = findViewById(R.id.tvName);
        tvUserRole = findViewById(R.id.tvRole);
        emailLayout = findViewById(R.id.emailInputLayout);
        edEmailId = findViewById(R.id.emailEd);
        phoneLayout = findViewById(R.id.phoneInputLayout);
        edPhone = findViewById(R.id.phoneEd);
        aboutLayout = findViewById(R.id.aboutInputLayout);
        edAbout = findViewById(R.id.aboutEd);
        changePswCheckBox = findViewById(R.id.chgPwdChkBox);
        oldPasswordLayout = findViewById(R.id.oldPasswordInputLayout);
        edOldPassword = findViewById(R.id.oldPasswordEdt);
        newPasswordLayout = findViewById(R.id.newPasswordInputLayout);
        edNewPassword = findViewById(R.id.newPasswordEdt);
        updateBtn = findViewById(R.id.btnUpdate);
        ivLogout = findViewById(R.id.ivLog);
        addSignBtn = findViewById(R.id.signBtn);
        ivSign = findViewById(R.id.signIv);
    }


    private void setData() {
        userId = preferencesManager.getStringValue(AppConstants.PREF_ID);
        fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);
        about = preferencesManager.getStringValue(AppConstants.PREF_ABOUT);
        email = preferencesManager.getStringValue(AppConstants.PREF_EMAIL);
        phone = preferencesManager.getStringValue(AppConstants.PREF_PHONE);
        password = preferencesManager.getStringValue(AppConstants.PREF_PASSWORD);

        signUrl = preferencesManager.getStringValue(AppConstants.PREF_SIGN_URL);
        profileUrl = preferencesManager.getStringValue(AppConstants.PREF_PROFILE_URL);
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        drawable = TextDrawable.builder().beginConfig()
                .width(150)
                .height(150)
                .bold()
                .endConfig()
                .buildRound(fName.substring(0, 1) + lName.substring(0, 1), color);
        if (!profileUrl.isEmpty()) {
            Picasso.with(this)
                    .load(profileUrl)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(profileImageView);

        } else {
            profileImageView.setImageDrawable(drawable);
        }

        if (signUrl.isEmpty()) {
            addSignBtn.setVisibility(View.VISIBLE);
            ivSign.setVisibility(View.GONE);
        } else {
            addSignBtn.setVisibility(View.GONE);
            ivSign.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(signUrl)
                    .into(ivSign);
        }
        tvFullName.setText(fName + " " + lName);
        tvUserRole.setText(preferencesManager.getStringValue(AppConstants.PREF_ROLE));
        edEmailId.setText(email);
        edPhone.setText(phone);
        edAbout.setText(about);
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
                    phoneLayout.setError(null);
                }

            }
        });

        edAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    aboutLayout.setError(null);
                }

            }
        });
        edNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    edNewPassword.setError(null);
                }

            }
        });
        edOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    oldPasswordLayout.setError(null);
                }

            }
        });


    }


    private void initializeClickEvents() {
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutDialog();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        changePswCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    oldPasswordLayout.setVisibility(View.VISIBLE);
                    newPasswordLayout.setVisibility(View.VISIBLE);
                } else {
                    oldPasswordLayout.setVisibility(View.GONE);
                    newPasswordLayout.setVisibility(View.GONE);
                }
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        addSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignaturePad();
            }
        });
    }

    private void validateData() {

        enteredEmailId = edEmailId.getText().toString();
        enteredPhone = edPhone.getText().toString();
        enteredAbout = edAbout.getText().toString();
        enteredOldPassword = edOldPassword.getText().toString();
        enteredNewPassword = edNewPassword.getText().toString();
        boolean isPasswordChange = false;
        if (enteredEmailId.isEmpty() || !enteredEmailId.matches(emailPattern)) {
            emailLayout.setError(getResources().getString(R.string.email_error_txt));
        } else if (enteredPhone.isEmpty() || enteredPhone.length() < 10) {
            phoneLayout.setError(getResources().getString(R.string.phone_error_txt));
        } else if (enteredAbout.isEmpty()) {
            aboutLayout.setError(getResources().getString(R.string.about_error_txt));
        } else if (changePswCheckBox.isChecked()) {
            if (enteredOldPassword.length() < 6) {
                oldPasswordLayout.setError(getResources().getString(R.string.password_error_txt));
            } else if (enteredNewPassword.length() < 6) {
                newPasswordLayout.setError(getResources().getString(R.string.password_error_txt));
            } else {
                if (enteredEmailId.equalsIgnoreCase(email) && enteredPhone.equalsIgnoreCase(phone)
                        && enteredAbout.equalsIgnoreCase(about) && enteredNewPassword.equalsIgnoreCase(password)) {
                    Toast.makeText(SupProfileActivity.this, "No Data changes found", Toast.LENGTH_SHORT).show();
                } else {
                    UserInfo userInfo = new UserInfo(userId, fName, lName, enteredEmailId, enteredPhone,
                            enteredNewPassword, fcmToken, enteredAbout);
                    isPasswordChange = true;
                    UpdateProfileData(userInfo, isPasswordChange);
                }
            }
        } else {
            if (enteredEmailId.equalsIgnoreCase(email) && enteredPhone.equalsIgnoreCase(phone)
                    && enteredAbout.equalsIgnoreCase(about)) {
                Toast.makeText(SupProfileActivity.this, "No Data changes found", Toast.LENGTH_SHORT).show();
            } else {
                isPasswordChange = false;
                UserInfo userInfo = new UserInfo(userId, fName, lName, enteredEmailId, enteredPhone,
                        password, fcmToken, enteredAbout);

                UpdateProfileData(userInfo, isPasswordChange);
            }
        }
    }

    private void UpdateProfileData(UserInfo userInfo, boolean isPasswordChange) {
        showProgressDialog("Updating profile...");
        Call<ApiResponse> call = api.updateProfile(userInfo);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int statusCode = response.code();
                ApiResponse responseData = response.body();
                String msg = responseData.getMessage();
                boolean status = responseData.isStatus();
                switch (statusCode) {
                    case 200:
                        hideProgressDialog();
                        if (!status) {
                            Toast.makeText(SupProfileActivity.this, "Profile not updated", Toast.LENGTH_SHORT).show();
                        } else {
                            if (msg.equals("Profile Details Updated.!!")) {
                                Toast.makeText(SupProfileActivity.this, "Profile details updated", Toast.LENGTH_SHORT).show();

                                preferencesManager.setStringValue(AppConstants.PREF_EMAIL, enteredEmailId);
                                preferencesManager.setStringValue(AppConstants.PREF_ABOUT, enteredAbout);
                                preferencesManager.setStringValue(AppConstants.PREF_PHONE, enteredPhone);
                                if (isPasswordChange) {
                                    preferencesManager.setStringValue(AppConstants.PREF_PASSWORD, enteredNewPassword);
                                }
                            }
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SupProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logOutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SupProfileActivity.this);
        builder.setTitle("Confirm SignOut");
        builder.setMessage("Are you sure you want to Logout?");
        builder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        preferencesManager.setBooleanValue(AppConstants.PREF_IS_LOGIN, false);
                        Intent i = new Intent(SupProfileActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);
                        finish();
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

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SupProfileActivity.this);
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

                    uploadFile(mPhotoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void uploadFile(File mPhotoFile) {
        showProgressDialog("uploading profile image...");
        MultipartBody.Part body = null;
        try {
            // File file = new File(imageString);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("profile", mPhotoFile.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ApiResponse> call = api.upLoadProfilePic(userId, body);
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
                            if (msg.equals("Profile Pic Updated")) {
                                String path = apiResponse.getPath();
                                loadImage(path);

                                Toast.makeText(SupProfileActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SupProfileActivity.this, "Please select valid file", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SupProfileActivity.this, "Please select valid file", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(SupProfileActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SupProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImage(String path) {
        String url = Api.BASE_URL + path;
        preferencesManager.setStringValue(AppConstants.PREF_PROFILE_URL, url);
        if (!path.isEmpty()) {
            Picasso.with(this)
                    .load(url)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(profileImageView);

        } else {
            profileImageView.setImageDrawable(drawable);
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
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the dialog height and width to screen size
        // dialog.getWindow().setLayout(width, height);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dailog_sign_layout);
        dialog.setCancelable(true);

      /*  //set layout height and width to its screen size
        LinearLayout linearLayout = dialog.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);*/

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

                } else {
                    Toast.makeText(SupProfileActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
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
            uploadSign(mSignFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void uploadSign(File mSignFile) {
        showProgressDialog("uploading signature...");
        MultipartBody.Part body = null;
        try {
            // File file = new File(imageString);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSignFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("sign", mSignFile.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ApiResponse> call = api.upLoadSign(userId, body);
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
                            if (msg.equals("Signature Updated")) {
                                String path = apiResponse.getPath();
                                loadSignUrl(path);

                                Toast.makeText(SupProfileActivity.this, "Signature Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SupProfileActivity.this, "Error occurred try again later", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SupProfileActivity.this, "Error occurred try again later", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 500:
                        hideProgressDialog();
                        Toast.makeText(SupProfileActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(SupProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSignUrl(String path) {
        String url = Api.BASE_URL + path;
        preferencesManager.setStringValue(AppConstants.PREF_SIGN_URL, url);
        if (!path.isEmpty()) {
            ivSign.setVisibility(View.VISIBLE);
            addSignBtn.setVisibility(View.GONE);
            Picasso.with(this)
                    .load(url)
                    .into(ivSign);

        } else {
            //
            addSignBtn.setVisibility(View.VISIBLE);
            ivSign.setVisibility(View.GONE);
        }

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
            progressDialog = new ProgressDialog(SupProfileActivity.this);
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