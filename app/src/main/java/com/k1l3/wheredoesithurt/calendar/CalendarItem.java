package com.k1l3.wheredoesithurt.calendar;

import com.k1l3.wheredoesithurt.models.Prescription;

import java.util.ArrayList;

public class CalendarItem {
    private ArrayList<String> hashtag;
    private String startDay;
    private String endDay;
    private Prescription prescription;
    private int index;

    public CalendarItem(Prescription prescription){
        this.prescription = prescription;
        this.hashtag = prescription.getHashTag();
        this.startDay = prescription.getBegin();
        this.endDay = prescription.getEnd();
    }
    public CalendarItem(Prescription prescription,int index){
        this(prescription);
        this.index = index;
    }

    public ArrayList<String> getHashtag() {
        return hashtag;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public int getIndex() {
        return index;
    }
    public Prescription getPrescription(){
        return prescription;
    }
}
