package com.k1l3.wheredoesithurt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

public class History_itemView extends LinearLayout {
    TextView presc_date, presc_medicine, seperator, medicine_name;
    ImageView history_medicine_image, history_medicine_image2;
    FlexboxLayout flexboxlayout;
    LinearLayout linearLayout, first_layout;
    EditText history_memo;

    public History_itemView(Context context) {
        super(context);
        init(context);
    }

    public History_itemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.history_item, this, true);
        presc_date = findViewById(R.id.presc_date);
        presc_medicine = findViewById(R.id.presc_medicine);
        history_medicine_image = findViewById(R.id.history_medicine_image);
        history_medicine_image2 = findViewById(R.id.history_medicine_image2);
        flexboxlayout = findViewById(R.id.flexboxlayout);
        linearLayout = findViewById(R.id.history_layout);
        first_layout = findViewById(R.id.history_first_layout);
        history_memo = findViewById(R.id.history_memo);
        seperator = findViewById(R.id.history_sep);
        medicine_name = findViewById(R.id.medicine_name);
        setupUI(first_layout);
    }

    //다른화면 클릭시 키보드 제거
    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(history_memo.getWindowToken(), 0);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


    public void setPresc_date(String presc_date) {
        this.presc_date.setText(presc_date);
    }


    public ImageView getHistory_medicine_image() {
        return history_medicine_image;
    }

    public void setHistory_medicine_image(Bitmap history_medicine_image) {
        this.history_medicine_image.setImageBitmap(history_medicine_image);
    }

    public ImageView getHistory_medicine_image2() {
        return history_medicine_image2;
    }

    public void setHistory_medicine_image2(Bitmap history_medicine_image2) {
        this.history_medicine_image2.setImageBitmap(history_medicine_image2);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public FlexboxLayout getFlexboxlayout() {
        return flexboxlayout;
    }

    public void setPresc_medicine(String presc_medicine) {
        this.presc_medicine.setText(presc_medicine);
    }

    public LinearLayout getFirst_layout() {
        return first_layout;
    }

    public EditText getHistory_memo() {
        return history_memo;
    }

    public void setHistory_memo(String history_memo) {
        this.history_memo.setText(history_memo);
    }

    public TextView getSeperator() {
        return seperator;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name.setText(medicine_name);
    }
}
