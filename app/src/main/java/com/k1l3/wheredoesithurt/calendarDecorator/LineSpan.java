package com.k1l3.wheredoesithurt.calendarDecorator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class LineSpan implements LineBackgroundSpan {
    private final int color;
    private final int index;

    public LineSpan(int color, int index) {
        this.color = color;
        this.index = index;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = paint.getColor();

        if (color != 0) {
            paint.setColor(color);
        }
        canvas.drawRect(left, bottom + index * 10, right, bottom + index * 10 + 10, paint);

        paint.setColor(oldColor);
    }
}
