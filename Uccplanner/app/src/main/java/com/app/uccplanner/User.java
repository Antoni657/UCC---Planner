package com.app.uccplanner;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {


    String count, day, month, place, title, url, host, time, desc, key, creator;

    public int getCounts() {
        return counts;
    }

    int counts;

    public String getCreator() {
        return creator;
    }

    long timestamp;
    int hour, minute;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public long getTimestamp() {
        return timestamp;
    }

//    public String getFormMonth(){
//
//    Date date = new Date(getTime());
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
//    return monthFormat.format(date);
//}
//public String getFormatDay(){
//    Date date = new Date(timestamp);
//    SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
//    return dayFormat.format(date);
//}
//public String getFormatYear(){
//    Date date = new Date(timestamp);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
//    return yearFormat.format(date);
//}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHost() {
        return host;
    }

    public String getTime() {
        return time;
    }

    public String getDesc() {
        return desc;
    }

    public String getCount() {
        return count;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getPlace() {
        return place;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
