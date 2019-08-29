package com.k1l3.wheredoesithurt.calendar;

import com.k1l3.wheredoesithurt.models.Prescription;

import java.util.ArrayList;

public class CalendarItem {
    private ArrayList<String> hashtag;
    private String startDay;
    private String endDay;

    public CalendarItem(Prescription prescription){
        this.hashtag = prescription.getHashTag();
        this.startDay = prescription.getBegin();
        this.endDay = prescription.getEnd();
    }

    public ArrayList<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(ArrayList<String> hashtag) {
        this.hashtag = hashtag;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
}
