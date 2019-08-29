package com.k1l3.wheredoesithurt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k1l3.wheredoesithurt.models.Times;
import com.k1l3.wheredoesithurt.models.User;

public class Fragment_time_spinner extends Fragment {
    private View viewGroup;
    private TimePicker timePicker;
    private String hour, min, receiveTime;
    private int position;
    private FragmentManager manager;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_time_spinner, container, false);
        ((MainActivity) getActivity()).toolbar_edit_time();
        timePicker = viewGroup.findViewById(R.id.time_picker);
        manager = getFragmentManager();

        ImageButton cancelbtn = viewGroup.findViewById(R.id.cancel);

        final User user = User.getInstance();

        if (getArguments() != null) {
            receiveTime = getArguments().getString("time");
            position = getArguments().getInt("position");
            timePicker.setHour(Integer.valueOf(receiveTime.substring(0, 2)));
            timePicker.setMinute(Integer.valueOf(receiveTime.substring(2, 4)));

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.getInstance().getUserInfo().getDefaultTimes().getTimes().remove(position);
                    User.getInstance().syncWithDatabase();

                    manager.popBackStack();
                }
            });
        } else {
            receiveTime = "add";
            cancelbtn.setVisibility(View.GONE);
        }

        ImageButton Finish = viewGroup.findViewById(R.id.finish);

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePicker.getHour() < 10) {
                    hour = "0" + timePicker.getHour();
                } else {
                    hour = String.valueOf(timePicker.getHour());
                }

                if (timePicker.getMinute() < 10) {
                    min = "0" + timePicker.getMinute();
                } else {
                    min = String.valueOf(timePicker.getMinute());
                }

                Times defaultTimes = user.getUserInfo().getDefaultTimes();

                if (receiveTime.equals("add")) {
                    defaultTimes.addTime(hour + min);
                } else {
                    defaultTimes.getTimes().set(position, hour + min);
                }

                User.getInstance().syncWithDatabase();

                manager.popBackStack();
            }
        });
        return viewGroup;
    }


}
