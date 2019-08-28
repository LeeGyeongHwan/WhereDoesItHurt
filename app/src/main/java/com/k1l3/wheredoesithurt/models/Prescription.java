package com.k1l3.wheredoesithurt.models;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class Prescription implements Serializable {
    private String begin;
    private String end;
    private String name;
    @Nullable
    private int caution;
    @Nullable
    private int sideEffect;
    private ArrayList<Medicine> medicines;
    private ArrayList<String> hashTag;
    private Times times;
    private String memo;
    private int totalClick;

    private Year year;
    private boolean isVisible = false;

    public Prescription() {
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public int getCaution() {
        return caution;
    }

    public void setCaution(int caution) {
        this.caution = caution;
    }

    public Times getTimes() {
        return times;
    }

    public void setTimes(Times times) {
        this.times = times;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(int sideEffect) {
        this.sideEffect = sideEffect;
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }

    public ArrayList<String> getHashTag() {
        return hashTag;
    }

    public void setHashTag(@Nullable ArrayList<String> hashTag) {
        this.hashTag = hashTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getTotalClick() {
        return totalClick;
    }

    public void setTotalClick(int totalClick) {
        this.totalClick = totalClick;
    }

    public Year getYear() {
        if(year == null)
            year = new Year();
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }
}
