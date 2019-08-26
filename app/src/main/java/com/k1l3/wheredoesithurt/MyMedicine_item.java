package com.k1l3.wheredoesithurt;

import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.Times;

import java.util.ArrayList;

public class MyMedicine_item {
    private String name,start,end;
    private Times alarm;
    private ArrayList<Medicine> medicine_info;
    private Prescription prescription;
    public MyMedicine_item(String start, String end, Times alarm, ArrayList<Medicine> medicine_info) {
        this.start = start;
        this.end = end;
        this.alarm = alarm;
        this.medicine_info = medicine_info;
    }
    public MyMedicine_item(Prescription prescription){
        this.name = prescription.getName();
        this.start = prescription.getBegin();
        this.end = prescription.getEnd();
        this.alarm = prescription.getTimes();
        this.medicine_info =prescription.getMedicines();
        this.prescription = prescription;
    }
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Times getAlarm() {
        return alarm;
    }

    public void setAlarm(Times alarm) {
        this.alarm = alarm;
    }

    public ArrayList<Medicine> getMedicine_info() {
        return medicine_info;
    }

    public void setMedicine_info(ArrayList<Medicine> medicine_info) {
        this.medicine_info = medicine_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
