package com.k1l3.wheredoesithurt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;

public class Fragment_my_default_time extends Fragment {
    private View viewGroup;
    private ImageButton add_time;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ListView defaultList;
    private Adapter adapter;
    private ArrayList<String> defaultTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_my_default_time, container, false);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        defaultList = (ListView) viewGroup.findViewById(R.id.defaultTimeListView);
        ((MainActivity) getActivity()).toolbar_my_default_time();

        add_time = viewGroup.findViewById(R.id.add_time);
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_time_spinner());
            }
        });

        adapter = new Adapter();

        User user = User.getInstance();

        defaultTime = user.getUserInfo().getDefaultTimes().getTimes();
        for (int i = 0; i < defaultTime.size(); i++) {
            adapter.addItem(new defaultTimeItem(defaultTime.get(i)));
        }
        defaultList.setAdapter(adapter);

        defaultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment_time_spinner = new Fragment_time_spinner();

                Bundle bundle = new Bundle();
                bundle.putString("time", defaultTime.get(position));
                bundle.putInt("position", position);
                fragment_time_spinner.setArguments(bundle);

                replaceFragment(fragment_time_spinner);
            }
        });
        return viewGroup;
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
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
