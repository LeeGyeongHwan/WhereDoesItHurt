package com.k1l3.wheredoesithurt.models;

public class Day {
    DayClick dayClick;

    public Day(){
        dayClick = new DayClick();
    }

    public DayClick getDayClick() {
        return dayClick;
    }

    public void setDayClick(DayClick dayClick) {
        this.dayClick = dayClick;
    }
}
