package com.k1l3.wheredoesithurt.models;

public class Times {
    private String BreakFast;
    private String Lunch;
    private String Dinner;

    public Times() {
    }

    public void setBreakFast(String breakFast) {
        BreakFast = breakFast;
    }

    public void setDinner(String dinner) {
        Dinner = dinner;
    }

    public void setLunch(String lunch) {
        Lunch = lunch;
    }

    public String getBreakFast() {
        return BreakFast;
    }

    public String getDinner() {
        return Dinner;
    }

    public String getLunch() {
        return Lunch;
    }
}
