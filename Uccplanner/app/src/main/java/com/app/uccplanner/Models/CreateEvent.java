package com.app.uccplanner.Models;

public class CreateEvent {

    private long timestamp;

    String day,month, place, title, url, host, time, desc;
//    private String month;
//    private String place;
//    private String title;
//    private String url;
//    private String host;
//    private String time;
//    private String desc;




    public CreateEvent(String host, String desc, String title, String place, long timestamp) {

    }

    public CreateEvent(String host, String desc, String place, String title, String url) {
        this.day = day;
        this.month = month;
        this.place = place;
        this.title = title;
        this.url = url;
        this.host = host;
        this.time = time;
        this.desc = desc;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
