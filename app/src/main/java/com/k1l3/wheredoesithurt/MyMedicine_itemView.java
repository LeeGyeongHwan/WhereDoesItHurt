package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyMedicine_itemView extends LinearLayout {
    private TextView name, name2, date, alarm, medicine_info;
    private LinearLayout my_medicine_layout;
    private ImageButton delete;

    public MyMedicine_itemView(Context context) {
        super(context);
        init(context);
    }

    public MyMedicine_itemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_medicine, this, true);
        name = findViewById(R.id.medicine_name);
        name2 = findViewById(R.id.medicine_name2);
        date = findViewById(R.id.my_medicine_date);
        alarm = findViewById(R.id.my_medicine_alarm);
        medicine_info = findViewById(R.id.my_medicine_info);
        my_medicine_layout = findViewById(R.id.my_medicine_layout);
        delete = findViewById(R.id.delete);
    }

    public TextView getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.setText(date);
    }

    public TextView getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm.setText(alarm);
    }

    public TextView getMedicine_info() {
        return medicine_info;
    }

    public void setMedicine_info(String medicine_info) {
        this.medicine_info.setText(medicine_info);
    }

    public TextView getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2.setText(name2);
    }

    public LinearLayout getMy_medicine_layout() {
        return my_medicine_layout;
    }

    public void setMy_medicine_layout(LinearLayout my_medicine_layout) {
        this.my_medicine_layout = my_medicine_layout;
    }

    public ImageButton getDelete() {
        return delete;
    }

    public void setDelete(ImageButton delete) {
        this.delete = delete;
    }
}
