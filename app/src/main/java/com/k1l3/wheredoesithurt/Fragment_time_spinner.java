package com.k1l3.wheredoesithurt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

public class Fragment_time_spinner extends Fragment {
    View viewGroup;
    TimePicker timePicker;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_time_spinner, container, false);
        ((MainActivity)getActivity()).toolbar_edit_time();
        timePicker = (TimePicker)viewGroup.findViewById(R.id.time_picker);

        return viewGroup;
    }


}
