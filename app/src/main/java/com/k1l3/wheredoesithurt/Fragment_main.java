package com.k1l3.wheredoesithurt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class Fragment_main extends Fragment {
    private View viewGroup;
    public int[] check = new int[3];
    private EditText medicine_search;
    private ViewGroup btn1, btn2, btn3;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ListView my_medicine_info;
    private int currentCount = 0;
    private SpannableString spannableString;
    private Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).toolbar_main();
        medicine_search = (EditText) viewGroup.findViewById(R.id.medicin_search);
        medicine_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        medicine_search.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        medicine_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!medicine_search.getText().toString().equals("")) {
                    Fragment fragment_search = new Fragment_search();
                    Bundle bundle = new Bundle();
                    bundle.putString("search_word", medicine_search.getText().toString());
                    fragment_search.setArguments(bundle);
                    replaceFragment(fragment_search);
                } else {
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
                    case R.id.flipbtn1:
                        check[0]++;
                        when = viewGroup.findViewById(R.id.when1);
                        time = viewGroup.findViewById(R.id.time1);
                        iseat = viewGroup.findViewById(R.id.iseat1);

                        if (check[0] % 2 == 0) {
                            btn1.setBackgroundResource(R.drawable.flip_purple);
                            iseat.setText("못먹었어요");
                            when.setTextColor(Color.parseColor("#ffffff"));
                            time.setTextColor(Color.parseColor("#ffffff"));
                            iseat.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            btn1.setBackgroundResource(R.drawable.flip_white);
                            iseat.setText("먹었어요");
                            when.setTextColor(Color.parseColor("#776DE0"));
                            time.setTextColor(Color.parseColor("#776DE0"));
                            iseat.setTextColor(Color.parseColor("#776DE0"));
                        }
                        break;
                    case R.id.flipbtn2:
                        check[1]++;
                        when = viewGroup.findViewById(R.id.when2);
                        time = viewGroup.findViewById(R.id.time2);
                        iseat = viewGroup.findViewById(R.id.iseat2);
                        if (check[1] % 2 == 0) {
                            btn2.setBackgroundResource(R.drawable.flip_purple);
                            iseat.setText("못먹었어요");
                            when.setTextColor(Color.parseColor("#ffffff"));
                            time.setTextColor(Color.parseColor("#ffffff"));
                            iseat.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            btn2.setBackgroundResource(R.drawable.flip_white);
                            iseat.setText("먹었어요");
                            when.setTextColor(Color.parseColor("#776DE0"));
                            time.setTextColor(Color.parseColor("#776DE0"));
                            iseat.setTextColor(Color.parseColor("#776DE0"));
                        }
                        break;
                    case R.id.flipbtn3:
                        check[2]++;
                        when = viewGroup.findViewById(R.id.when3);
                        time = viewGroup.findViewById(R.id.time3);
                        iseat = viewGroup.findViewById(R.id.iseat3);
                        if (check[2] % 2 == 0) {
                            btn3.setBackgroundResource(R.drawable.flip_purple);
                            iseat.setText("못먹었어요");
                            when.setTextColor(Color.parseColor("#ffffff"));
                            time.setTextColor(Color.parseColor("#ffffff"));
                            iseat.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            btn3.setBackgroundResource(R.drawable.flip_white);
                            iseat.setText("먹었어요");
                            when.setTextColor(Color.parseColor("#776DE0"));
                            time.setTextColor(Color.parseColor("#776DE0"));
                            iseat.setTextColor(Color.parseColor("#776DE0"));
                        }
                        break;
                }
            }
        };

        btn1 = viewGroup.findViewById(R.id.flipbtn1);
        btn1.setOnClickListener(onClickListener);
        btn2 = viewGroup.findViewById(R.id.flipbtn2);
        btn2.setOnClickListener(onClickListener);
        btn3 = viewGroup.findViewById(R.id.flipbtn3);
        btn3.setOnClickListener(onClickListener);

        my_medicine_info =  viewGroup.findViewById(R.id.my_medicine_info);
        adapter=new Adapter();
        getMedicineName();
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
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
        Log.e(TAG, "값 : " + String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }

    private void getMedicineName() {
        User user = User.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        if (user.getPrescriptions().size()!=0) {
            while (true) {
                int startYear = Integer.valueOf(user.getPrescriptions().get(currentCount).getBegin().substring(0, 4));
                int startMonth = Integer.valueOf(user.getPrescriptions().get(currentCount).getBegin().substring(5, 7));
                int startDay = Integer.valueOf(user.getPrescriptions().get(currentCount).getBegin().substring(8, 10));
                int endYear = Integer.valueOf(user.getPrescriptions().get(currentCount).getEnd().substring(0, 4));
                int endMonth = Integer.valueOf(user.getPrescriptions().get(currentCount).getEnd().substring(5, 7));
                int endDay = Integer.valueOf(user.getPrescriptions().get(currentCount).getEnd().substring(8, 10));
                Calendar cal = Calendar.getInstance();
                if (cal.get(Calendar.YEAR) >= startYear && cal.get(Calendar.YEAR) <= endYear) {
                    if (cal.get(Calendar.MONTH) + 1 >= startMonth && cal.get(Calendar.MONTH) + 1 <= endMonth) {
                        if (cal.get(Calendar.DATE) >= startDay && cal.get(Calendar.DATE) <= endDay) {
                            for (int i = 0; i < user.getPrescriptions().get(currentCount).getMedicines().size(); i++) {

                                adapter.addItem(user.getPrescriptions().get(currentCount).getMedicines().get(i));
                            }
                            my_medicine_info.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(my_medicine_info);
                            break;
                        } else {
                            currentCount++;
                        }
                    } else {
                        currentCount++;
                    }
                } else {
                    currentCount++;
                }
            }
        }
    }

    private class Adapter extends BaseAdapter {
        ArrayList<Medicine> items = new ArrayList<Medicine>();

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

        public void addItem(Medicine item) {
            items.add(item);
        }

        public void addItem(ArrayList<Medicine> item) {
            items = item;
        }

        public void deleteItem(Medicine item) {
            items.remove(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Medicine_infoView view = new Medicine_infoView(viewGroup.getContext());
            Medicine item = items.get(position);
            view.setMedicine_name(item.getName());
            view.setMedicine_kind(item.getKind());

            return view;
        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
