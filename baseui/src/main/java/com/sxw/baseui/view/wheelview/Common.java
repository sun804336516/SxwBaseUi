package com.sxw.baseui.view.wheelview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Common {

    public static Integer[] months_big = {1, 3, 5, 7, 8, 10, 12};
    public static Integer[] months_small = {4, 6, 9, 11};

    public static long getDateyyyyMMdd2LongTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static int getCurrentSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }

    //-------------------------------初始化年----------------------------------
    public static ArrayList<String> buildYear() {
        ArrayList<String> yearList = new ArrayList<>();
        int year = getCurrentYear();
        for (int i = year; i < 2099; i++) {
            yearList.add(i + "");
        }
        return yearList;
    }

    //-------------------------------初始化年----------------------------------
    public static ArrayList<String> buildYear1970() {
        ArrayList<String> yearList = new ArrayList<>();
        int year = 1970;
        for (int i = year; i < 2099; i++) {
            yearList.add(i + "");
        }
        return yearList;
    }

    //-------------------------------初始化月份----------------------------------
    public static ArrayList<String> buildMonth(int year) {
        if (getCurrentYear() >= year) {
            return buildMonthCurrent();
        } else {
            return buildMonthOther();
        }
    }

    /**
     * 当前年的月份
     *
     * @return
     */
    public static ArrayList<String> buildMonthCurrent() {
        ArrayList<String> monthList = new ArrayList<>();
        int month = getCurrentMonth();
        for (int i = month; i < 13; i++) {
            monthList.add(i + "");
        }
        return monthList;
    }

    /**
     * 之后年的月份
     *
     * @return
     */
    public static ArrayList<String> buildMonthOther() {
        ArrayList<String> monthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            monthList.add(i + "");
        }
        return monthList;
    }

    /**
     * 之后年的月份
     *
     * @return
     */
    public static ArrayList<String> buildMonthOther1() {
        ArrayList<String> monthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                monthList.add("0" + i);
            } else {
                monthList.add(i + "");
            }
        }
        return monthList;
    }

    //-------------------------------初始化日----------------------------------
    public static ArrayList<String> buildDay(int year, int month) {
        if (getCurrentYear() >= year && getCurrentMonth() >= month) {
            return buildDayCurrent();
        } else {
            return buildDayOther(year, month);
        }
    }

    public static ArrayList<String> buildDayCurrent() {
        ArrayList<String> dayList = new ArrayList<>();
        int endDay = getEndDay(getCurrentYear(), getCurrentMonth());
        int day = getCurrentDay();
        for (int i = day; i <= endDay; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }

    public static ArrayList<String> buildDay() {
        ArrayList<String> dayList = new ArrayList<>();
        int endDay = getEndDay(getCurrentYear(), getCurrentMonth());
        for (int i = 1; i <= endDay; i++) {
            if (i < 10) {
                dayList.add("0" + i);
            } else {
                dayList.add("" + i);
            }
        }
        return dayList;
    }

    public static ArrayList<String> buildDayOther(int year, int month) {
        ArrayList<String> dayList = new ArrayList<>();
        int endDay = getEndDay(year, month);
        for (int i = 1; i <= endDay; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }

    public static ArrayList<String> buildDayOther1(int year, int month) {
        ArrayList<String> dayList = new ArrayList<>();
        int endDay = getEndDay(year, month);
        for (int i = 1; i <= endDay; i++) {
            if(i < 10){
                dayList.add("0" + i);
            }else{
                dayList.add(i + "");
            }
        }
        return dayList;
    }

    public static int getEndDay(int year, int month) {
        int endDay;
        List<Integer> monthBig = Arrays.asList(months_big);
        if (monthBig.contains(month)) {
            endDay = 31;
        } else if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                endDay = 28;
            } else {
                endDay = 29;
            }
        } else {
            endDay = 30;
        }
        return endDay;
    }

    //-------------------------------初始化时----------------------------------
    public static ArrayList<String> buildHour(int year, int month, int day) {
        if (getCurrentYear() >= year && getCurrentMonth() >= month && getCurrentDay() >= day) {
            return buildHourCurrent();
        } else {
            return buildHorOther();
        }
    }

    public static ArrayList<String> buildHorOther() {
        ArrayList<String> hourList = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hourList.add(i + "");
        }
        return hourList;
    }

    public static ArrayList<String> buildHourCurrent() {
        ArrayList<String> hourList = new ArrayList<>();
        int hour = getCurrentHour();
        for (int i = hour; i <= 23; i++) {
            hourList.add(i + "");
        }
        return hourList;
    }

    public static ArrayList<String> buildHour() {
        ArrayList<String> hourList = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hourList.add("0" + i);
            } else {
                hourList.add(i + "");
            }
        }
        return hourList;
    }

    //-------------------------------初始化分----------------------------------
    public static ArrayList<String> buildMinute(int year, int month, int day, int hour) {
        if (getCurrentYear() >= year && getCurrentMonth() >= month && getCurrentDay() >= day && getCurrentHour() >= hour) {
            return buildMinuteCurrent();
        } else {
            return buildMinuteOther();
        }
    }

    public static ArrayList<String> buildMinuteOther() {
        ArrayList<String> minuteList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minuteList.add(i + "");
        }
        return minuteList;
    }

    public static ArrayList<String> buildMinuteCurrent() {
        ArrayList<String> minuteList = new ArrayList<>();
        int minute = getCurrentMinute();
        for (int i = minute; i <= 59; i++) {
            minuteList.add(i + "");
        }
        return minuteList;
    }

    public static ArrayList<String> buildMinute() {
        ArrayList<String> minuteList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                minuteList.add("0" + i);
            } else {
                minuteList.add(i + "");

            }
        }
        return minuteList;
    }

    //-------------------------------初始化秒----------------------------------
    public static ArrayList<String> buildSecond(int year, int month, int day, int hour, int minute) {
        if (getCurrentYear() >= year && getCurrentMonth() >= month && getCurrentDay() >= day && getCurrentHour() >= hour && getCurrentMinute() >= minute) {
            return buildSecondCurrent();
        } else {
            return buildSecondOther();
        }
    }

    public static ArrayList<String> buildSecondOther() {
        ArrayList<String> secondList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            secondList.add(i + "");
        }
        return secondList;
    }

    public static ArrayList<String> buildSecondOther1() {
        ArrayList<String> secondList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                secondList.add("0" + i);
            } else {
                secondList.add(i + "");

            }
        }
        return secondList;
    }

    public static ArrayList<String> buildSecondCurrent() {
        ArrayList<String> secondteList = new ArrayList<>();
        int second = getCurrentSecond();
        for (int i = second; i <= 59; i++) {
            secondteList.add(i + "");
        }
        return secondteList;
    }
}
