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
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private String hour,min,id;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FragmentManager manager;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_time_spinner, container, false);
        ((MainActivity)getActivity()).toolbar_edit_time();
        id = ((MainActivity)getActivity()).getId();
        timePicker = (TimePicker)viewGroup.findViewById(R.id.time_picker);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        manager = getFragmentManager();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                Toast.makeText(getContext(), "hour : " + hour + ", min : " + min, Toast.LENGTH_LONG).show();
            }
        });
        Button Finish = (Button)viewGroup.findViewById(R.id.finish);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = String.valueOf(timePicker.getCurrentHour());
                min = String.valueOf(timePicker.getCurrentMinute());
                Times time = new Times();
                Toast.makeText(getContext(), "hour : " + hour + ", min : " + min, Toast.LENGTH_LONG).show();

                databaseReference = database.getReference("users").child(id);
                ValueEventListener databaseListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Times time = new Times();
                        User info = dataSnapshot.getValue(User.class);
                        if(info.getUserInfo().getDefaultTimes()==null){
                            time.addTime(hour+min);
                        }
                        else{
                            time = info.getUserInfo().getDefaultTimes();
                            time.addTime(hour+min);
                        }
                        info.getUserInfo().setDefaultTimes(time);
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
