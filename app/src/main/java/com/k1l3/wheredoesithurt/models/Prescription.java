package com.k1l3.wheredoesithurt.models;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Prescription {
    private String begin;
    private int end;
    private int caution;
    private int sideEffect;
    private ArrayList<Medicine> medicines;
    private ArrayList<String> hashTag;
    private boolean isVisible = false;

    public Prescription() {
    }

    public String getBegin() {
        return begin;
    }

    public int getCaution() {
        return caution;
    }

    public int getEnd() {
        return end;
    }

    public int getSideEffect() {
        return sideEffect;
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public ArrayList<String> getHashTag() {
        return hashTag;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public void show(){
        isVisible = true;
    }

    public void hide(){
        isVisible = false;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setCaution(int caution) {
        this.caution = caution;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }

    public void setSideEffect(int sideEffect) {
        this.sideEffect = sideEffect;
    }

    public void setHashTag(@Nullable ArrayList<String> hashTag) {
        this.hashTag = hashTag;
    }
}
