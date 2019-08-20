package com.k1l3.wheredoesithurt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class Fragment_my_default_time extends Fragment {
    private View viewGroup;
    private Button add_time;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ListView defaultList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String id;
    private Adapter adapter;
    private ArrayList<String> defaultTime;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_my_default_time, container, false);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        id = ((MainActivity)getActivity()).getId();
        defaultList = (ListView)viewGroup.findViewById(R.id.defaultTimeListView);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        ((MainActivity)getActivity()).toolbar_my_default_time();

        add_time = (Button)viewGroup.findViewById(R.id.add_time);
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_time_spinner());
            }
        });

    //    adapter = new Adapter();
        databaseReference = database.getReference("users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapter = new Adapter();
                    User info = dataSnapshot.getValue(User.class);
                    if(info.getUserInfo().getDefaultTimes()!=null) {
                        defaultTime = info.getUserInfo().getDefaultTimes().getTimes();
                        for (int i = 0; i < defaultTime.size(); i++) {
                            adapter.addItem(new defaultTimeItem(defaultTime.get(i)));
                        }
                        defaultList.setAdapter(adapter);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        defaultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment_time_spinner = new Fragment_time_spinner();
                Bundle bundle = new Bundle();
                bundle.putString("time", defaultTime.get(position));
                bundle.putInt("position",position);
                fragment_time_spinner.setArguments(bundle);
                replaceFragment(fragment_time_spinner);
            }
        });
        return viewGroup;
    }
    private void replaceFragment(@NonNull Fragment fragment) {
        transaction= manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
        Log.e(TAG,"값 : " + String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }

    private class Adapter extends BaseAdapter {
        ArrayList<defaultTimeItem> items = new ArrayList<defaultTimeItem>();

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

        public void addItem(defaultTimeItem item) {
            items.add(item);
        }

        public void addItem(ArrayList<defaultTimeItem> item) {
            items = item;
        }

        public void deleteItem(Search_item item) {
            items.remove(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            defaultTimeItemView view = new defaultTimeItemView(viewGroup.getContext());
            defaultTimeItem item = items.get(position);
            view.setAMPM(item.getAMPM());
            view.setTime(item.getTime());
            return view;
        }
    }
}
