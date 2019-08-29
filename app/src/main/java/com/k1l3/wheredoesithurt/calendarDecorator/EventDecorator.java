package com.k1l3.wheredoesithurt.calendarDecorator;

import android.support.v4.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {
    private int color;
    private int index;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color,int index, Collection<CalendarDay> dates, Fragment context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.index = index;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new LineSpan(color, index)); // 날자밑에 점
    }
}
