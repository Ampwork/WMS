package com.ampwork.workdonereportmanagement.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.ampwork.workdonereportmanagement.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AppUtility {

    public static void changeStatusBarColor(Activity context){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(context.getResources().getColor(R.color.latestBlue));
        }
    }
    public static String getCurrentDateTime() {
        String datetime = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        datetime = simpleDateFormat.format(new Date());
        return datetime;
    }

    public static String getCurrentDate() {
        String datetime = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        datetime = simpleDateFormat.format(new Date());
        return datetime;
    }

    public static String getCurrentYear() {
        String datetime = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        datetime = simpleDateFormat.format(new Date());
        return datetime;
    }

    public static String getDateMonth(String dateStr) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("MMM");

        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static String getDateFormat(String dateStr) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd");

        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static boolean dateValidation(String date) {
        Boolean result = false;
        String[] array = date.split("-");
        if (array.length == 3) {
            int day = Integer.parseInt(array[0]);
            int month = Integer.parseInt(array[1]);
            int year = Integer.parseInt(array[2]);
            if (day <= 31 && month <= 12 && year > 2020 && year < 2100 && day > 0 && month > 0) {
                return true;
            }
        }

        return result;
    }

    public static String getAge(String dob) {
        int diffMonth = 0;
        String output = "";
      /*  String CurrentDate = "09/24/2018";
        String FinalDate = "09/26/2019";*/
        Date startDate;
        Date currentDate;
        SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy");
        try {
            startDate = dates.parse(dob);
            currentDate = new Date();

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(currentDate);

            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

            if(diffMonth>18){
                int year = diffMonth/12;
                int months = diffMonth%12;

                if(year>1){
                    output = year + " Years";
                }else {
                    output = year + " Year";
                }

            }else {
                if(diffMonth>1){
                    output = diffMonth + " Months";
                }else {
                    output = diffMonth + " Month";
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return String.valueOf(output);

    }


    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static int getNotificationCount(String notification) {
        int result = 1;
        if (!notification.isEmpty()) {
            result = Integer.parseInt(notification) + 1;

        }
        return result;

    }

}
