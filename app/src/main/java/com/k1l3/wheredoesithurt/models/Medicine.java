package com.k1l3.wheredoesithurt.models;

import java.io.Serializable;

public class Medicine implements Serializable {
    private String name;

    private int dailyDosage;
    private int numberOfDoses;
    private int numberOfDay;

    public Medicine() {
    }

    public Medicine(String name, int dailyDosage, int numberOfDoses, int numberOfDay) {
        this.name = name;
        this.dailyDosage = dailyDosage;
        this.numberOfDoses = numberOfDoses;
        this.numberOfDay = numberOfDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
