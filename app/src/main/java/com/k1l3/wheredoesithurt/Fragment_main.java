package com.k1l3.wheredoesithurt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.constraint.Constraints.TAG;

public class Fragment_main extends Fragment {
    EditText medicine_search;
    ViewGroup btn1, btn2, btn3;
    public int[] check = new int[3];
    FragmentManager manager;
    FragmentTransaction transaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        final View viewGroup = inflater.inflate(R.layout.fragment_main,container, false);
        medicine_search=(EditText)viewGroup.findViewById(R.id.medicin_search);
        medicine_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        medicine_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!medicine_search.getText().toString().equals("")) {
                    Fragment fragment_search = new Fragment_search();
                    Bundle bundle = new Bundle();
                    bundle.putString("search_word", medicine_search.getText().toString());
                    fragment_search.setArguments(bundle);
                    replaceFragment(fragment_search);
                }
                else{
                    Toast.makeText(getContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        //버튼 클릭
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView when, time, iseat;
                switch (view.getId()) {
                    case R.id.flipbtn1 :

                        check[0]++;
                        when = (TextView)viewGroup.findViewById(R.id.when1);
                        time = (TextView)viewGroup.findViewById(R.id.time1);
                        iseat = (TextView)viewGroup.findViewById(R.id.iseat1);

                        if(check[0]%2 == 0) {
                            btn1.setBackgroundResource(R.drawable.flip_purple);
                            iseat.setText("못먹었어요");
                            when.setTextColor(Color.parseColor("#ffffff"));
                            time.setTextColor(Color.parseColor("#ffffff"));
                            iseat.setTextColor(Color.parseColor("#ffffff"));
                        }
                        else {
                            btn1.setBackgroundResource(R.drawable.flip_white);
                            iseat.setText("먹었어요");
                            when.setTextColor(Color.parseColor("#776DE0"));
                            time.setTextColor(Color.parseColor("#776DE0"));
                            iseat.setTextColor(Color.parseColor("#776DE0"));
                        }
                        break ;

                    case R.id.flipbtn2 :
                        check[1]++;
                        when = (TextView)viewGroup.findViewById(R.id.when2);
                        time = (TextView)viewGroup.findViewById(R.id.time2);
                        iseat = (TextView)viewGroup.findViewById(R.id.iseat2);
                        if(check[1]%2 == 0) {
                            btn2.setBackgroundResource(R.drawable.flip_purple);
                            iseat.setText("못먹었어요");
                            when.setTextColor(Color.parseColor("#ffffff"));
                            time.setTextColor(Color.parseColor("#ffffff"));
                            iseat.setTextColor(Color.parseColor("#ffffff"));
                        }
                        else{
                            btn2.setBackgroundResource(R.drawable.flip_white);
                            iseat.setText("먹었어요");
                            when.setTextColor(Color.parseColor("#776DE0"));
                            time.setTextColor(Color.parseColor("#776DE0"));
                            iseat.setTextColor(Color.parseColor("#776DE0"));
                        }
                        break ;

                    case R.id.flipbtn3 :
                        check[2]++;
                        when = (TextView)viewGroup.findViewById(R.id.when3);
                        time = (TextView)viewGroup.findViewById(R.id.time3);
                        iseat = (TextView)viewGroup.findViewById(R.id.iseat3);
                        if(check[2]%2 == 0){
                            btn3.setBackgroundResource(R.drawable.flip_purple);
                            iseat.setText("못먹었어요");
                            when.setTextColor(Color.parseColor("#ffffff"));
                            time.setTextColor(Color.parseColor("#ffffff"));
                            iseat.setTextColor(Color.parseColor("#ffffff"));
                        }
                        else{
                            btn3.setBackgroundResource(R.drawable.flip_white);
                            iseat.setText("먹었어요");
                            when.setTextColor(Color.parseColor("#776DE0"));
                            time.setTextColor(Color.parseColor("#776DE0"));
                            iseat.setTextColor(Color.parseColor("#776DE0"));
                        }
                        break ;
                }
            }
        };

        btn1 = viewGroup.findViewById(R.id.flipbtn1);
        btn1.setOnClickListener(onClickListener) ;
        btn2 = viewGroup.findViewById(R.id.flipbtn2);
        btn2.setOnClickListener(onClickListener) ;
        btn3 = viewGroup.findViewById(R.id.flipbtn3);
        btn3.setOnClickListener(onClickListener) ;

        return viewGroup;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void replaceFragment(@NonNull Fragment fragment) {
        /*getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();*/
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
        Log.e(TAG,"값 : " + String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }


}
