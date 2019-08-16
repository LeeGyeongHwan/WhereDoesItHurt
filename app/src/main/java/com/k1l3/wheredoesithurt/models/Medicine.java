package com.k1l3.wheredoesithurt.models;

import org.jetbrains.annotations.Nullable;

public class Medicine {
    private String name;

    @Nullable
    private int dailyDosage;
    @Nullable
    private int numberOfDoses;
    @Nullable
    private int numberOfDay;

    public Medicine() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
