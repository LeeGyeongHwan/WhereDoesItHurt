package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.api.services.vision.v1.Vision;

import java.util.ArrayList;

public class SetAlarmPage extends AppCompatActivity {
    public Button nextBtn,cancelBtn;
    public TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_page);

        nextBtn = findViewById(R.id.alarm_nextpage);
        cancelBtn = findViewById(R.id.cancelalarm);
        title = findViewById(R.id.title_alarm);


        Intent intent=getIntent();
        String title_pre=intent.getStringExtra("title");
        String alarmnum = intent.getStringExtra("alarmcount");
        String alarmday = intent.getStringExtra("alarmday");
        int index=intent.getIntExtra("index",1);
        String text= "";
        ArrayList<TextView> arrayText = new ArrayList<>();
        ArrayList<TimePicker> arrayPicker = new ArrayList<>();
        ArrayList<ImageView> arrayImg = new ArrayList<>();

        for(int i=0; i<Integer.parseInt(alarmnum); i++){
            LinearLayout linearLayout = findViewById(R.id.startinflatealarm);

            LayoutInflater layoutInflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layoutInflater.inflate(R.layout.add_alarm, linearLayout, true);
            TextView textView=findViewById(R.id.extext);
            final TimePicker picker=findViewById(R.id.timepicker);
            final ImageView pickerBtn = findViewById(R.id.pickerBtn);
            textView.setId(500+i);
            picker.setId(600+i);
            pickerBtn.setId(700+i);
            arrayText.add(textView);
            arrayPicker.add(picker);
            arrayImg.add(pickerBtn);
            pickerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(picker.getVisibility()== View.GONE) {
                        picker.setVisibility(View.VISIBLE);
                        pickerBtn.setImageResource(R.drawable.up);
                    }
                    else{
                        picker.setVisibility(View.GONE);
                        pickerBtn.setImageResource(R.drawable.down);
                    }
                }
            });
        }

        title.setText(title_pre);




        for(int i=0;i<index;i++){
            String str=intent.getStringExtra("edit1".concat(Integer.toString(i)));
            text=text.concat(str+" ");
            str=intent.getStringExtra("edit2".concat(Integer.toString(i)));
            text=text.concat(str+" ");
            str=intent.getStringExtra("edit3".concat(Integer.toString(i)));
            text=text.concat(str+" ");
            str=intent.getStringExtra("edit4".concat(Integer.toString(i)));
            text=text.concat(str+" ");
        }


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveintent= new Intent(SetAlarmPage.this,Addword_Activity.class);
                startActivity(moveintent);
            }
        });

    }
}
