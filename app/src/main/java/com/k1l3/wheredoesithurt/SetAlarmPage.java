package com.k1l3.wheredoesithurt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetAlarmPage extends AppCompatActivity {
    public TextView textView;
    public Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_page);

        textView=findViewById(R.id.textalarm);
        btn=findViewById(R.id.nextBtn);

        Intent intent=getIntent();
        String title_pre=intent.getStringExtra("title");
        int index=intent.getIntExtra("index",1);
        String text=title_pre.concat(" : ");
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

        textView.setText(text);
        Log.d("check", "onCreate: Text"+text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveintent= new Intent(SetAlarmPage.this,Addword_Activity.class);
                startActivity(moveintent);
            }
        });

    }
}
