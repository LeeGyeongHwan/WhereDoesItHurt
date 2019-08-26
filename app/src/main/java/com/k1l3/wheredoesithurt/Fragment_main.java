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
    private ListView my_medicine_info, my_caution_food;
    private int currentCount = 0;
    private SpannableString spannableString;
    private Adapter adapter;
    private foodAdapter foodAdapter;

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
        //약 이름
        my_medicine_info = viewGroup.findViewById(R.id.my_medicine_info);
        adapter = new Adapter();
        getMedicineName();

        //주의해야할 음식
        my_caution_food = viewGroup.findViewById(R.id.my_catuion_food);
        foodAdapter = new foodAdapter();
        getCautionFood();
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
        if (user.getPrescriptions().size() != 0) {
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
                            //setListViewHeightBasedOnChildren(my_medicine_info);
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

    private void getCautionFood() {
        User user = User.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        if (user.getPrescriptions().size() != 0) {
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

                                foodAdapter.addItem(user.getPrescriptions().get(currentCount).getMedicines().get(i));
                            }
                            my_caution_food.setAdapter(foodAdapter);
                            setListViewHeightBasedOnChildren(my_caution_food);
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

    private class foodAdapter extends BaseAdapter {
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
            Caution_foodView view = new Caution_foodView(viewGroup.getContext());
            Medicine item = items.get(position);
            view.setMedicine_name(item.getName());

            int caution = item.getCaution();
            if (caution == 0) {
                view.setFood(" 없음");
            } else {
                for (int i = 0; i < 11; i++) {
                    switch (caution % 2) {
                        case 0:
                            caution = caution / 2;
                            break;
                        case 1:
                            if (i == 0) {//감기
                                view.setFood(view.getFood().getText().toString() + " 초콜릿 커피 카페인");
                            } else if (i == 1) {//철분제
                                view.setFood(view.getFood().getText().toString() + " 녹차 유제품");
                            } else if (i == 2) {//알레르기
                                view.setFood(view.getFood().getText().toString() + " 과일주스");
                            } else if (i == 3) {//통풍
                                view.setFood(view.getFood().getText().toString() + " 고기 생선 조개 시금치 맥주");
                            } else if (i == 4) {//골다공증
                                view.setFood(view.getFood().getText().toString() + " 탄산음료");
                            } else if (i == 5) {//기관지확장제
                                view.setFood(view.getFood().getText().toString() + " 카페인");
                            } else if (i == 6) {//변비
                                view.setFood(view.getFood().getText().toString() + " 우유");
                            } else if (i == 7) {//결핵
                                view.setFood(view.getFood().getText().toString() + " 치즈");
                            } else if (i == 8) {//고혈압
                                view.setFood(view.getFood().getText().toString() + " 자몽주스 바나나 자몽");
                            } else if (i == 9) {//항응고
                                view.setFood(view.getFood().getText().toString() + " 시금치 등 푸른채소");
                            } else if (i == 10) {//빈혈
                                view.setFood(view.getFood().getText().toString() + " 홍차 녹차");
                            }
                            caution = caution / 2;
                            break;
                        default:
                            break;
                    }
                }
            }

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
