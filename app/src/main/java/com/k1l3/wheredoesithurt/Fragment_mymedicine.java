package com.k1l3.wheredoesithurt;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class Fragment_mymedicine extends Fragment {
    private View viewGroup;
    private String id;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FragmentManager manager;
    private ListView listView;
    private Adapter adapter;
    private TextView no_medicine;
    private ScrollView scrollView;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_mymedicine, container, false);
        ((MainActivity) getActivity()).toolbar_edit_time();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        listView = (ListView)viewGroup.findViewById(R.id.mymedicine_list_view);
        id = ((MainActivity) getActivity()).getId();
        ((MainActivity)getActivity()).toolbar_mymedicine();
        no_medicine = (TextView)viewGroup.findViewById(R.id.no_medicine);
        scrollView = (ScrollView)viewGroup.findViewById(R.id.scrollView);
        databaseReference = database.getReference("users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter = new Adapter();
                User info = dataSnapshot.getValue(User.class);
                if(info.getPrescriptions()!=null){
                    for(int i=0;i<info.getPrescriptions().size();i++){
                        int startYear = Integer.valueOf(info.getPrescriptions().get(i).getBegin().substring(0,4));
                        int startMonth = Integer.valueOf(info.getPrescriptions().get(i).getBegin().substring(5,7));
                        int startDay = Integer.valueOf(info.getPrescriptions().get(i).getBegin().substring(8,10));
                        int endYear = Integer.valueOf(info.getPrescriptions().get(i).getEnd().substring(0,4));
                        int endMonth = Integer.valueOf(info.getPrescriptions().get(i).getEnd().substring(5,7));
                        int endDay = Integer.valueOf(info.getPrescriptions().get(i).getEnd().substring(8,10));
                        Calendar cal = Calendar.getInstance();
                        if(cal.get(Calendar.YEAR)>=startYear&&cal.get(Calendar.YEAR)<=endYear){
                            if(cal.get(Calendar.MONTH)+1>=startMonth&&cal.get(Calendar.MONTH)+1<=endMonth){
                                if(cal.get(Calendar.DATE)>=startDay&&cal.get(Calendar.DATE)<=endDay){
                                    no_medicine.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    MyMedicine_item myMedicine_item = new MyMedicine_item(info.getPrescriptions().get(i));
                                    adapter.addItem(myMedicine_item);
                                }
                            }
                        }
                    }
                    listView.setAdapter(adapter);
                }
                if(adapter.isEmpty()){
                    no_medicine.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return viewGroup;
    }

    class Adapter extends BaseAdapter {
        ArrayList<MyMedicine_item> items = new ArrayList<MyMedicine_item>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(MyMedicine_item item) {
            items.add(item);
        }

        public void addItem(ArrayList<MyMedicine_item> item) {
            items = item;
        }

        public void deleteItem(Search_item item) {
            items.remove(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyMedicine_itemView view = new MyMedicine_itemView(viewGroup.getContext());
            final MyMedicine_item item = items.get(position);
            view.setName(item.getName());
            view.setName2(item.getName());
            view.setDate(item.getStart().substring(0,4)+"."+item.getStart().substring(5,7)+"."+item.getStart().substring(8,10)
                            +" - "+item.getEnd().substring(0,4)+"."+item.getEnd().substring(5,7)+"."+item.getEnd().substring(8,10));
            StringBuffer buffer = new StringBuffer();
            for(int i=0;i<item.getAlarm().getTimes().size();i++){
                if(item.getAlarm().getTimes().get(i).substring(0,2).equals("AM")) {
                    buffer.append(item.getAlarm().getTimes().get(i).substring(3,8) + " ");
                }else{
                    if(item.getAlarm().getTimes().get(i).substring(3,5).equals("12")){
                        buffer.append(item.getAlarm().getTimes().get(i).substring(3,8) + " ");
                    }else {
                        buffer.append(Integer.valueOf(item.getAlarm().getTimes().get(i).substring(3, 5)) + 12 + item.getAlarm().getTimes().get(i).substring(5, 8) + " ");
                    }
                }
            }
            view.setAlarm(buffer.toString());
            StringBuffer buffer2 = new StringBuffer();
            for(int i=0;i<item.getMedicine_info().size();i++){
                buffer2.append(item.getMedicine_info().get(i).getName()+" ");
            }
            view.setMedicine_info(buffer2.toString());

            //클릭시 밑에화면나오게.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(view.getMy_medicine_layout().getVisibility()==View.GONE){
                        view.getName().setBackgroundColor(Color.rgb(173,165,253));
                        view.getName().setTextColor(Color.rgb(255,255,255));
                        view.getMy_medicine_layout().setVisibility(View.VISIBLE);
                    }else{
                        view.getName().setBackgroundColor(Color.rgb(255,255,255));
                        view.getName().setTextColor(Color.rgb(173,165,253));
                        view.getMy_medicine_layout().setVisibility(View.GONE);
                    }
                }
            });

            view.getDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference = database.getReference("users").child(id);
                    ValueEventListener databaseListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User info = dataSnapshot.getValue(User.class);
                            ArrayList<Prescription> prescriptions = info.getPrescriptions();
                            if(prescriptions!=null) {
                                for (int i = 0; i < info.getPrescriptions().size(); i++) {
                                    if (item.getName().equals(prescriptions.get(i).getName()) && item.getStart().equals(prescriptions.get(i).getBegin())
                                            && item.getEnd().equals(prescriptions.get(i).getEnd())) {
                                        prescriptions.remove(i);
                                        Log.e(TAG, i + " 번째 제거");
                                        break;
                                    }
                                }
                                info.setPrescriptions(prescriptions);
                                databaseReference.setValue(info);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(databaseListener);
                }
            });
            return view;
        }
    }
}
