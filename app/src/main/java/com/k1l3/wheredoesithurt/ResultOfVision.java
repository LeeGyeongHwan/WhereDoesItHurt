package com.k1l3.wheredoesithurt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultOfVision extends AppCompatActivity {

    public TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_analysis);

        txt=(TextView)findViewById(R.id.image_details);

        Intent intent = getIntent();
        txt.setText(intent.getStringExtra("result"));

    }
}
