package com.k1l3.wheredoesithurt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResultOfVision extends AppCompatActivity {
    public Button cancelBtn,nextPage;
    public EditText title_pre;
    public EditText edit1_1,edit1_2,edit1_3,edit1_4;
    public EditText edit2_1,edit2_2,edit2_3,edit2_4;
    public EditText edit3_1,edit3_2,edit3_3,edit3_4;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_analysis);

        cancelBtn=(Button)findViewById(R.id.cancelvision);
        nextPage=findViewById(R.id.nextpage);
        title_pre=findViewById(R.id.title_prescription);
        edit1_1=(EditText)findViewById(R.id.analysis_edit1_1);
        edit1_2=(EditText)findViewById(R.id.analysis_edit1_2);
        edit1_3=(EditText)findViewById(R.id.analysis_edit1_3);
        edit1_4=(EditText)findViewById(R.id.analysis_edit1_4);

        edit2_1=(EditText)findViewById(R.id.analysis_edit2_1);
        edit2_2=(EditText)findViewById(R.id.analysis_edit2_2);
        edit2_3=(EditText)findViewById(R.id.analysis_edit2_3);
        edit2_4=(EditText)findViewById(R.id.analysis_edit2_4);

        edit3_1=(EditText)findViewById(R.id.analysis_edit3_1);
        edit3_2=(EditText)findViewById(R.id.analysis_edit3_2);
        edit3_3=(EditText)findViewById(R.id.analysis_edit3_3);
        edit3_4=(EditText)findViewById(R.id.analysis_edit3_4);


        Intent intent = getIntent();
        String getStr= intent.getStringExtra("result");
        String[] words = getStr.split("\\s");

        edit1_1.setText(words[0]);
        edit1_2.setText(words[1]);
        edit1_3.setText(words[2]);
        edit1_4.setText(words[3]);

        edit2_1.setText(words[4]);
        edit2_2.setText(words[5]);
        edit2_3.setText(words[6]);
        edit2_4.setText(words[7]);

        edit3_1.setText(words[8]);
        edit3_2.setText(words[9]);
        edit3_3.setText(words[10]);
        edit3_4.setText(words[11]);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ResultOfVision.this, Addword_Activity.class);
                startActivity(intent);
            }
        } );
    }
}
