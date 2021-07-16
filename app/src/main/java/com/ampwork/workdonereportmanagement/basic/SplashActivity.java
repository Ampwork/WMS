package com.ampwork.workdonereportmanagement.basic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.clerk.activities.ClerkHomeActivity;
import com.ampwork.workdonereportmanagement.faculty.activities.FacultyNavigationActivity;
import com.ampwork.workdonereportmanagement.hod.activities.HodHomeActivity;
import com.ampwork.workdonereportmanagement.superintendent.SuperintendentHomeActivity;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    Boolean isLoggedIn;
    PreferencesManager myPref;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myPref = new PreferencesManager(this);
        isLoggedIn = myPref.getBooleanValue(AppConstants.PREF_IS_LOGIN);
        role = myPref.getStringValue(AppConstants.PREF_ROLE);
        Boolean is_from_notification = getIntent().getBooleanExtra("is_from_notification", false);

        List<String> datesList = new ArrayList<>();
        List<String> endDatesList = new ArrayList<>();

        datesList = AppUtility.getDateListOfMonth(6);
        endDatesList = AppUtility.getMonthEndDates(6);

        /*for (String d : datesList){
            for (String b:endDatesList){

                if (d.equals(b))
                {
                    Log.e("insert","....."+b);
                    datesList.remove(b);
                }
            }
            Log.e("insert date","..............."+d);
        }
*/

        for (int i=0;i<datesList.size();i++){
            for (int j=0;j<endDatesList.size();j++){
                if (datesList.get(i).equals(endDatesList.get(j))){
                    Log.e("insert","....."+endDatesList.get(j));
                    datesList.remove(i);
                }
            }
            Log.e("insert date","..............."+datesList.get(i));
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isLoggedIn) {

                    switch (role) {
                        case "faculty":
                            /*if (is_from_notification)
                            {

                            }else {

                                Intent facultyIntent = new Intent(SplashActivity.this, FacultyNavigationActivity.class);
                                facultyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(facultyIntent);
                            }*/
                            Intent facultyIntent = new Intent(SplashActivity.this, FacultyNavigationActivity.class);
                            facultyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(facultyIntent);

                            break;
                        case "program coordinator":
                            if (is_from_notification) {
                                String reportId = getIntent().getStringExtra("reportId");
                                myPref.setStringValue(AppConstants.PREF_HOD_NOTIFICATION_REPORT_ID, reportId);
                                myPref.setBooleanValue(AppConstants.PREF_IS_FROM_NOTIFICATION, true);
                                Intent hodIntent = new Intent(SplashActivity.this, HodHomeActivity.class);
                                hodIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(hodIntent);
                            } else {
                                Intent hodIntent = new Intent(SplashActivity.this, HodHomeActivity.class);
                                hodIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(hodIntent);
                            }
                            break;
                        case "superintendent":
                            if (is_from_notification) {
                                String reportId = getIntent().getStringExtra("reportId");
                                myPref.setStringValue(AppConstants.PREF_SUP_NOTIFICATION_REPORT_ID, reportId);
                                myPref.setBooleanValue(AppConstants.PREF_IS_FROM_NOTIFICATION, true);
                                Intent supIntent = new Intent(SplashActivity.this, SuperintendentHomeActivity.class);
                                supIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(supIntent);
                            } else {
                                Intent supIntent = new Intent(SplashActivity.this, SuperintendentHomeActivity.class);
                                supIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(supIntent);
                            }

                            break;

                        case "clerk":
                            Intent clerkIntent = new Intent(SplashActivity.this, ClerkHomeActivity.class);
                            clerkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(clerkIntent);
                            break;
                    }

                } else {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                    finish();
                }


            }
        }, 1500);

    }
}