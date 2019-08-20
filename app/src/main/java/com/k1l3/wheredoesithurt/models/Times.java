package com.k1l3.wheredoesithurt.models;

import java.util.ArrayList;

public class Times {
    private ArrayList<String> times;

    public Times() {
        times = new ArrayList<>();
    }

    public void addTime(String time){
        times.add(time);
    }

    public ArrayList<String> getTimes(){
        return times;
    }
}
