package com.ampwork.workdonereportmanagement.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.ampwork.workdonereportmanagement.R;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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

    public static String getDateFormats(String dateStr) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("MMM-dd-yyyy");

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


    public enum MonthDays {

        January(31,1),
        February(28,2),
        March(31,3),
        April(30,4),
        May(31,5),
        June(30,6),
        July(31,7),
        August(31,8),
        September(30,9),
        October(31,10),
        November(30,11),
        December(31,12);

        private int value;
        private int day;

        MonthDays(int day,int value) {
            this.day = day;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public int getDay() {
            return day;
        }
    }

    public static int checkMonth(String month){
        int monthDay;
        switch (month) {
            case "January":
                monthDay = MonthDays.January.getDay();
                break;
            case "February":
                monthDay = MonthDays.February.getDay();
                break;
            case "March":
                monthDay = MonthDays.March.getDay();
                break;
            case "April":
                monthDay = MonthDays.April.getDay();
                break;
            case "May":
                monthDay = MonthDays.May.getDay();
                break;
            case "June":
                monthDay = MonthDays.June.getDay();
                break;
            case "July":
                monthDay = MonthDays.July.getDay();
                break;
            case "August":
                monthDay = MonthDays.August.getDay();
                break;
            case "September":
                monthDay = MonthDays.September.getDay();
                break;
            case "October":
                monthDay = MonthDays.October.getDay();
                break;
            case "November":
                monthDay = MonthDays.November.getDay();
                break;
            default:
                monthDay = MonthDays.December.getDay();
                break;
        }

        return monthDay;
    }

    public static int checkMonthValue(String month){
        int monthDay;
        switch (month) {
            case "January":
                monthDay = MonthDays.January.getValue();
                break;
            case "February":
                monthDay = MonthDays.February.getValue();
                break;
            case "March":
                monthDay = MonthDays.March.getValue();
                break;
            case "April":
                monthDay = MonthDays.April.getValue();
                break;
            case "May":
                monthDay = MonthDays.May.getValue();
                break;
            case "June":
                monthDay = MonthDays.June.getValue();
                break;
            case "July":
                monthDay = MonthDays.July.getValue();
                break;
            case "August":
                monthDay = MonthDays.August.getValue();
                break;
            case "September":
                monthDay = MonthDays.September.getValue();
                break;
            case "October":
                monthDay = MonthDays.October.getValue();
                break;
            case "November":
                monthDay = MonthDays.November.getValue();
                break;
            default:
                monthDay = MonthDays.December.getValue();
                break;
        }

        return monthDay;
    }

    public  static int getNumberOfWeeks(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month);
        int maxWeeknumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return maxWeeknumber;
    }

    public static String getDateYear(String dateStr) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy");

        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }


    public static void dat() {
        List<String> monthDates = new ArrayList<>();
        int month = 7;
        int value = month-1;
        /*Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, value);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 1; i < maxDay; i++)
        {
            cal.set(Calendar.DAY_OF_MONTH, i);
            monthDates.add(df.format(cal.getTime()));
        }*/


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        List<List<String>> weekdates = new ArrayList<List<String>>();
        List<String> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2021);
        c.set(Calendar.MONTH, value);
        c.set(Calendar.DAY_OF_MONTH, 1);
        while (c.get(Calendar.MONTH) == value) {
            dates = new ArrayList<String>();
            while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                c.add(Calendar.DAY_OF_MONTH, -1);
            }
            dates.add(format.format(c.getTime()));

            c.add(Calendar.DAY_OF_MONTH, 6);
            dates.add(format.format(c.getTime()));

            weekdates.add(dates);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }


        List<String> endDates = getEndDate(weekdates);

        Log.e("end","dates........"+endDates);
    }


    public static String getStartDate (List<List<String>> weekdates ){
        String startDate = "";
        for(List<String> csv : weekdates)
        {
            //dumb logic to place the commas correctly
            if(!csv.isEmpty())
            {
                startDate = csv.get(0);

            }
        }
        return startDate;
    }

    public static List<String> getEndDate (List<List<String>> weekdates ){
        List<String> dates = new ArrayList<>();
        String endDate = "";
        for(List<String> csv : weekdates)
        {
            //dumb logic to place the commas correctly
            if(!csv.isEmpty())
            {
                for(int i=1; i < csv.size(); i++)
                {
                    endDate = csv.get(i);
                    dates.add(endDate);
                }

            }
        }
        return dates;
    }

    public static  void getNextDates(String dates){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dates));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
       // cal.set(Calendar.DAY_OF_WEEK, 7);
        Log.e("break","........................."+sdf.format(cal.getTime()));

    }

    public static  List<String> getMonthEndDates(int value){

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        List<List<String>> weekdates = new ArrayList<List<String>>();
        List<String> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2021);
        c.set(Calendar.MONTH, value);
        c.set(Calendar.DAY_OF_MONTH, 1);
        while (c.get(Calendar.MONTH) == value) {
            dates = new ArrayList<String>();
            while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                c.add(Calendar.DAY_OF_MONTH, -1);
            }
            dates.add(format.format(c.getTime()));

            c.add(Calendar.DAY_OF_MONTH, 6);
            dates.add(format.format(c.getTime()));

            weekdates.add(dates);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }


        List<String> endDates = getEndDate(weekdates);

        Log.e("end","dates........"+endDates);

        return endDates;
    }

    public static  List<String> getDateListOfMonth(int value){
        List<String> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, value);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 1; i < maxDay; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i);
            String dateStr = df.format(cal.getTime());
            dates.add(dateStr);
        }
        return dates;
    }

}
