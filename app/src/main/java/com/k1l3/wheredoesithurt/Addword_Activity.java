package com.k1l3.wheredoesithurt;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Addword_Activity extends AppCompatActivity {
    ArrayList<String> word_in;
    TextView textView,hash_tag_text;
    EditText add_text_hash;
    Button saveButton;
    SpannableString spannableString;
    FlexboxLayout flexboxLayout;
    StringBuffer hash_tag;
    int[] check = new int[15];
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        textView = (TextView)findViewById(R.id.textView);
        hash_tag_text = (TextView)findViewById(R.id.hash_tag_text);
        flexboxLayout = (FlexboxLayout)findViewById(R.id.add_flexboxlayout);
        saveButton = (Button)findViewById(R.id.save_button);
        hash_tag = new StringBuffer();
        add_text_hash = (EditText)findViewById(R.id.add_text_hash);
        spannableString = new SpannableString(textView.getText().toString());
        Custom_text("처방전");
        Custom_text("단어");
        textView.setText(spannableString);
        word_in = new ArrayList<>(Arrays.asList(
                "외과","내과","산부인과","이비인후과","정형외과","정신과","화상","발열","기침","가려움","알레르기","고지혈증","산부인과","무좀","고혈압","당뇨"));
        //HASH TAG 버튼 Custom과 지정.
        for(int i=0;i<15;i++){
            int counting = new Random().nextInt(word_in.size());
            final Button button = new Button(this);
            button.setTextColor(Color.BLACK);
            button.setText(word_in.get(counting));
            button.setBackgroundColor(Color.WHITE);
            button.setTextSize(15);
            button.setIncludeFontPadding(false);
            button.setPadding(20, 20, 20, 20);
            final int checking = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check[checking]%2==0){
                        button.setTextColor(Color.WHITE);
                        button.setBackgroundColor(Color.BLACK);
                        check[checking]++;
                        hash_tag.append(" #"+button.getText().toString());
                        hash_tag_text.setText(hash_tag.toString());
                    }
                    else{
                        button.setTextColor(Color.BLACK);
                        button.setBackgroundColor(Color.WHITE);
                        check[checking]++;
                        hash_tag.delete(hash_tag.indexOf(button.getText().toString())-2,
                                hash_tag.indexOf(button.getText().toString())+button.getText().toString().length());
                        hash_tag_text.setText(hash_tag.toString());
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
                hash_tag.append(" #"+add_text_hash.getText().toString());
                hash_tag_text.setText(hash_tag.toString());
                add_text_hash.setText("");
                return false;
            }
        });
        //저장하기 버튼
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void Custom_text(String word){
        int start = textView.getText().toString().indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
