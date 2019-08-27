package com.k1l3.wheredoesithurt.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Year implements Serializable {
    ArrayList<Month> months;

    public Year(){
        months = new ArrayList<>();
        for(int i=0;i<12;i++){
            months.add(new Month());
        }
    }

    public ArrayList<Month> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<Month> months) {
        this.months = months;
    }
}
