package com.k1l3.wheredoesithurt;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

public class History_itemView extends LinearLayout {
    TextView presc_date,presc_medicine;
    Button button;
    ImageView history_medicine_image,history_medicine_image2;
    FlexboxLayout flexboxlayout;
    LinearLayout linearLayout,first_layout;
    EditText history_memo;
    public History_itemView(Context context) {
        super(context);
        init(context);
    }

    public History_itemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.history_item,this,true);
        presc_date = (TextView)findViewById(R.id.presc_date);
        presc_medicine = (TextView)findViewById(R.id.presc_medicine);
        button = (Button)findViewById(R.id.history_button);
        history_medicine_image = (ImageView)findViewById(R.id.history_medicine_image);
        history_medicine_image2 = (ImageView)findViewById(R.id.history_medicine_image2);
        flexboxlayout = (FlexboxLayout)findViewById(R.id.flexboxlayout);
        linearLayout = (LinearLayout)findViewById(R.id.history_layout);
        first_layout = (LinearLayout)findViewById(R.id.history_first_layout);
        history_memo = (EditText)findViewById(R.id.history_memo);
        setupUI(first_layout);
    }
    //다른화면 클릭시 키보드 제거
    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager im = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(history_memo.getWindowToken(),0);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(innerView);
                }
            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public TextView getPresc_date() {
        return presc_date;
    }

    public void setPresc_date(String presc_date) {
        this.presc_date.setText(presc_date);
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public ImageView getHistory_medicine_image() {
        return history_medicine_image;
    }

    public void setHistory_medicine_image(ImageView history_medicine_image) {
        this.history_medicine_image = history_medicine_image;
    }

    public ImageView getHistory_medicine_image2() {
        return history_medicine_image2;
    }

    public void setHistory_medicine_image2(ImageView history_medicine_image2) {
        this.history_medicine_image2 = history_medicine_image2;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public FlexboxLayout getFlexboxlayout() {
        return flexboxlayout;
    }

    public void setFlexboxlayout(FlexboxLayout flexboxlayout) {
        this.flexboxlayout = flexboxlayout;
    }

    public void setPresc_date(TextView presc_date) {
        this.presc_date = presc_date;
    }

    public TextView getPresc_medicine() {
        return presc_medicine;
    }

    public void setPresc_medicine(String presc_medicine) {
        this.presc_medicine.setText(presc_medicine);
    }

    public EditText getHistory_memo() {
        return history_memo;
    }

    public void setHistory_memo(String history_memo) {
        this.history_memo.setText(history_memo);
    }
}
