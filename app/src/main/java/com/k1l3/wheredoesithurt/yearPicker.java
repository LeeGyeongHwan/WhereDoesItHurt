package com.k1l3.wheredoesithurt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

public class yearPicker extends DialogFragment {


    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    private Button btnConfirm;
    private Button btnCancel;
    private int year;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.year_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                yearPicker.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, yearPicker.getValue(), yearPicker.getValue(),0);
                yearPicker.this.getDialog().cancel();
            }
        });
        if(getArguments()==null) {
            year = cal.get(Calendar.YEAR);
        }else{
            year = getArguments().getInt("year");
        }
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2019);
        yearPicker.setValue(year);

        builder.setView(dialog);

        return builder.create();
    }
}

