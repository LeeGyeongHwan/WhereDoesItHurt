package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Caution_foodView extends LinearLayout {
    TextView medicine_name, food ;
    public Caution_foodView(Context context) {
        super(context);
        init(context);
    }

    public Caution_foodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.caution_food, this, true);
        medicine_name = findViewById(R.id.medicine_name);
        food = findViewById(R.id.food);
    }

    public TextView getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name.setText(medicine_name);
    }

    public TextView getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food.setText(food);
    }
}
