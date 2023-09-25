package com.example.nativeapplication;

import android.util.Log;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CalendarUtils {
    public static int weekNumber(LocalDateTime date) {
        return date.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    /* Returns an ArrayList with the dates of the week */
    public static ArrayList<LocalDateTime> datesOfWeek(LocalDateTime date) {
        ArrayList<LocalDateTime> dates = new ArrayList<>();
        date = date.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            dates.add(date);
            date = date.plusDays(1);
        }
        return dates;
    }
}
