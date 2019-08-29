package com.k1l3.wheredoesithurt;

import android.graphics.Color;
import android.os.AsyncTask;
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

import com.k1l3.wheredoesithurt.calendarDecorator.EventDecorator;
import com.k1l3.wheredoesithurt.calendarDecorator.OneDayDecorator;
import com.k1l3.wheredoesithurt.calendarDecorator.SaturdayDecorator;
import com.k1l3.wheredoesithurt.calendarDecorator.SundayDecorator;
import com.k1l3.wheredoesithurt.models.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import static android.support.constraint.Constraints.TAG;

public class Fragment_calendar extends Fragment {
    private View viewGroup;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private MaterialCalendarView materialCalendarView;
    private OneDayDecorator selectedDayDecorator = new OneDayDecorator();
    private User user = User.getInstance();
    private ArrayList<String> result = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_calendar, container, false);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        ((MainActivity) getActivity()).toolbar_calendar();

        extractDate();

        materialCalendarView = viewGroup.findViewById(R.id.calendarView);

        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), selectedDayDecorator);

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDayDecorator.setDate(date.getDate());
            }
        });

        return viewGroup;
    }

    private void extractDate() {
        for (int i = 0; i < user.getPrescriptions().size(); ++i) {
            Calendar calendar = Calendar.getInstance();

            String startDay = user.getPrescriptions().get(i).getBegin();
            String endDay = user.getPrescriptions().get(i).getEnd();

            String date = startDay;

            while (date.compareTo(endDay) <= 0) {
                Log.d(TAG, "extractDate: " + endDay);

                result.add(date);

                String[] time = date.split("\\.");

                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);

                calendar.set(year, month - 1, day);

                calendar.add(Calendar.DATE, 1);

                date = dateFormat.format(calendar.getTime());
            }
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        ArrayList<String> Time_Result;

        ApiSimulator(ArrayList<String> Time_Result) {
            this.Time_Result = Time_Result;
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

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, Fragment_calendar.this));
        }

    }

}
