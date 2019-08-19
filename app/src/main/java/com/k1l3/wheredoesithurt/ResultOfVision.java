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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.k1l3.wheredoesithurt.models.Medicine;
import java.util.ArrayList;

import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultOfVision extends AppCompatActivity {
    private Button cancelBtn,nextPage,addMedBtn;
    private EditText title_pre;
    private Prescription prescription;
    private ArrayList<Medicine> medicines;
    private int index=0;

    private final ArrayList<EditText> EditList1 = new ArrayList<>();
    private final ArrayList<EditText> EditList2 = new ArrayList<>();
    private final ArrayList<EditText> EditList3 = new ArrayList<>();
    private final ArrayList<EditText> EditList4 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_analysis);

        cancelBtn=(Button)findViewById(R.id.cancelvision);
        nextPage=findViewById(R.id.nextpage);
        addMedBtn=findViewById(R.id.addMedBtn);
        title_pre=findViewById(R.id.title_prescription);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = simpleDateFormat.format(date);

        prescription = new Prescription();
        prescription.setBegin(formatDate);

        Intent intent = getIntent();
        String getStr= intent.getStringExtra("result");
        int getCount=intent.getIntExtra("numbermedicine",1);
        Log.d("check", "onCreate: "+getStr);

        medicines = new ArrayList<>();

        cancelBtn = (Button) findViewById(R.id.cancelvision);
        nextPage = findViewById(R.id.nextpage);
        title_pre = findViewById(R.id.title_prescription);

        for(int i=0; i<getCount; i++){
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.startinflatelinear);

            LayoutInflater layoutInflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layoutInflater.inflate(R.layout.add_linear, linearLayout, true);
            EditText edit1=findViewById(R.id.analysis_edit1_1);
            EditText edit2=findViewById(R.id.analysis_edit1_2);
            EditText edit3=findViewById(R.id.analysis_edit1_3);
            EditText edit4=findViewById(R.id.analysis_edit1_4);
            edit1.setId(100+i);
            edit2.setId(200+i);
            edit3.setId(300+i);
            edit4.setId(400+i);
            EditList1.add(edit1);
            EditList2.add(edit2);
            EditList3.add(edit3);
            EditList4.add(edit4);
            index=i+1;
        }

        if(getStr != null){
            TextView text=findViewById(R.id.visiontext);
            text.setVisibility(View.VISIBLE);
            String[] words = getStr.split("\\s");

            for(int i=0;i<words.length;i++){
                int num=i/4;
                switch (i%4){
                    case 0:EditList1.get(num).setText(words[i]);
                        break;
                    case 1:EditList2.get(num).setText(words[i]);
                        break;
                    case 2:EditList3.get(num).setText(words[i]);
                        break;
                    case 3:EditList4.get(num).setText(words[i]);
                        break;
                    default:break;
                }
            }
        }
        
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent (ResultOfVision.this,SetAlarmPage.class);
                intent.putExtra("index",index);
                intent.putExtra("title",title_pre.getText().toString());
                intent.putExtra("alarmcount",EditList3.get(0).getText().toString());
                intent.putExtra("alarmday",EditList4.get(0).getText().toString());

                addMedicines();
                prescription.setMedicines(medicines);
                intent.putExtra("prescription",prescription);
                
                startActivity(intent);
            }
        } );

        addMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.startinflatelinear);

                LayoutInflater layoutInflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                layoutInflater.inflate(R.layout.add_linear, linearLayout, true);
                EditText edit1=findViewById(R.id.analysis_edit1_1);
                EditText edit2=findViewById(R.id.analysis_edit1_2);
                EditText edit3=findViewById(R.id.analysis_edit1_3);
                EditText edit4=findViewById(R.id.analysis_edit1_4);
                edit1.setId(100+ index);
                edit2.setId(200+ index);
                edit3.setId(300+ index);
                edit4.setId(400+ index);
                index++;
                EditList1.add(edit1);
                EditList2.add(edit2);
                EditList3.add(edit3);
                EditList4.add(edit4);
            }
        });
    }

    private void addMedicines(){
        for(int i=0;i<EditList1.size();++i){
            medicines.add(new Medicine(EditList1.get(i).getText().toString(),
                                        Integer.parseInt(EditList2.get(i).getText().toString()),
                                        Integer.parseInt(EditList3.get(i).getText().toString()),
                                        Integer.parseInt(EditList4.get(i).getText().toString())));
        }
    }
}
