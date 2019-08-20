package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class defaultTimeItemView extends LinearLayout {
    TextView AMPM,Time;
    public defaultTimeItemView(Context context) {
        super(context);
        init(context);
    }

    public defaultTimeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.default_time_item, this, true);
        AMPM = (TextView) findViewById(R.id.AMPM);
        Time = (TextView)findViewById(R.id.Time);
    }

    public TextView getAMPM() {
        return AMPM;
    }

    public void setAMPM(String AMPM) {
        this.AMPM.setText(AMPM);
    }

    public TextView getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time.setText(time);
    }
}
