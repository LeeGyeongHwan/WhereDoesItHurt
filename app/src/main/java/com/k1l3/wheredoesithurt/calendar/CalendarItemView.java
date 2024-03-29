package com.k1l3.wheredoesithurt.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.k1l3.wheredoesithurt.R;
import com.k1l3.wheredoesithurt.models.DayClick;
import com.k1l3.wheredoesithurt.models.Prescription;

import static android.support.constraint.Constraints.TAG;

public class CalendarItemView extends LinearLayout {
    private ImageView calendarCircle;
    private TextView hashtags;
    private TextView dayCount;
    private TextView wholeDayCount;
    private Context context;
    private LinearLayout linearLayout;
    private LinearLayout detailLinearLayout;

    private View btn1, btn2, btn3;
    private TextView when1, when2, when3, time1, time2, time3, iseat1, iseat2, iseat3;
    private ListView my_medicine_info;
    private CircleProgressBar progressBar;

    public CalendarItemView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_item, this, true);

        hashtags = findViewById(R.id.calendar_item_hashtags);
        dayCount = findViewById(R.id.calendar_day_count);
        wholeDayCount = findViewById(R.id.calendar_whole_day_count);
        calendarCircle = findViewById(R.id.calendar_circle);
        linearLayout = findViewById(R.id.calendar_item_simple);
        detailLinearLayout = findViewById(R.id.calendar_item_detail);

        when1 = findViewById(R.id.calendar_item_detail_when1);
        time1 = findViewById(R.id.calendar_item_detail_time1);
        iseat1 = findViewById(R.id.calendar_item_detail_iseat1);
        when2 = findViewById(R.id.calendar_item_detail_when2);
        time2 = findViewById(R.id.calendar_item_detail_time2);
        iseat2 = findViewById(R.id.calendar_item_detail_iseat2);
        when3 = findViewById(R.id.calendar_item_detail_when3);
        time3 = findViewById(R.id.calendar_item_detail_time3);
        iseat3 = findViewById(R.id.calendar_item_detail_iseat3);
        btn1 = findViewById(R.id.calendar_item_detail_flipbtn1);
        btn2 = findViewById(R.id.calendar_item_detail_flipbtn2);
        btn3 = findViewById(R.id.calendar_item_detail_flipbtn3);

        progressBar = findViewById(R.id.calendar_item_detail_progress_bar);
        my_medicine_info = findViewById(R.id.calendar_item_detail_my_medicine_info);
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

    public void setCalendarCircle(int color) {
        Drawable drawable = context.getDrawable(R.drawable.circle_purple);

        switch (color) {
            case 0:
                drawable = context.getDrawable(R.drawable.circle_yellow);
                break;
            case 1:
                drawable = context.getDrawable(R.drawable.circle_sky);
                break;
            case 2:
                drawable = context.getDrawable(R.drawable.circle_purple);
                break;
        }

        calendarCircle.setImageDrawable(drawable);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public ListView getMy_medicine_info() {
        return my_medicine_info;
    }

    public LinearLayout getDetailLinearLayout() {
        return detailLinearLayout;
    }

    public void setProgressBar(Prescription prescription) {
        int currentClick = prescription.getTotalClick();
        progressBar.setMax(100);

        int currentProgress = (currentClick * 100) /
                (prescription.getMedicines().get(0).getNumberOfDay() *
                        prescription.getMedicines().get(0).getNumberOfDoses());

        progressBar.setProgress(currentProgress);
    }

    public void setButtons(Prescription prescription, String selectedDate) {
        setTimes(prescription);

        setButtonVisibility(prescription);

        String[] time = selectedDate.split("\\.");

        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);

        DayClick dayClick = prescription.getYear()
                .getMonths().get(month - 1)
                .getDays().get(day - 1)
                .getDayClick();

        if (dayClick.getOne() == 0) {
            btn1.setBackgroundResource(R.drawable.flip_purple);
            iseat1.setText("못먹었어요");
            when1.setTextColor(Color.parseColor("#ffffff"));
            time1.setTextColor(Color.parseColor("#ffffff"));
            iseat1.setTextColor(Color.parseColor("#ffffff"));
        } else {
            btn1.setBackgroundResource(R.drawable.flip_white);
            iseat1.setText("먹었어요");
            when1.setTextColor(Color.parseColor("#776DE0"));
            time1.setTextColor(Color.parseColor("#776DE0"));
            iseat1.setTextColor(Color.parseColor("#776DE0"));
        }

        if (dayClick.getTwo() == 0) {
            btn2.setBackgroundResource(R.drawable.flip_purple);
            iseat2.setText("못먹었어요");
            when2.setTextColor(Color.parseColor("#ffffff"));
            time2.setTextColor(Color.parseColor("#ffffff"));
            iseat2.setTextColor(Color.parseColor("#ffffff"));
        } else {
            btn2.setBackgroundResource(R.drawable.flip_white);
            iseat2.setText("먹었어요");
            when2.setTextColor(Color.parseColor("#776DE0"));
            time2.setTextColor(Color.parseColor("#776DE0"));
            iseat2.setTextColor(Color.parseColor("#776DE0"));
        }

        if (dayClick.getThree() == 0) {
            btn3.setBackgroundResource(R.drawable.flip_purple);
            iseat3.setText("못먹었어요");
            when3.setTextColor(Color.parseColor("#ffffff"));
            time3.setTextColor(Color.parseColor("#ffffff"));
            iseat3.setTextColor(Color.parseColor("#ffffff"));
        } else {
            btn3.setBackgroundResource(R.drawable.flip_white);
            iseat3.setText("먹었어요");
            when3.setTextColor(Color.parseColor("#776DE0"));
            time3.setTextColor(Color.parseColor("#776DE0"));
            iseat3.setTextColor(Color.parseColor("#776DE0"));
        }
    }

    public void setTimes(Prescription prescription) {
        for (int i = 0; i < prescription.getTimes().getTimes().size(); ++i) {
            if(i==0){
                time1.setText(prescription.getTimes().getTimes().get(i));
            }else if(i==1){
                time2.setText(prescription.getTimes().getTimes().get(i));
            }else if(i==2){
                time3.setText(prescription.getTimes().getTimes().get(i));
            }
        }
    }

    public void setButtonVisibility(Prescription prescription){
        for (int i = 0; i < prescription.getTimes().getTimes().size(); ++i) {
            if(i==0){
                btn1.setVisibility(VISIBLE);
            }else if(i==1){
                btn2.setVisibility(VISIBLE);
            }else if(i==2){
                btn3.setVisibility(VISIBLE);
            }
        }
    }
}
