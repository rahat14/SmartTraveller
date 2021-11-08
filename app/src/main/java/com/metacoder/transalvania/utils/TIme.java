package com.metacoder.transalvania.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TIme {
    private final static String prefix = "";
    private final static String suffix = "";
    private final  static String time_ago_seconds = "Just Now";
    private final  static String time_ago_minute = "a minute ago";
    private final  static String time_ago_minutes = "%d minutes ago";
    private final  static String time_ago_hour = "about an hour ago";
    private final  static String time_ago_hours = "%d hours ago";
    private final static String time_ago_day = " a day ago";
    private final  static String time_ago_days = "%d days ago";
    private final  static String time_ago_month = "about a month ago";
    private final  static String time_ago_months = "%d months ";
    private final  static String time_ago_year = "about a year ";
    private final  static String time_ago_years = "%d years ";

    public TIme() {
    }

    public static String From(long millis) {
        long diff = new Date().getTime() - millis;
        double seconds = Math.abs(diff) / 1000;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        double years = days / 365;

        String words;

        if (seconds < 45) {
            words = String.format(time_ago_seconds, Math.round(seconds));
        } else if (seconds < 90) {
            words = String.format(time_ago_minute, 1);
        } else if (minutes < 45) {
            words = String.format(time_ago_minutes, Math.round(minutes));
        } else if (minutes < 90) {
            words = String.format(time_ago_hour, 1);
        } else if (hours < 24) {

            long  wor = Math.round(hours) ;

            words = String.format(time_ago_hours, wor);


        } else if (hours < 42) {
            words = String.format(time_ago_day, 1);
        } else if (days < 30) {
            words = String.format(time_ago_days, Math.round(days));
        } else if (days < 45) {
            words = String.format(time_ago_month, 1);
        } else if (days < 365) {
            words = String.format(time_ago_months, Math.round(days / 30));
        } else if (years < 1.5) {
            words = String.format(time_ago_year, 1);
        } else {
            words = String.format(time_ago_years, Math.round(years));
        }

        StringBuilder sb = new StringBuilder();

        if (prefix.length() > 0) {
            sb.append(prefix).append(" ");
        }

        sb.append(words);

        if (suffix.length() > 0) {
            sb.append(" ").append(suffix);
        }

        return sb.toString().trim();
    }
    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
