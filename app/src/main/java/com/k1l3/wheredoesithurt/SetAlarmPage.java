package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.Times;
import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;

public class SetAlarmPage extends AppCompatActivity {
    final private ArrayList<TextView> arrayText = new ArrayList<>();
    final private ArrayList<TimePicker> arrayPicker = new ArrayList<>();
    final private ArrayList<ImageView> arrayImg = new ArrayList<>();
    final private ArrayList<LinearLayout> arrayLinear = new ArrayList<>();
    final private ArrayList<Button> arrayBtn = new ArrayList<>();
    public LinearLayout from_linear, to_linear, from_contain, to_contain;
    private Button nextBtn;
    private ImageButton cancelBtn;
    private TextView title, alarm_num, from_dateText, to_dateText;
    private ImageView from_img, to_img;
    private DatePicker from_date, to_date;
    private String id;
    private static final User user = User.getInstance();
    private Prescription prescription;
    private Times times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_page);

        nextBtn = findViewById(R.id.alarm_nextpage);
        cancelBtn = findViewById(R.id.cancelalarm);
        title = findViewById(R.id.title_alarm);
        from_img = findViewById(R.id.from_imgBtn);
        to_img = findViewById(R.id.to_imBtn);
        from_linear = findViewById(R.id.from_datelinear);
        to_linear = findViewById(R.id.to_datelinear);
        alarm_num = findViewById(R.id.alarm_num);
        from_date = findViewById(R.id.from_datepicker);
        to_date = findViewById(R.id.to_datepicker);
        from_dateText = findViewById(R.id.from_dateText);
        to_dateText = findViewById(R.id.to_dateText);
        from_contain = findViewById(R.id.from_linear);
        to_contain = findViewById(R.id.to_linear);

        Intent intent = getIntent();
        String title_pre = intent.getStringExtra("title");
        String alarmnum = intent.getStringExtra("alarmcount");
        String alarmday = intent.getStringExtra("alarmday");
        id = intent.getStringExtra("id");
        prescription = (Prescription) intent.getSerializableExtra("prescription");
        times = new Times();

        ArrayList<String> defaultTime = user.getUserInfo().getDefaultTimes().getTimes();

        alarm_num.setText(alarmnum + "회씩");

        initiate_datepicker(Integer.parseInt(alarmday));

        for (int i = 0; i < Integer.parseInt(alarmnum); i++) {
            LinearLayout linearLayout = findViewById(R.id.startinflatealarm);
            String[] time = {"AM 08:30", "PM 12:30", "PM 06:30"};

            LayoutInflater layoutInflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layoutInflater.inflate(R.layout.add_alarm, linearLayout, true);
            final TextView textView = findViewById(R.id.extext);
            final TimePicker picker = findViewById(R.id.timepicker);
            final ImageView pickerBtn = findViewById(R.id.pickerBtn);
            final LinearLayout picker_linear = findViewById(R.id.pickerlinear);
            final Button confirmBtn = findViewById(R.id.confirm_timeBtn);


            if( i<defaultTime.size()){
                String deTime = defaultTime.get(i);
                int hour = Integer.parseInt(deTime.substring(0,2));
                int min = Integer.parseInt(deTime.substring(2,4));


                String ampm = "AM";
                if(hour>12){
                    hour=hour-12;
                    ampm = "PM";
                }
                else if(hour==0)
                    hour = 12;
                else if(hour==12)
                    ampm ="PM";

                if (ampm.equals("AM")) {
                    picker.setHour(hour);
                    picker.setMinute(min);
                } else {
                    picker.setHour(hour + 12);
                    if (hour==12)
                        picker.setHour(hour);
                    picker.setMinute(min);
                }


                String shour = Integer.toString(hour);
                String smin = Integer.toString(min);
                if (hour < 10)
                    shour = "0" + shour;
                if (min < 10)
                    smin = "0" + smin;
                String timenow = ampm + " " + shour + ":" + smin;
                textView.setText(timenow);

            }else{
                int hour = picker.getHour();
                int min = picker.getMinute();
                String ampm = "AM";
                if(hour>12){
                    hour=hour-12;
                    ampm = "PM";
                }
                else if(hour==0)
                    hour = 12;
                else if(hour==12)
                    ampm ="PM";

                String shour = Integer.toString(hour);
                String smin = Integer.toString(min);
                if (hour < 10)
                    shour = "0" + shour;
                if (min < 10)
                    smin = "0" + smin;
                String timenow = ampm + " " + shour + ":" + smin;
                textView.setText(timenow);

            }
            /*
            if (i < time.length) {
                textView.setText(time[i]);
                String ampm = time[i].substring(0, 2);
                String hour = time[i].substring(3, 5);
                String min = time[i].substring(6, 8);
                if (ampm.equals("AM")) {
                    picker.setHour(Integer.parseInt(hour));
                    picker.setMinute(Integer.parseInt(min));
                } else {
                    picker.setHour(Integer.parseInt(hour) + 12);
                    if (hour.equals("12"))
                        picker.setHour(Integer.parseInt(hour));
                    picker.setMinute(Integer.parseInt(min));
                }
            } else {
                int hour = picker.getHour();
                int min = picker.getMinute();
                String ampm;
                if (hour > 12) { // 0= am 12시 12는 pm 12시
                    ampm = "PM";
                    hour = hour - 12;
                } else if (hour == 12) {
                    ampm = "PM";
                } else if (hour == 0) {
                    ampm = "AM";
                    hour = 12;
                } else {
                    ampm = "AM";
                }
                String shour = Integer.toString(hour);
                String smin = Integer.toString(min);
                if (hour < 10)
                    shour = "0" + shour;
                if (min < 10)
                    smin = "0" + smin;
                String timenow = ampm + " " + shour + ":" + smin;
                textView.setText(timenow);
            }
            */

            textView.setId(500 + i);
            picker.setId(600 + i);
            pickerBtn.setId(700 + i);
            picker_linear.setId(800 + i);
            confirmBtn.setId(900 + i);
            arrayText.add(textView);
            arrayPicker.add(picker);
            arrayImg.add(pickerBtn);
            arrayLinear.add(picker_linear);
            arrayBtn.add(confirmBtn);

            final int finalI = i;
            picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    int idofbtn = 900 + finalI;
                    Button btn = findViewById(idofbtn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("reamer", "onClick: getHour" + picker.getHour());
                            Log.d("reamer", "onClick: getId" + picker.getId());
                            int hour = picker.getHour();
                            int min = picker.getMinute();
                            String ampm = "AM";
                            if(hour>12){
                                hour=hour-12;
                                ampm = "PM";
                            }
                            else if(hour==0)
                                hour = 12;
                            else if(hour==12)
                                ampm ="PM";

                            String shour = Integer.toString(hour);
                            String smin = Integer.toString(min);
                            if (hour < 10)
                                shour = "0" + shour;
                            if (min < 10)
                                smin = "0" + smin;
                            String timenow = ampm + " " + shour + ":" + smin;
                            textView.setText(timenow);

                            picker_linear.setVisibility(View.GONE);
                            pickerBtn.setImageResource(R.drawable.alarm_down);
                        }
                    });
                }
            });

            pickerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (picker_linear.getVisibility() == View.GONE) {
                        picker_linear.setVisibility(View.VISIBLE);
                        pickerBtn.setImageResource(R.drawable.up);
                    } else {
                        picker_linear.setVisibility(View.GONE);
                        pickerBtn.setImageResource(R.drawable.alarm_down);
                    }
                }
            });
        }

        title.setText(title_pre);

        from_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imglistener(1);
            }
        });
        to_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imglistener(2);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimes();

                prescription.setTimes(times);
                prescription.setBegin(from_dateText.getText().toString());
                prescription.setEnd(to_dateText.getText().toString());

                Intent moveintent = new Intent(SetAlarmPage.this, Addword_Activity.class);
                moveintent.putExtra("prescription", prescription);
                moveintent.putExtra("id", id);
                moveintent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(moveintent);
                finish();
            }
        });

    }

    public void imglistener(int i) {
        switch (i) {
            case 1:
                if (from_linear.getVisibility() == View.GONE) {
                    from_img.setImageResource(R.drawable.up);
                    from_linear.setVisibility(View.VISIBLE);
                } else {
                    from_img.setImageResource(R.drawable.down);
                    from_linear.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (to_linear.getVisibility() == View.GONE) {
                    to_img.setImageResource(R.drawable.up);
                    to_linear.setVisibility(View.VISIBLE);
                } else {
                    to_img.setImageResource(R.drawable.down);
                    to_linear.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void initiate_datepicker(int num) {
        String fromdate = from_date.getYear() + ".";
        if (from_date.getMonth() + 1 < 10) {
            fromdate += "0" + (from_date.getMonth() + 1) + ".";
        } else {
            fromdate += (from_date.getMonth() + 1) + ".";
        }

        if (from_date.getDayOfMonth() < 10) {
            fromdate += "." + from_date.getDayOfMonth();
        } else {
            fromdate += from_date.getDayOfMonth();
        }

        from_dateText.setText(fromdate);


        from_date.init(from_date.getYear(), from_date.getMonth(), from_date.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                Button btn = findViewById(R.id.from_dateBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        from_date.updateDate(year, monthOfYear, dayOfMonth);
                        setAgain(1, year, monthOfYear + 1, dayOfMonth);
                        from_img.setImageResource(R.drawable.down);
                        from_linear.setVisibility(View.GONE);
                    }
                });
            }
        });

        to_date.init(from_date.getYear(), from_date.getMonth(), from_date.getDayOfMonth() + num, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                Button btn = findViewById(R.id.to_dateBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        to_date.updateDate(year, monthOfYear, dayOfMonth);
                        setAgain(2, year, monthOfYear + 1, dayOfMonth);
                        to_img.setImageResource(R.drawable.down);
                        to_linear.setVisibility(View.GONE);
                    }
                });
            }
        });

        String todate = to_date.getYear() + ".";
        if (to_date.getMonth() + 1 < 10) {
            todate += "0" + (to_date.getMonth() + 1) + ".";
        } else {
            todate += (to_date.getMonth() + 1) + ".";
        }

        if (to_date.getDayOfMonth() < 10) {
            todate += "." + to_date.getDayOfMonth();
        } else {
            todate += to_date.getDayOfMonth();
        }

        to_dateText.setText(todate);
    }

    public void setAgain(int what, int year, int month, int day) {
        String date = year + "." + month + "." + day;
        switch (what) {
            case 1:
                from_dateText.setText(date);
                break;
            case 2:
                to_dateText.setText(date);
                break;
        }
    }

    private void addTimes() {
        for (int i = 0; i < arrayText.size(); ++i) {
            String time=arrayText.get(i).getText().toString();
            String ampm = time.substring(0,2);
            String shour = time.substring(3,5);
            String smin = time.substring(6,8);
            String newTime="";
            if (ampm.equals("AM")){
                if(shour.equals("12")){
                    shour="00"+smin;
                }
                newTime=shour+smin;
            }else{
                if(shour.equals("12")){
                    newTime=shour+smin;
                }else {
                    shour = Integer.toString(Integer.parseInt(shour) + 12);
                    newTime = shour + smin;
                }
            }
            Log.d("what", "addTimes: i : " + i +", newTime :"+newTime);
            times.addTime(newTime);
        }
    }
}
