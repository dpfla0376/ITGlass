package com.example.yelim.it_glass;

/**
 * Created by Yelim on 2017-05-28.
 */

public class Record {
    private String fullDate;
    private String record;
    private String year;
    private String month;
    private String day;
/*
    public Record(String fullDate, String record) {
        this.fullDate = fullDate;
        this.record = record;
        parsingDate(this.fullDate);
    }
    */
    public Record(String day, String record) {
        this.day = day;
        this.record = record;
    }

    public Record(String year, String month, String day, String record) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.record = record;
    }

    public Record(String fullDate) {
        this.fullDate = fullDate;
        this.record = null;
        String[] temp = parsingDate(this.fullDate);
        year = temp[0];
        month = temp[1];
        day = temp[2];
    }

    public Record(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.record = null;
    }

    public static String[] parsingDate(String fullDate) {
        String[] temp = fullDate.split("/");
        return temp;
    }

    public String getRecord() {
        return record;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "year : " + year + " / month : " + month + " / day : " + day + " / record : " + record;
    }
}
