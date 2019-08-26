package com.k1l3.wheredoesithurt;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k1l3.wheredoesithurt.models.LifeStyles;
import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_myinfo extends Fragment {
    private View viewGroup;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String id;
    private EditText name;
    private Button year,month,day,savebtn;
    private RadioButton female,male;
    private CheckBox smoking,alcohol,obesity,pargnancy;
    private FragmentManager manager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_myinfo, container, false);
        ((MainActivity)getActivity()).toolbar_myinfo();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        id = ((MainActivity)getActivity()).getId();
        name = (EditText)viewGroup.findViewById(R.id.name);
        year = (Button)viewGroup.findViewById(R.id.year);
        month = (Button)viewGroup.findViewById(R.id.month);
        day = (Button)viewGroup.findViewById(R.id.day);
        savebtn = (Button)viewGroup.findViewById(R.id.savebtn);
        female = (RadioButton)viewGroup.findViewById(R.id.female);
        male = (RadioButton)viewGroup.findViewById(R.id.male);
        smoking = (CheckBox)viewGroup.findViewById(R.id.smoknig);
        alcohol = (CheckBox)viewGroup.findViewById(R.id.alcohol);
        obesity = (CheckBox)viewGroup.findViewById(R.id.obesity);
        pargnancy = (CheckBox)viewGroup.findViewById(R.id.pargnancy);
        manager = getFragmentManager();
        final DatePickerDialog.OnDateSetListener yearListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth){
                year.setText(String.valueOf(years));
            }
        };
        final DatePickerDialog.OnDateSetListener monthListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int months, int monthOfYear, int dayOfMonth){
                if(months<10) {
                    month.setText("0"+String.valueOf(months));
                }else{
                    month.setText(String.valueOf(months));
                }
            }
        };
        final DatePickerDialog.OnDateSetListener dayListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int days, int monthOfYear, int dayOfMonth){
                if(days<10) {
                    day.setText("0"+String.valueOf(days));
                }else{
                    day.setText(String.valueOf(days));
                }
            }
        };
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPicker yearPicker = new yearPicker();
                yearPicker.setListener(yearListener);

                Bundle args = new Bundle();
                args.putInt("year",Integer.valueOf(year.getText().toString()));
                yearPicker.setArguments(args);
                yearPicker.show(getFragmentManager(), "YearPickerTest");
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthPicker monthPicker = new monthPicker();
                monthPicker.setListener(monthListener);
                Bundle args = new Bundle();
                if(month.getText().toString().substring(0,1)=="0"){
                    args.putInt("month",Integer.valueOf(month.getText().toString().substring(1,2)));
                }else{
                    args.putInt("month",Integer.valueOf(month.getText().toString()));
                }
                monthPicker.setArguments(args);
                monthPicker.show(getFragmentManager(),"MonthPickerTest");
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPicker dayPicker = new dayPicker();
                dayPicker.setListener(dayListener);
                Bundle args = new Bundle();
                if(day.getText().toString().substring(0,1)=="0"){
                    args.putInt("day",Integer.valueOf(day.getText().toString().substring(1,2)));
                }else{
                    args.putInt("day",Integer.valueOf(day.getText().toString()));
                }
                dayPicker.setArguments(args);
                dayPicker.show(getFragmentManager(),"dayPickerTest");
            }
        });
        //초기 불러오기
        databaseReference = database.getReference("users").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User info = dataSnapshot.getValue(User.class);
                name.setText(info.getUserInfo().getName());
                if(info.getUserInfo().getGender()!=null){
                    if(info.getUserInfo().getGender().equals("Female")){
                        female.setChecked(true);
                    }
                    else{
                        male.setChecked(true);
                    }
                }
                if(info.getUserInfo().getAge()!=null){
                    year.setText(info.getUserInfo().getAge().substring(0,4));
                    month.setText(info.getUserInfo().getAge().substring(4,6));
                    day.setText(info.getUserInfo().getAge().substring(6,8));
                }
                if(info.getUserInfo().getLifeStyles()!=null){
                    if(info.getUserInfo().getLifeStyles().getLifeStyles().get(0)==1){
                        smoking.setChecked(true);
                    }else{
                        smoking.setChecked(false);
                    }
                    if(info.getUserInfo().getLifeStyles().getLifeStyles().get(1)==1){
                        alcohol.setChecked(true);
                    }else{
                        alcohol.setChecked(false);
                    }
                    if(info.getUserInfo().getLifeStyles().getLifeStyles().get(2)==1){
                        pargnancy.setChecked(true);
                    }else{
                        pargnancy.setChecked(false);
                    }
                    if(info.getUserInfo().getLifeStyles().getLifeStyles().get(3)==1){
                        obesity.setChecked(true);
                    }else{
                        obesity.setChecked(false);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //저장하기
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = database.getReference("users").child(id);
                ValueEventListener databaseListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User info = dataSnapshot.getValue(User.class);
                        //이름
                        info.getUserInfo().setName(name.getText().toString());
                        //성별
                        if(female.isChecked()){
                            info.getUserInfo().setGender("Female");
                        }else if(male.isChecked()){
                            info.getUserInfo().setGender("Male");
                        }
                        //생년월일
                        info.getUserInfo().setAge(year.getText().toString()+month.getText().toString()+day.getText().toString());
                        //나의 특징
                        ArrayList<Integer> myFeature = new ArrayList<> (Arrays.asList(0,0,0,0));
                        //흡연
                        if(smoking.isChecked()){
                            myFeature.set(0,1);
                        }else{
                            myFeature.set(0,0);
                        }
                        //음주
                        if(alcohol.isChecked()){
                            myFeature.set(1,1);
                        }else{
                            myFeature.set(1,0);
                        }
                        //임신
                        if(pargnancy.isChecked()){
                            myFeature.set(2,1);
                        }else{
                            myFeature.set(2,0);
                        }
                        //비만
                        if(obesity.isChecked()){
                            myFeature.set(3,1);
                        }else{
                            myFeature.set(3,0);
                        }
                        LifeStyles lifeStyles = new LifeStyles();
                        lifeStyles.setLifeStyles(myFeature);
                        info.getUserInfo().setLifeStyles(lifeStyles);
                        databaseReference.setValue(info);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };
                databaseReference.addListenerForSingleValueEvent(databaseListener);
                manager.popBackStack();
            }
        });

        return viewGroup;
    }

}
