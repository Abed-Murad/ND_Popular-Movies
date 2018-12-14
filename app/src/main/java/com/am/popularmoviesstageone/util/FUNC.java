package com.am.popularmoviesstageone.util;

import android.text.format.DateFormat;

import java.util.Date;

public class FUNC {
    public static String getDateDetails(Date date) {

        String day = (String) DateFormat.format("dd", date);            // 20
        String monthNumber = (String) DateFormat.format("MM", date);    // 06
        String monthString = (String) DateFormat.format("MMM", date);   // Jun
        String year = (String) DateFormat.format("yyyy", date);         // 2013

        return day + " " + monthString + " " + year;


    }
}
