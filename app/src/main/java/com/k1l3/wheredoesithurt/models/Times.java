package com.k1l3.wheredoesithurt.models;

public class Times {
    private String BreakFast;
    private String Lunch;
    private String Dinner;

    public Times() {
    }

    public String getBreakFast() {
        return BreakFast;
    }

    public void setBreakFast(String breakFast) {
        BreakFast = breakFast;
    }

    public String getDinner() {
        return Dinner;
    }

    public void setDinner(String dinner) {
        Dinner = dinner;
    }

    public String getLunch() {
        return Lunch;
    }

    public void setLunch(String lunch) {
        Lunch = lunch;
    }
}
