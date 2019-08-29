package com.k1l3.wheredoesithurt;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class History_item {
    String history_day;
    ArrayList<String> hash_tag = new ArrayList<>();
    ArrayList<String> medicine = new ArrayList<>();
    int count = 0;
    Bitmap bitmap;
    public History_item(String history_day, ArrayList<String> hash_tag, ArrayList<String> medicine) {
        this.history_day = history_day;
        this.hash_tag = hash_tag;
        this.medicine = medicine;
    }

    public String getHistory_day() {
        return history_day;
    }

    public void setHistory_day(String history_day) {
        this.history_day = history_day;
    }

    public ArrayList<String> getHash_tag() {
        return hash_tag;
    }

    public void setHash_tag(ArrayList<String> hash_tag) {
        this.hash_tag = hash_tag;
    }

    public ArrayList<String> getMedicine() {
        return medicine;
    }

    public void setMedicine(ArrayList<String> medicine) {
        this.medicine = medicine;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
