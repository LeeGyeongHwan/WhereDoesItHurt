package com.k1l3.wheredoesithurt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class Fragment_calendar extends Fragment {
    View viewGroup;
    FragmentManager manager;
    FragmentTransaction transaction;
    CalendarView calendarView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_calendar,container, false);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        ((MainActivity)getActivity()).toolbar_calendar();
        initCalendar();
        return viewGroup;
    }

    private void initCalendar(){

    }

}
