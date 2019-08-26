package com.k1l3.wheredoesithurt.models;

import java.io.Serializable;

public class Medicine implements Serializable {
    private String name, kind;

    private int dailyDosage;
    private int numberOfDoses;
    private int numberOfDay;

    private int caution;

    public Medicine() {
    }

    public Medicine(String name, int dailyDosage, int numberOfDoses, int numberOfDay, String kind, int caution) {
        this.name = name;
        this.dailyDosage = dailyDosage;
        this.numberOfDoses = numberOfDoses;
        this.numberOfDay = numberOfDay;
        this.kind = kind;
        this.caution = caution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getCaution() {
        return caution;
    }

    public void setCaution(int caution) {
        this.caution = caution;
    }
}
