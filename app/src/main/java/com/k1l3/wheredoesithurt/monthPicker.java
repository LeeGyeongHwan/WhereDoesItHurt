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

public class monthPicker extends DialogFragment {


    public Calendar cal = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener listener;
    private Button btnConfirm;
    private Button btnCancel;
    private int month;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.month_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPicker.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, monthPicker.getValue(), monthPicker.getValue(), 0);
                monthPicker.this.getDialog().cancel();
            }
        });
        if (getArguments() == null) {
            month = cal.get(Calendar.MONTH + 1);
        } else {
            month = getArguments().getInt("month");
        }
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month);

        builder.setView(dialog);

        return builder.create();
    }
}

