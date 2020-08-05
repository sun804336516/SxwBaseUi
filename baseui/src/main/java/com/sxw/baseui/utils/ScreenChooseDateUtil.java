package com.sxw.baseui.utils;


import com.sxw.baseui.view.wheelview.Common;

import java.util.ArrayList;

/**
 * 作者：sxw on 2019/8/7/007 20:06
 */
public class ScreenChooseDateUtil {

    public static ArrayList<String> buildYear2Current(int year) {
        if (year < 1970) {
            year = 1970;
        }
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = year; i < Common.getCurrentYear() + 1; i++) {
            yearList.add(i + "");
        }
        return yearList;
    }

//    public static ArrayList<String> buildMonth() {
//        ArrayList<String> monthList = new ArrayList<>();
//        for (int i = 1; i < 13; i++) {
//            monthList.add(i + "");
//        }
//        return monthList;
//    }

    public static ArrayList<String> buildMonth(int year) {
        if (Common.getCurrentYear() == year) {
            return buildMonth2Current();
        } else {
            return Common.buildMonthOther();
        }
    }

    public static ArrayList<String> buildMonth2Current() {
        ArrayList<String> monthList = new ArrayList<>();
        int month = Common.getCurrentMonth();
        for (int i = 1; i <= month; i++) {
            monthList.add(i + "");
        }
        return monthList;
    }

//    public static ArrayList<String> buildDay() {
//        ArrayList<String> dayList = new ArrayList<>();
////        int endDay = Common.getEndDay(Common.getCurrentYear(), Common.getCurrentMonth());
//        for (int i = 1; i <= 31; i++) {
//            dayList.add(i + "");
//        }
//        return dayList;
//    }

    public static ArrayList<String> buildDay(int year, int month) {
        if (Common.getCurrentYear() == year && Common.getCurrentMonth() == month) {
            return buildDay2Current();
        } else {
            return Common.buildDayOther(year, month);
        }
    }

    public static ArrayList<String> buildDay2Current() {
        ArrayList<String> dayList = new ArrayList<>();
        int day = Common.getCurrentDay();
        for (int i = 1; i <= day; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }
}
