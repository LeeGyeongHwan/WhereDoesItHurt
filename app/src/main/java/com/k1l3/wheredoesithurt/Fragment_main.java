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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k1l3.wheredoesithurt.models.DayClick;
import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class Fragment_main extends Fragment {
    private View viewGroup;
    public int[] check = new int[3];
    private EditText medicine_search;
    private View btn1, btn2, btn3;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ListView my_medicine_info, my_caution_food;
    private int currentCount = 0;
    private SpannableString spannableString;
    private Adapter adapter;
    private foodAdapter foodAdapter;
    private TextView when1, when2, when3, time1, time2, time3, iseat1, iseat2, iseat3;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String id;
    public Calendar cal = Calendar.getInstance();
    private int currentClick;
    private CircleProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).toolbar_main();
        medicine_search = (EditText) viewGroup.findViewById(R.id.medicin_search);
        medicine_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        medicine_search.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        when1 = viewGroup.findViewById(R.id.when1);
        time1 = viewGroup.findViewById(R.id.time1);
        iseat1 = viewGroup.findViewById(R.id.iseat1);
        when2 = viewGroup.findViewById(R.id.when2);
        time2 = viewGroup.findViewById(R.id.time2);
        iseat2 = viewGroup.findViewById(R.id.iseat2);
        when3 = viewGroup.findViewById(R.id.when3);
        time3 = viewGroup.findViewById(R.id.time3);
        iseat3 = viewGroup.findViewById(R.id.iseat3);
        btn1 = viewGroup.findViewById(R.id.flipbtn1);
        btn2 = viewGroup.findViewById(R.id.flipbtn2);
        btn3 = viewGroup.findViewById(R.id.flipbtn3);
        progressBar = viewGroup.findViewById(R.id.progress_bar);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        id = ((MainActivity) getActivity()).getId();

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

        //약 이름
        my_medicine_info = viewGroup.findViewById(R.id.my_medicine_info);
        adapter = new Adapter();
        getMedicineName();

        //주의해야할 음식
        my_caution_food = viewGroup.findViewById(R.id.my_catuion_food);
        foodAdapter = new foodAdapter();
        getCautionFood();

        //버튼
        setButton();

        //그래프
        setGraph();
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

    //주의해야할 음식
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
                            //setListViewHeightBasedOnChildren(my_caution_food);
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

    //나의 약 정보 Adapter
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

    //주의해야할 음식 Adapter
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

    //버튼
    private void setButton() {
        User user = User.getInstance();
        if (user.getPrescriptions().size() != 0) {
            if (user.getPrescriptions().get(currentCount).getTimes().getTimes().size() == 2) { //알람시간 2개
                btn3.setVisibility(View.INVISIBLE);
                int get_0 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(0).substring(3, 5));
                int get_1 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(1).substring(3, 5));
                setButtonDate(user, get_0, when1, time1, 0);
                setButtonDate(user, get_1, when2, time2, 1);
            } else if (user.getPrescriptions().get(currentCount).getTimes().getTimes().size() == 1) { //알람시간 1개
                btn2.setVisibility(View.INVISIBLE);
                btn3.setVisibility(View.INVISIBLE);

                int get_0 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(0).substring(3, 5));
                setButtonDate(user, get_0, when1, time1, 0);
            } else if (user.getPrescriptions().get(currentCount).getTimes().getTimes().size() == 3) { //알람시간 3개
                int get_0 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(0).substring(3, 5));
                int get_1 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(1).substring(3, 5));
                int get_2 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(2).substring(3, 5));
                setButtonDate(user, get_0, when1, time1, 0);
                setButtonDate(user, get_1, when2, time2, 1);
                setButtonDate(user, get_2, when3, time3, 2);
            }
            buttonClick(btn1, 0);
            buttonClick(btn2, 1);
            buttonClick(btn3, 2);
        }
    }

    //버튼 날짜,종류 설정
    private void setButtonDate(User user, int get_0, TextView when, TextView time, int i) {
        if (user.getPrescriptions().get(currentCount).getTimes().getTimes().get(i).substring(0, 2).equals("AM")) {
            if (get_0 >= 5 && get_0 <= 9) {
                when.setText("아침");
            } else if (get_0 >= 10) {
                when.setText("점심");
            } else if (get_0 < 5) {
                when.setText("저녁");
            }
        } else {
            if (get_0 == 12 || get_0 <= 4) {
                when.setText("점심");
            } else if (get_0 > 4) {
                when.setText("저녁");
            }
        }
        time.setText(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(i).substring(3, 8));
    }

    //Button click이벤트
    public void buttonClick(View fliptbtn1, final int i) {
        databaseReference = database.getReference("users").child(id).child("prescriptions").child(String.valueOf(currentCount))
                .child(String.valueOf(cal.get(Calendar.YEAR))).child(String.valueOf(cal.get(Calendar.MONTH) + 1)).child(String.valueOf(cal.get(Calendar.DATE)));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DayClick item = dataSnapshot.getValue(DayClick.class);
                if (item != null) {
                    for (int i = 0; i < 3; i++) {
                        if (i == 0) {
                            check[i] = item.getOne();
                            if (check[i] == 0) {
                                btn1.setBackgroundResource(R.drawable.flip_purple);
                                iseat1.setText("못먹었어요");
                                when1.setTextColor(Color.parseColor("#ffffff"));
                                time1.setTextColor(Color.parseColor("#ffffff"));
                                iseat1.setTextColor(Color.parseColor("#ffffff"));
                            } else {
                                btn1.setBackgroundResource(R.drawable.flip_white);
                                iseat1.setText("먹었어요");
                                when1.setTextColor(Color.parseColor("#776DE0"));
                                time1.setTextColor(Color.parseColor("#776DE0"));
                                iseat1.setTextColor(Color.parseColor("#776DE0"));
                            }
                        } else if (i == 1) {
                            check[i] = item.getTwo();
                            if (check[i] == 0) {
                                btn2.setBackgroundResource(R.drawable.flip_purple);
                                iseat2.setText("못먹었어요");
                                when2.setTextColor(Color.parseColor("#ffffff"));
                                time2.setTextColor(Color.parseColor("#ffffff"));
                                iseat2.setTextColor(Color.parseColor("#ffffff"));
                            } else {
                                btn2.setBackgroundResource(R.drawable.flip_white);
                                iseat2.setText("먹었어요");
                                when2.setTextColor(Color.parseColor("#776DE0"));
                                time2.setTextColor(Color.parseColor("#776DE0"));
                                iseat2.setTextColor(Color.parseColor("#776DE0"));
                            }
                        } else if (i == 2) {
                            check[i] = item.getThree();
                            if (check[i] == 0) {
                                btn3.setBackgroundResource(R.drawable.flip_purple);
                                iseat3.setText("못먹었어요");
                                when3.setTextColor(Color.parseColor("#ffffff"));
                                time3.setTextColor(Color.parseColor("#ffffff"));
                                iseat3.setTextColor(Color.parseColor("#ffffff"));
                            } else {
                                btn3.setBackgroundResource(R.drawable.flip_white);
                                iseat3.setText("먹었어요");
                                when3.setTextColor(Color.parseColor("#776DE0"));
                                time3.setTextColor(Color.parseColor("#776DE0"));
                                iseat3.setTextColor(Color.parseColor("#776DE0"));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fliptbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clicking = 0;
                if (i == 0) {
                    if (check[i] % 2 == 1) {
                        btn1.setBackgroundResource(R.drawable.flip_purple);
                        iseat1.setText("못먹었어요");
                        when1.setTextColor(Color.parseColor("#ffffff"));
                        time1.setTextColor(Color.parseColor("#ffffff"));
                        iseat1.setTextColor(Color.parseColor("#ffffff"));
                        check[i]--;
                        clicking--;
                    } else {
                        btn1.setBackgroundResource(R.drawable.flip_white);
                        iseat1.setText("먹었어요");
                        when1.setTextColor(Color.parseColor("#776DE0"));
                        time1.setTextColor(Color.parseColor("#776DE0"));
                        iseat1.setTextColor(Color.parseColor("#776DE0"));
                        check[i]++;
                        clicking++;
                    }
                } else if (i == 1) {
                    if (check[i] % 2 == 1) {
                        btn2.setBackgroundResource(R.drawable.flip_purple);
                        iseat2.setText("못먹었어요");
                        when2.setTextColor(Color.parseColor("#ffffff"));
                        time2.setTextColor(Color.parseColor("#ffffff"));
                        iseat2.setTextColor(Color.parseColor("#ffffff"));
                        check[i]--;
                        clicking--;
                    } else {
                        btn2.setBackgroundResource(R.drawable.flip_white);
                        iseat2.setText("먹었어요");
                        when2.setTextColor(Color.parseColor("#776DE0"));
                        time2.setTextColor(Color.parseColor("#776DE0"));
                        iseat2.setTextColor(Color.parseColor("#776DE0"));
                        check[i]++;
                        clicking++;
                    }
                } else if (i == 2) {
                    if (check[i] % 2 == 1) {
                        btn3.setBackgroundResource(R.drawable.flip_purple);
                        iseat3.setText("못먹었어요");
                        when3.setTextColor(Color.parseColor("#ffffff"));
                        time3.setTextColor(Color.parseColor("#ffffff"));
                        iseat3.setTextColor(Color.parseColor("#ffffff"));
                        check[i]--;
                        clicking--;
                    } else {
                        btn3.setBackgroundResource(R.drawable.flip_white);
                        iseat3.setText("먹었어요");
                        when3.setTextColor(Color.parseColor("#776DE0"));
                        time3.setTextColor(Color.parseColor("#776DE0"));
                        iseat3.setTextColor(Color.parseColor("#776DE0"));
                        check[i]++;
                        clicking++;
                    }
                }
                buttonDatabase(check, clicking);
            }
        });
    }

    //Button database
    private void buttonDatabase(final int[] check, final int clicking) {
        databaseReference = database.getReference("users").child(id).child("prescriptions").child(String.valueOf(currentCount))
                .child(String.valueOf(cal.get(Calendar.YEAR))).child(String.valueOf(cal.get(Calendar.MONTH) + 1)).child(String.valueOf(cal.get(Calendar.DATE)));
        ValueEventListener databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DayClick dayClick = new DayClick(check[0], check[1], check[2]);
                databaseReference.setValue(dayClick);
                buttonTotalDB(clicking);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(databaseListener);
    }

    //총클릭횟수 DB업데이트
    private void buttonTotalDB(final int clicking) {
        databaseReference = database.getReference("users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User info = dataSnapshot.getValue(User.class);
                currentClick = info.getPrescriptions().get(currentCount).getTotalClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("totalClick", clicking + currentClick);
        database.getReference("users").child(id).child("prescriptions").child(String.valueOf(currentCount)).updateChildren(taskMap);
    }

    private void setGraph() {
        databaseReference = database.getReference("users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User info = dataSnapshot.getValue(User.class);
                currentClick = info.getPrescriptions().get(currentCount).getTotalClick();
                progressBar.setMax(100);
                int currentProgress = (currentClick * 100) / (info.getPrescriptions().get(currentCount).getMedicines().get(0).getNumberOfDay() *
                        info.getPrescriptions().get(currentCount).getMedicines().get(0).getNumberOfDoses());
                progressBar.setProgress(currentProgress);
                Log.e("asdg", String.valueOf(currentProgress));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
