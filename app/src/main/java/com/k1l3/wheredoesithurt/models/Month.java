package com.k1l3.wheredoesithurt.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Month implements Serializable {
    ArrayList<Day> days;

    public Month() {
        days = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            days.add(new Day());
        }
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }
}
