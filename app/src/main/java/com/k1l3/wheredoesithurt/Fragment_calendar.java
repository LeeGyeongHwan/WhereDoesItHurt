package com.k1l3.wheredoesithurt;

import android.os.AsyncTask;
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

import com.k1l3.wheredoesithurt.calendar.CalendarItem;
import com.k1l3.wheredoesithurt.calendar.CalendarItemView;
import com.k1l3.wheredoesithurt.calendarDecorator.EventDecorator;
import com.k1l3.wheredoesithurt.calendarDecorator.SaturdayDecorator;
import com.k1l3.wheredoesithurt.calendarDecorator.SundayDecorator;
import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import static android.support.constraint.Constraints.TAG;

public class Fragment_calendar extends Fragment {
    private final int[] colors = {0xFFFFF8BC, 0xFFA2E9FF, 0xFFEDB1F7};
    private View viewGroup;
    private MaterialCalendarView materialCalendarView;
    private User user = User.getInstance();
    private HashMap<String, Integer> indexCache = new HashMap<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private ListView listView;
    private Adaptor adaptor;
    private boolean calendarShow = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_calendar, container, false);
        ((MainActivity) getActivity()).toolbar_calendar();
        listView = viewGroup.findViewById(R.id.calendar_item_list);

        extractDate();

        materialCalendarView = viewGroup.findViewById(R.id.calendarView);

        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                adaptor = new Adaptor(dateFormat.format(date.getDate()));

                addPrescriptionToAdaptor(adaptor, dateFormat.format(date.getDate()));

                listView.setAdapter(adaptor);
            }
        });

        materialCalendarView.setDateSelected(new Date(), true);

        adaptor = new Adaptor(dateFormat.format(new Date().getDate()));

        addPrescriptionToAdaptor(adaptor, dateFormat.format(new Date().getDate()));

        listView.setAdapter(adaptor);

        return viewGroup;
    }

    private void addPrescriptionToAdaptor(Adaptor adaptor, String selectedDate) {
        if (user.getPrescriptions() != null) {
            for (int i = 0; i < user.getPrescriptions().size(); ++i) {
                Prescription prescription = user.getPrescriptions().get(i);

                String startDate = prescription.getBegin();
                String endDate = prescription.getEnd();

                boolean todayInRange = selectedDate.compareTo(startDate) >= 0 && selectedDate.compareTo(endDate) <= 0;

                if (todayInRange) {
                    adaptor.addItem(new CalendarItem(prescription, i % 3));
                }
            }
        }
    }

    private void extractDate() {
        for (int i = 0; i < user.getPrescriptions().size(); ++i) {
            ArrayList<String> result = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();

            String startDay = user.getPrescriptions().get(i).getBegin();
            String endDay = user.getPrescriptions().get(i).getEnd();

            String date = startDay;

            int index = 0;

            while (date.compareTo(endDay) <= 0) {
                if (indexCache.containsKey(date)) {
                    if (index < indexCache.get(date) + 1) {
                        index = indexCache.get(date) + 1;
                    }
                }

                result.add(date);

                String[] time = date.split("\\.");

                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);

                calendar.set(year, month - 1, day);

                calendar.add(Calendar.DATE, 1);

                date = dateFormat.format(calendar.getTime());
            }

            for (int j = 0; j < result.size(); ++j) {
                indexCache.put(result.get(j), index);
            }

            new ApiSimulator(result, index, colors[i % 3]).executeOnExecutor(Executors.newSingleThreadExecutor());
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        ArrayList<String> Time_Result;
        private int index;
        private int color;

        ApiSimulator(ArrayList<String> Time_Result, int index, int color) {
            this.Time_Result = Time_Result;
            this.index = index;
            this.color = color;

            Log.d(TAG, "ApiSimulator: " + index);
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < Time_Result.size(); i++) {
                String[] time = Time_Result.get(i).split("\\.");

                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);

                CalendarDay date = CalendarDay.from(year, month - 1, day);

                dates.add(date);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            materialCalendarView.addDecorator(new EventDecorator(color, index, calendarDays, Fragment_calendar.this));
        }

    }

    public class Adaptor extends BaseAdapter {
        ArrayList<CalendarItem> items = new ArrayList<>();
        String selectedDate;

        public Adaptor(String selectedDate) {
            super();
            this.selectedDate = selectedDate;
        }

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CalendarItemView view = new CalendarItemView(viewGroup.getContext());
            final CalendarItem item = items.get(position);

            view.setHashtags(extractHashtags(item));
            view.setDayCount(calculateDayCount(item.getStartDay(), selectedDate) + 1);
            view.setWholeDayCount(calculateDayCount(item.getStartDay(), item.getEndDay()) + 1);
            view.setCalendarCircle(item.getIndex());

            view.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(calendarShow){
                        materialCalendarView.setVisibility(View.GONE);
                        if(item.getisVisible()){
                            view.getDetailLinearLayout().setVisibility(View.GONE);
                            item.setVisible(false);
                        }else{
                            view.getDetailLinearLayout().setVisibility(View.VISIBLE);
                            item.setVisible(true);
                        }
                    }
//                    if (item.getisVisible()) {
//                        materialCalendarView.setVisibility(View.GONE);
//
//                        //toVisible(view.getDetailLinearLayout());
//                        view.getDetailLinearLayout().setVisibility(View.VISIBLE);
//                        Log.d(TAG, "onClick: " +view);
//                        //calendarShow = false;
//                        item.setVisible(false);
//                    } else {
//                        //materialCalendarView.setVisibility(View.VISIBLE);
//                        view.getDetailLinearLayout().setVisibility(View.GONE);
//                        toGone(view.getDetailLinearLayout());
//                        //calendarShow = true;
//                        item.setVisible(true);
//                    }
                }
            });

            final Adapter adapter = new Adapter();

            for (int i = 0; i < items.get(position).getPrescription().getMedicines().size(); i++) {
                adapter.addItem(items.get(position).getPrescription().getMedicines().get(i));
            }

            view.getMy_medicine_info().setAdapter(adapter);

            return view;
        }

        public void addItem(CalendarItem item) {
            items.add(item);
        }

        private String extractHashtags(CalendarItem item) {
            String hashtags = "";

            if (item.getHashtag() != null) {
                for (int i = 0; i < item.getHashtag().size(); ++i) {
                    String hashtag = item.getHashtag().get(i);
                    if (hashtags.length() + hashtag.length() <= 20) {
                        hashtags += "#" + hashtag + " ";
                    } else {
                        break;
                    }
                }
            }

            Log.d(TAG, "extractHashtags: " + hashtags);

            return hashtags;
        }

        private long calculateDayCount(String beginDate, String endDate) {
            Date begin = new Date();
            Date end = new Date();

            try {
                begin = dateFormat.parse(beginDate);
                end = dateFormat.parse(endDate);
            } catch (ParseException e) {
                Log.e(TAG, "calculateDayCount: ", e);
            }

            return (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
        }

        private void toVisible(ViewGroup layout) {
            layout.setVisibility(View.VISIBLE);

            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);

                child.setVisibility(View.VISIBLE);

                if (child instanceof ViewGroup) {
                    toVisible((ViewGroup) child);
                }
            }
        }

        private void toGone(ViewGroup layout) {
            layout.setVisibility(View.GONE);

            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof ViewGroup) {
                    toGone((ViewGroup) child);
                } else {
                    child.setVisibility(View.GONE);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Medicine_infoView view = new Medicine_infoView(viewGroup.getContext());
            Medicine item = items.get(position);
            view.setMedicine_name(item.getName());
            view.setMedicine_kind(item.getKind());

            return view;
        }
    }

}
