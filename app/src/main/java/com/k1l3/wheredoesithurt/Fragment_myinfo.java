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
    private EditText name;
    private Button year, month, day, savebtn;
    private RadioButton female, male;
    private CheckBox smoking, alcohol, obesity, pargnancy;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_myinfo, container, false);
        ((MainActivity) getActivity()).toolbar_myinfo();
        name = viewGroup.findViewById(R.id.name);
        year = viewGroup.findViewById(R.id.year);
        month = viewGroup.findViewById(R.id.month);
        day = viewGroup.findViewById(R.id.day);
        savebtn = viewGroup.findViewById(R.id.savebtn);
        female = viewGroup.findViewById(R.id.female);
        male = viewGroup.findViewById(R.id.male);
        smoking = viewGroup.findViewById(R.id.smoknig);
        alcohol = viewGroup.findViewById(R.id.alcohol);
        obesity = viewGroup.findViewById(R.id.obesity);
        pargnancy = viewGroup.findViewById(R.id.pargnancy);
        manager = getFragmentManager();
        final DatePickerDialog.OnDateSetListener yearListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
                year.setText(String.valueOf(years));
            }
        };
        final DatePickerDialog.OnDateSetListener monthListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int months, int monthOfYear, int dayOfMonth) {
                if (months < 10) {
                    month.setText("0" + months);
                } else {
                    month.setText(String.valueOf(months));
                }
            }
        };
        final DatePickerDialog.OnDateSetListener dayListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int days, int monthOfYear, int dayOfMonth) {
                if (days < 10) {
                    day.setText("0" + days);
                } else {
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
                args.putInt("year", Integer.valueOf(year.getText().toString()));
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
                if (month.getText().toString().substring(0, 1) == "0") {
                    args.putInt("month", Integer.valueOf(month.getText().toString().substring(1, 2)));
                } else {
                    args.putInt("month", Integer.valueOf(month.getText().toString()));
                }
                monthPicker.setArguments(args);
                monthPicker.show(getFragmentManager(), "MonthPickerTest");
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPicker dayPicker = new dayPicker();
                dayPicker.setListener(dayListener);
                Bundle args = new Bundle();
                if (day.getText().toString().substring(0, 1) == "0") {
                    args.putInt("day", Integer.valueOf(day.getText().toString().substring(1, 2)));
                } else {
                    args.putInt("day", Integer.valueOf(day.getText().toString()));
                }
                dayPicker.setArguments(args);
                dayPicker.show(getFragmentManager(), "dayPickerTest");
            }
        });
        //초기 불러오기
        final User user =User.getInstance();
        name.setText(user.getUserInfo().getName());
        if (user.getUserInfo().getGender() != null) {
            if (user.getUserInfo().getGender().equals("Female")) {
                female.setChecked(true);
            } else {
                male.setChecked(true);
            }
        }
        if (user.getUserInfo().getAge() != null) {
            year.setText(user.getUserInfo().getAge().substring(0, 4));
            month.setText(user.getUserInfo().getAge().substring(4, 6));
            day.setText(user.getUserInfo().getAge().substring(6, 8));
        }
        if (user.getUserInfo().getLifeStyles() != null) {
            if (user.getUserInfo().getLifeStyles().getLifeStyles().get(0) == 1) {
                smoking.setChecked(true);
            } else {
                smoking.setChecked(false);
            }
            if (user.getUserInfo().getLifeStyles().getLifeStyles().get(1) == 1) {
                alcohol.setChecked(true);
            } else {
                alcohol.setChecked(false);
            }
            if (user.getUserInfo().getLifeStyles().getLifeStyles().get(2) == 1) {
                pargnancy.setChecked(true);
            } else {
                pargnancy.setChecked(false);
            }
            if (user.getUserInfo().getLifeStyles().getLifeStyles().get(3) == 1) {
                obesity.setChecked(true);
            } else {
                obesity.setChecked(false);
            }
        }

        //저장하기
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getUserInfo().setName(name.getText().toString());

                if (female.isChecked()) {
                    user.getUserInfo().setGender("Female");
                } else if (male.isChecked()) {
                    user.getUserInfo().setGender("Male");
                }
                //생년월일
                user.getUserInfo().setAge(year.getText().toString() + month.getText().toString() + day.getText().toString());
                //나의 특징
                ArrayList<Integer> myFeature = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
                //흡연
                if (smoking.isChecked()) {
                    myFeature.set(0, 1);
                } else {
                    myFeature.set(0, 0);
                }
                //음주
                if (alcohol.isChecked()) {
                    myFeature.set(1, 1);
                } else {
                    myFeature.set(1, 0);
                }
                //임신
                if (pargnancy.isChecked()) {
                    myFeature.set(2, 1);
                } else {
                    myFeature.set(2, 0);
                }
                //비만
                if (obesity.isChecked()) {
                    myFeature.set(3, 1);
                } else {
                    myFeature.set(3, 0);
                }

                LifeStyles lifeStyles = new LifeStyles();
                lifeStyles.setLifeStyles(myFeature);

                user.getUserInfo().setLifeStyles(lifeStyles);

                user.syncWithDatabase();

                manager.popBackStack();
            }
        });

        return viewGroup;
    }

}
