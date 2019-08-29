package com.k1l3.wheredoesithurt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.k1l3.wheredoesithurt.models.DayClick;
import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_main extends Fragment {
    private final Calendar calendar = Calendar.getInstance();
    private final User user = User.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final String today = dateFormat.format(new Date());
    private final int todayYear = calendar.get(Calendar.YEAR);
    private final int todayMonth = calendar.get(Calendar.MONTH) + 1;
    private final int todayDay = calendar.get(Calendar.DATE);

    private View viewGroup;
    private View btn1, btn2, btn3;
    private EditText medicine_search;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ListView my_medicine_info, my_caution_food;
    private Adapter adapter;
    private foodAdapter foodAdapter;
    private TextView when1, when2, when3, time1, time2, time3, iseat1, iseat2, iseat3, presc_name;
    private CircleProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private LinearLayout counting_linear;
    private int currentClick;
    private int availCounting = 0;
    private int currentAvail = 0;
    private int currentCount = 0;
    private float pressedX = 0;
    private int[] check = new int[3];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).toolbar_main();
        medicine_search = viewGroup.findViewById(R.id.medicin_search);
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
        presc_name = viewGroup.findViewById(R.id.presc_name);
        counting_linear = viewGroup.findViewById(R.id.counting_layout);
        progressBar = viewGroup.findViewById(R.id.progress_bar);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        constraintLayout = viewGroup.findViewById(R.id.constraint_layout);


        int NotiCheck=(((MainActivity) getActivity()).getFragment());
        int NotiMed=(((MainActivity) getActivity()).getMedNum());

        if((NotiCheck!=-1)&&(NotiMed!=-1)){
            for(int i=0;i<NotiMed;i++){
                getRightCurrentCount();
                if (currentAvail < availCounting - 1) {
                    currentAvail++;
                }
                currentImage();

                my_medicine_info = viewGroup.findViewById(R.id.my_medicine_info);
                adapter = new Adapter();
                getMedicineName();

                //주의해야할 음식
                my_caution_food = viewGroup.findViewById(R.id.my_catuion_food);
                foodAdapter = new foodAdapter();
                getCautionFood();

                setButton();

                setGraph();

                setPrescriptionName();
            }
            currentAvail=NotiMed;
            currentImage();
            Log.d("what", "onCreateView: 프래그먼트 진입 notiMed : "+NotiMed+", noticheck : "+NotiCheck);
            int clicking=0;
            switch (NotiCheck) {
                case 0:
                    btn1.setBackgroundResource(R.drawable.flip_white);
                    iseat1.setText("먹었어요");
                    when1.setTextColor(Color.parseColor("#776DE0"));
                    time1.setTextColor(Color.parseColor("#776DE0"));
                    iseat1.setTextColor(Color.parseColor("#776DE0"));
                    check[0]++;
                    clicking++;
                    currentClick++;
                    break;
                case 1:
                    btn2.setBackgroundResource(R.drawable.flip_white);
                    iseat2.setText("먹었어요");
                    when2.setTextColor(Color.parseColor("#776DE0"));
                    time2.setTextColor(Color.parseColor("#776DE0"));
                    iseat2.setTextColor(Color.parseColor("#776DE0"));
                    check[1]++;
                    clicking++;
                    currentClick++;
                    break;
                case 2:
                    btn3.setBackgroundResource(R.drawable.flip_white);
                    iseat3.setText("먹었어요");
                    when3.setTextColor(Color.parseColor("#776DE0"));
                    time3.setTextColor(Color.parseColor("#776DE0"));
                    iseat3.setTextColor(Color.parseColor("#776DE0"));
                    check[2]++;
                    clicking++;
                    currentClick++;
            }

            buttonDatabase(check, clicking);
            Log.d("what", "onClick: buttondatabase");
        }




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
        return viewGroup;
    }

    @Override
    public void onStart() {
        Log.d("what", "Fragment main onStart: ");

        if (User.getInstance().getPrescriptions().size() != 0) {
            if (currentCount == -1) {
                getRightCurrentCount();
            }

            getAvailable();

            currentImage();

            if (user.getPrescriptions() != null) {
                setGraph();
                setPrescriptionName();
            }

            //드래그시 화면이동
            constraintLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float distance = 0;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: // 손가락을 touch 했을 떄 x 좌표값 저장
                            pressedX = event.getX();
                            break;
                        case MotionEvent.ACTION_UP: // 손가락을 떼었을 때 저장해놓은 x좌표와의 거리 비교
                            distance = pressedX - event.getX();
                            break;
                    } // 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
                    if (Math.abs(distance) < 100 || distance == 0) {
                    }
                    if (distance > 0) {// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                        getRightCurrentCount();
                        if (currentAvail < availCounting - 1) {
                            currentAvail++;
                        }
                        currentImage();

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

                        //이름
                        setPrescriptionName();

                    } else if (distance < 0) {
                        getLeftCurrentCount();
                        if (availCounting >= currentAvail && currentAvail > 0) {
                            currentAvail--;
                        }
                        currentImage();

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

                        //이름
                        setPrescriptionName();
                    }
                    return true;
                }
            });


            buttonClick(btn1, 0);
            buttonClick(btn2, 1);
            buttonClick(btn3, 2);

            my_medicine_info = viewGroup.findViewById(R.id.my_medicine_info);
            adapter = new Adapter();
            getMedicineName();

            //주의해야할 음식
            my_caution_food = viewGroup.findViewById(R.id.my_catuion_food);
            foodAdapter = new foodAdapter();
            getCautionFood();

            //버튼
            setButton();
            setGraph();

            //화면 이동 확인 이미지
            counting_linear = viewGroup.findViewById(R.id.counting_layout);

            getAvailable();

            currentImage();
        }

        super.onStart();
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }

    private void getMedicineName() {
        if (user.getPrescriptions().size() != 0) {
            while (true) {
                String startDay = user.getPrescriptions().get(currentCount).getBegin();
                String endDay = user.getPrescriptions().get(currentCount).getEnd();

                boolean todayInRange = today.compareTo(startDay) >= 0 && today.compareTo(endDay) <= 0;

                if (todayInRange) {
                    for (int i = 0; i < user.getPrescriptions().get(currentCount).getMedicines().size(); i++) {
                        adapter.addItem(user.getPrescriptions().get(currentCount).getMedicines().get(i));
                    }
                    my_medicine_info.setAdapter(adapter);
                    break;
                } else {
                    currentCount++;
                }

            }
        }
    }

    //주의해야할 음식
    private void getCautionFood() {
        if (user.getPrescriptions().size() != 0) {
            while (true) {
                String startDay = user.getPrescriptions().get(currentCount).getBegin();
                String endDay = user.getPrescriptions().get(currentCount).getEnd();

                boolean todayInRange = today.compareTo(startDay) >= 0 && today.compareTo(endDay) <= 0;

                if (todayInRange) {
                    for (int i = 0; i < user.getPrescriptions().get(currentCount).getMedicines().size(); i++) {
                        foodAdapter.addItem(user.getPrescriptions().get(currentCount).getMedicines().get(i));
                    }

                    my_caution_food.setAdapter(foodAdapter);

                    break;
                } else {
                    currentCount++;
                }
            }
        }
    }

    //버튼
    private void setButton() {
        if (user.getPrescriptions().size() != 0) {
            if (user.getPrescriptions().get(currentCount).getTimes().getTimes().size() == 2) { //알람시간 2개
                btn3.setVisibility(View.INVISIBLE);
                int get_0 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(0).substring(0, 2));
                int get_1 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(1).substring(0, 2));
                setButtonDate(get_0, when1, time1, 0);
                setButtonDate(get_1, when2, time2, 1);
            } else if (user.getPrescriptions().get(currentCount).getTimes().getTimes().size() == 1) { //알람시간 1개
                btn2.setVisibility(View.INVISIBLE);
                btn3.setVisibility(View.INVISIBLE);

                int get_0 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(0).substring(0, 2));
                setButtonDate(get_0, when1, time1, 0);
            } else if (user.getPrescriptions().get(currentCount).getTimes().getTimes().size() == 3) { //알람시간 3개
                int get_0 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(0).substring(0, 2));
                int get_1 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(1).substring(0, 2));
                int get_2 = Integer.valueOf(user.getPrescriptions().get(currentCount).getTimes().getTimes().get(2).substring(0, 2));
                setButtonDate(get_0, when1, time1, 0);
                setButtonDate(get_1, when2, time2, 1);
                setButtonDate(get_2, when3, time3, 2);
            }
        }
    }

    //버튼 날짜,종류 설정
    private void setButtonDate(int get_0, TextView when, TextView time, int i) {
        String AMPM;
        if (get_0 > 12) {
            get_0 = get_0 - 12;
            AMPM = "PM";
        } else if (get_0 == 12) {
            AMPM = "PM";
        } else {
            AMPM = "AM";
        }
        if (AMPM.equals("AM")) {
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
        time.setText(get_0 + ":" + user.getPrescriptions().get(currentCount).getTimes().getTimes().get(i).substring(2, 4));

        DayClick item = user.getPrescriptions().get(currentCount)
                .getYear()
                .getMonths().get(calendar.get(Calendar.MONTH) - 1)
                .getDays().get(calendar.get(Calendar.DATE) - 1)
                .getDayClick();

        for (int index = 0; index < 3; index++) {
            if (index == 0) {
                check[index] = item.getOne();
                if (check[index] == 0) {
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
            } else if (index == 1) {
                check[index] = item.getTwo();
                if (check[index] == 0) {
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
            } else if (index == 2) {
                check[index] = item.getThree();
                if (check[index] == 0) {
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

    //Button click이벤트
    public void buttonClick(View fliptbtn1, final int i) {
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
                        currentClick--;
                    } else {
                        btn1.setBackgroundResource(R.drawable.flip_white);
                        iseat1.setText("먹었어요");
                        when1.setTextColor(Color.parseColor("#776DE0"));
                        time1.setTextColor(Color.parseColor("#776DE0"));
                        iseat1.setTextColor(Color.parseColor("#776DE0"));
                        check[i]++;
                        clicking++;
                        currentClick++;
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
                        currentClick--;
                    } else {
                        btn2.setBackgroundResource(R.drawable.flip_white);
                        iseat2.setText("먹었어요");
                        when2.setTextColor(Color.parseColor("#776DE0"));
                        time2.setTextColor(Color.parseColor("#776DE0"));
                        iseat2.setTextColor(Color.parseColor("#776DE0"));
                        check[i]++;
                        clicking++;
                        currentClick++;
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
                        currentClick--;
                    } else {
                        btn3.setBackgroundResource(R.drawable.flip_white);
                        iseat3.setText("먹었어요");
                        when3.setTextColor(Color.parseColor("#776DE0"));
                        time3.setTextColor(Color.parseColor("#776DE0"));
                        iseat3.setTextColor(Color.parseColor("#776DE0"));
                        check[i]++;
                        clicking++;
                        currentClick++;
                    }
                }

                buttonDatabase(check, clicking);

            }
        });
    }

    //Button database
    private void buttonDatabase(final int[] check, final int clicking) {
        DayClick dayClick = new DayClick(check[0], check[1], check[2]);

        user.getPrescriptions().get(currentCount)
                .getYear()
                .getMonths().get(calendar.get(Calendar.MONTH) - 1)
                .getDays().get(calendar.get(Calendar.DATE) - 1)
                .setDayClick(dayClick);

        currentClick = user.getPrescriptions().get(currentCount).getTotalClick();

        user.getPrescriptions().get(currentCount).setTotalClick(currentClick + clicking);

        user.syncWithDatabase();

        setGraph();
    }

    private void setGraph() {
        if (user.getPrescriptions().size() != 0) {
            currentClick = user.getPrescriptions().get(currentCount).getTotalClick();
            progressBar.setMax(100);

            int currentProgress = (currentClick * 100) /
                    (user.getPrescriptions().get(currentCount).getMedicines().get(0).getNumberOfDay() *
                            user.getPrescriptions().get(currentCount).getMedicines().get(0).getNumberOfDoses());

            progressBar.setProgress(currentProgress);
        }
    }

    private void getRightCurrentCount() {
        if (currentCount < user.getPrescriptions().size() - 1) {
            if (user.getPrescriptions() != null) {
                for (int i = currentCount + 1; i < user.getPrescriptions().size(); i++) {
                    String startDay = user.getPrescriptions().get(i).getBegin();
                    String endDay = user.getPrescriptions().get(i).getEnd();

                    boolean todayInRange = today.compareTo(startDay) >= 0 && today.compareTo(endDay) <= 0;

                    if (todayInRange) {
                        currentCount = i;
                        break;
                    }
                }
            }
        }
    }

    private void getLeftCurrentCount() {
        if (currentCount > 0) {
            currentCount = currentCount - 1;

            if (user.getPrescriptions() != null) {
                for (int i = currentCount; i >= 0; i--) {
                    String startDay = user.getPrescriptions().get(i).getBegin();
                    String endDay = user.getPrescriptions().get(i).getEnd();

                    boolean todayInRange = today.compareTo(startDay) >= 0 && today.compareTo(endDay) <= 0;

                    if (todayInRange) {
                        currentCount = i;
                        break;
                    }
                }
            }
        }
    }

    private void getAvailable() {
        availCounting = 0;

        if (user.getPrescriptions() != null) {
            for (int i = 0; i < user.getPrescriptions().size(); i++) {
                String startDay = user.getPrescriptions().get(i).getBegin();
                String endDay = user.getPrescriptions().get(i).getEnd();

                boolean todayInRange = today.compareTo(startDay) >= 0 && today.compareTo(endDay) <= 0;

                if (todayInRange) {
                    availCounting++;
                }
            }
        }
    }

    private void currentImage() {
        counting_linear.removeAllViews();

        for (int i = 0; i < availCounting; i++) {
            if (i == currentAvail) {
                ImageView image = new ImageView(getContext());
                Bitmap slid_full = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.slide_fill);
                image.setImageBitmap(slid_full);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(20, 20);
                parms.setMargins(2, 0, 2, 0);
                image.setLayoutParams(parms);
                counting_linear.addView(image);
            } else {
                ImageView image = new ImageView(getContext());
                Bitmap slid_empty = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.slide_empty);
                image.setImageBitmap(slid_empty);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(20, 20);
                parms.setMargins(2, 0, 2, 0);
                image.setLayoutParams(parms);
                counting_linear.addView(image);
            }
        }
    }

    private void setPrescriptionName() {
        presc_name.setText(user.getPrescriptions().get(currentCount).getName());
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
}
