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
import android.widget.Button;

import static android.support.constraint.Constraints.TAG;

public class Fragment_my_default_time extends Fragment {
    View viewGroup;
    Button add_time;
    FragmentManager manager;
    FragmentTransaction transaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_my_default_time, container, false);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        ((MainActivity)getActivity()).toolbar_my_default_time();

        add_time = (Button)viewGroup.findViewById(R.id.add_time);
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_time_spinner());
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

}
