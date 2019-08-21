package com.k1l3.wheredoesithurt.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Times implements Serializable {
    private ArrayList<String> times;

    public Times() {
        this.times = new ArrayList<>();
    }

    public void addTime(String time){
        times.add(time);
    }

    public ArrayList<String> getTimes(){
        return times;
    }
}
