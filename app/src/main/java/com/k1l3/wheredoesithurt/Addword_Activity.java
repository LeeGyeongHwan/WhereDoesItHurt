package com.k1l3.wheredoesithurt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;
import com.k1l3.wheredoesithurt.models.Year;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Addword_Activity extends AppCompatActivity {
    private static final String TAG = "AddwordActivity";
    private ArrayList<String> word_in;
    private TextView textView;
    private EditText add_text_hash;
    private Button saveButton;
    private ImageButton cancelBtn;
    private SpannableString spannableString;
    private FlexboxLayout flexboxLayout, word_flexboxlayout;
    private int[] check = new int[15];
    private Prescription prescription;
    private ArrayList<String> hashtags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        textView = findViewById(R.id.textView);
        flexboxLayout = findViewById(R.id.add_flexboxlayout);
        word_flexboxlayout = findViewById(R.id.word_flexboxlayout);
        saveButton = findViewById(R.id.save_button);
        add_text_hash = findViewById(R.id.add_text_hash);
        cancelBtn = findViewById(R.id.canceladdword);
        spannableString = new SpannableString(textView.getText().toString());
        hashtags = new ArrayList<>();

        Intent intent = getIntent();
        prescription = (Prescription) intent.getSerializableExtra("prescription");
        final Boolean isDrinking = intent.getBooleanExtra("isDrinking",false);
        final Boolean isSmoking = intent.getBooleanExtra("isSmoking",false);

        Custom_text("처방전");
        Custom_text("단어");
        textView.setText(spannableString);
        word_in = new ArrayList<>(Arrays.asList(
                "외과", "내과", "산부인과", "이비인후과", "정형외과", "정신과", "화상", "발열", "기침", "가려움", "알레르기", "고지혈증", "산부인과", "무좀", "고혈압", "당뇨"));
        //HASH TAG 버튼 Custom과 지정.
        for (int i = 0; i < 15; i++) {
            int counting = new Random().nextInt(word_in.size());
            final Button button = new Button(this);
            button.setTextColor(Color.parseColor("#292929"));
            button.setText(word_in.get(counting));
            button.setBackgroundResource(R.drawable.alarm_btn);
            button.setTextSize(18);
            button.setIncludeFontPadding(false);

            //boxsize
            FlexboxLayout.LayoutParams parms = new FlexboxLayout.LayoutParams(word_in.get(counting).length() * 82, 130);
            parms.setMargins(25, 25, 0, 0);
            button.setLayoutParams(parms);
            button.setPadding(15, 15, 15, 15);

            final int checking = i;

            final Button word_button = new Button(getApplicationContext());
            word_button.setTextColor(Color.BLACK);
            button.setBackgroundResource(R.drawable.roundborder_grey);
            word_button.setText("#" + button.getText().toString());
            word_button.setTextSize(15);


            //boxsize
            FlexboxLayout.LayoutParams parms2 = new FlexboxLayout.LayoutParams(button.getText().toString().length() * 82, 130);
            parms2.setMargins(25, 25, 0, 0);
            button.setLayoutParams(parms2);
            word_button.setLayoutParams(parms2);

            parms.setMargins(25, 25, 0, 0);
            button.setLayoutParams(parms);
            button.setPadding(15, 15, 15, 15);

            word_button.setBackgroundColor(Color.parseColor("#ffffff"));
            word_button.setIncludeFontPadding(false);

            word_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    word_flexboxlayout.removeView(v);
                    button.setTextColor(Color.BLACK);
                    button.setBackgroundResource(R.drawable.roundborder_grey);
                    check[checking]++;

                    hashtags.remove(word_button.getText().toString());
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check[checking] % 2 == 0) {
                        button.setTextColor(Color.parseColor("#7F74F2"));
                        button.setBackgroundResource(R.drawable.alarm_btn);
                        check[checking]++;
                        word_flexboxlayout.addView(word_button);
                        hashtags.add(button.getText().toString());
                    } else {
                        button.setTextColor(Color.BLACK);
                        button.setBackgroundResource(R.drawable.roundborder_grey);
                        check[checking]++;
                        word_flexboxlayout.removeView(word_button);
                        hashtags.remove(button.getText().toString());
                    }
                }
            });
            word_in.remove(counting);
            flexboxLayout.addView(button);
        }

        //Edittext 에서 hashtag 단어 입력추가 기능
        add_text_hash.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final Button word_button = new Button(getApplicationContext());
                word_button.setTextColor(Color.parseColor("#292929"));
                word_button.setBackgroundColor(Color.WHITE);
                word_button.setText("#" + add_text_hash.getText().toString());
                word_button.setTextSize(15);
                word_button.setIncludeFontPadding(false);
                //boxsize
                FlexboxLayout.LayoutParams parms3 = new FlexboxLayout.LayoutParams(add_text_hash.getText().toString().length() * 65, 130);
                parms3.setMargins(25, 25, 0, 0);
                word_button.setLayoutParams(parms3);
                word_button.setLayoutParams(parms3);
                word_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        word_flexboxlayout.removeView(v);
                        hashtags.remove(word_button.getText().toString());
                    }
                });
                word_flexboxlayout.addView(word_button);

                hashtags.add(add_text_hash.getText().toString());

                add_text_hash.setText("");

                return false;
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //저장하기 버튼
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrescription();
                Intent outIntent = getIntent();

                outIntent.putExtra("titlePre", prescription.getName());
                outIntent.putExtra("isDrinking",isDrinking);
                outIntent.putExtra("isSmoking",isSmoking);
                setResult(RESULT_OK, outIntent);
                finish();
            }
        });
    }

    private void savePrescription() {
        prescription.setHashTag(hashtags);
        prescription.setYear(new Year());

        User.getInstance().getPrescriptions().add(prescription);

        User.getInstance().syncWithDatabase();
    }

    void Custom_text(String word) {
        int start = textView.getText().toString().indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
