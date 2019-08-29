package com.k1l3.wheredoesithurt;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

public class Fragment_mymedicine extends Fragment {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final String today = dateFormat.format(new Date());
    private final User user = User.getInstance();
    private View viewGroup;
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
        listView = viewGroup.findViewById(R.id.mymedicine_list_view);
        ((MainActivity) getActivity()).toolbar_mymedicine();
        no_medicine = viewGroup.findViewById(R.id.no_medicine);
        scrollView = viewGroup.findViewById(R.id.scrollView);

        adapter = new Adapter();

        if(user.getPrescriptions() != null){
            for (int i = 0; i < user.getPrescriptions().size(); i++) {
                String startDay = user.getPrescriptions().get(i).getBegin();
                String endDay = user.getPrescriptions().get(i).getEnd();

                boolean todayInRange = today.compareTo(startDay) >= 0 && today.compareTo(endDay) <= 0;

                if(todayInRange){
                    no_medicine.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                    MyMedicine_item myMedicine_item = new MyMedicine_item(user.getPrescriptions().get(i));
                    adapter.addItem(myMedicine_item);
                }
            }
            listView.setAdapter(adapter);
        }
        if (adapter.isEmpty()) {
            no_medicine.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyMedicine_itemView view = new MyMedicine_itemView(viewGroup.getContext());
            final MyMedicine_item item = items.get(position);
            view.setName(item.getName());
            view.setName2(item.getName());
            view.setDate(item.getStart().substring(0, 4) + "." + item.getStart().substring(5, 7) + "." + item.getStart().substring(8, 10)
                    + " - " + item.getEnd().substring(0, 4) + "." + item.getEnd().substring(5, 7) + "." + item.getEnd().substring(8, 10));
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < item.getAlarm().getTimes().size(); i++) {
                buffer.append(item.getAlarm().getTimes().get(i).substring(0, 2) + ":" + item.getAlarm().getTimes().get(i).substring(2, 4) + " ");
            }
            view.setAlarm(buffer.toString());
            StringBuffer buffer2 = new StringBuffer();
            for (int i = 0; i < item.getMedicine_info().size(); i++) {
                buffer2.append(item.getMedicine_info().get(i).getName() + " ");
            }
            view.setMedicine_info(buffer2.toString());

            //클릭시 밑에화면나오게.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view.getMy_medicine_layout().getVisibility() == View.GONE) {
                        view.getName().setBackgroundColor(Color.rgb(173, 165, 253));
                        view.getName().setTextColor(Color.rgb(255, 255, 255));
                        view.getMy_medicine_layout().setVisibility(View.VISIBLE);
                    } else {
                        view.getName().setBackgroundColor(Color.rgb(255, 255, 255));
                        view.getName().setTextColor(Color.rgb(173, 165, 253));
                        view.getMy_medicine_layout().setVisibility(View.GONE);
                    }
                }
            });

            view.getDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Prescription> prescriptions = user.getPrescriptions();

                    if(prescriptions != null){
                        for (int i = 0; i < user.getPrescriptions().size(); i++) {
                            if (item.getName().equals(prescriptions.get(i).getName()) && item.getStart().equals(prescriptions.get(i).getBegin())
                                    && item.getEnd().equals(prescriptions.get(i).getEnd())) {
                                prescriptions.remove(i);
                                break;
                            }
                        }
                        user.setPrescriptions(prescriptions);
                    }

                    user.syncWithDatabase();

                    items.remove(item);

                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }
}
