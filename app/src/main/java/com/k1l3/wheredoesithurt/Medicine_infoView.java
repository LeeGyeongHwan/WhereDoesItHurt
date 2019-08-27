package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Medicine_infoView extends LinearLayout {
    TextView medicine_name, medicine_kind;

    public Medicine_infoView(Context context) {
        super(context);
        init(context);
    }

    public Medicine_infoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.medicine_info, this, true);

        medicine_name = findViewById(R.id.medicine_name);
        medicine_kind = findViewById(R.id.medicine_kind);
    }

    public TextView getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name.setText(medicine_name);
    }

    public TextView getMedicine_kind() {
        return medicine_kind;
    }

    public void setMedicine_kind(String medicine_kind) {
        this.medicine_kind.setText(medicine_kind);
    }
}
