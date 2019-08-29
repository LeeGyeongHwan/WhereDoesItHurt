package com.k1l3.wheredoesithurt.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.k1l3.wheredoesithurt.R;

public class CalendarItemView extends LinearLayout {
    private TextView hashtags;
    private TextView dayCount;
    private TextView wholeDayCount;
    private LinearLayout calendarViewLayout;

    public CalendarItemView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_item, this, true);
        hashtags = findViewById(R.id.calendar_item_hashtags);
        dayCount = findViewById(R.id.calendar_day_count);
        wholeDayCount = findViewById(R.id.calendar_whole_day_count);
    }

    public void setHashtags(String hashtags) {
        this.hashtags.setText(hashtags);
    }

    public void setDayCount(long dayCount) {
        this.dayCount.setText(String.valueOf(dayCount));
    }


    public void setWholeDayCount(long wholeDayCount) {
        this.wholeDayCount.setText(String.valueOf(wholeDayCount));
    }
}
