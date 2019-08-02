package com.k1l3.wheredoesithurt.models;

import java.util.ArrayList;

public class Prescription {
    private int Begin;
    private int End;
    private int Caution;
    private int SideEffect;
    private ArrayList<Medicine> Medicines;

    public Prescription() {

    }

    public int getBegin() {
        return Begin;
    }

    public int getCaution() {
        return Caution;
    }

    public int getEnd() {
        return End;
    }

    public int getSideEffect() {
        return SideEffect;
    }

    public ArrayList<Medicine> getMedicines() {
        return Medicines;
    }

    public void setBegin(int begin) {
        Begin = begin;
    }

    public void setCaution(int caution) {
        Caution = caution;
    }

    public void setEnd(int end) {
        End = end;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        Medicines = medicines;
    }

    public void setSideEffect(int sideEffect) {
        SideEffect = sideEffect;
    }
}
