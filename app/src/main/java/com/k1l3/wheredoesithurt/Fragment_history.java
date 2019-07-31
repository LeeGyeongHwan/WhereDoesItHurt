package com.k1l3.wheredoesithurt;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_history extends Fragment {
    ArrayList<String> a = new ArrayList<>();
    ArrayList<String> b = new ArrayList<>();
    View viewGroup;
    ListView listView;
    Adapter adapter;
    Button button;
    Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_history,container, false);
        listView = (ListView)viewGroup.findViewById(R.id.history_list_view);
        button = (Button)viewGroup.findViewById(R.id.history_button);
        adapter = new Adapter();
        a.add("가려움");
        a.add("알레르기");
        a.add("기침");
        a.add("내과");
        a.add("외과");
        a.add("치과");
        a.add("이비인후과");
        b.add("파티엔 정");
        b.add("다이엔 캡슐");
        b.add("카푸리엔스 정");
        adapter.addItem(new History_item("2019.07.29",a,b));
        adapter.addItem(new History_item("2019.07.30",a,b));
        listView.setAdapter(adapter);
        return viewGroup;
    }

    class Adapter extends BaseAdapter {
        ArrayList<History_item> items = new ArrayList<History_item>();
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
        public void addItem(History_item item){
            items.add(item);
        }
        public void addItem(ArrayList<History_item> item){items=item;}
        public void deleteItem(History_item item){
            items.remove(item);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final History_itemView view = new History_itemView(viewGroup.getContext());
            final History_item item = items.get(position);
            StringBuffer medicine = new StringBuffer(item.getMedicine().get(0));

            //Hash태그에 들어가는것 추가 + Custom
            view.setPresc_date(item.getHistory_day());
            for(int i=0;i<item.getHash_tag().size();i++){
                TextView textView = new TextView(getContext());
                textView.setText(item.getHash_tag().get(i));
                textView.setTextColor(Color.BLACK);
                textView.setBackground(getResources().getDrawable(R.drawable.searchbox));
                textView.setTextSize(15);
                textView.setIncludeFontPadding(false);
                textView.setPadding(20, 20, 20, 20);
                view.getFlexboxlayout().addView(textView);
            }
            if(item.getMedicine().size()>=1) {
                for (int i = 1; i < item.getMedicine().size(); i++) {
                    medicine.append(" / "+item.getMedicine().get(i));
                }
            }
            view.setPresc_medicine(medicine.toString());

            //History에서 버튼누르면 밑에께나오기
            view.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getCount()==0) {
                        view.getLinearLayout().setVisibility(View.VISIBLE);
                        item.setCount(1);
                    }
                    else{
                        view.getLinearLayout().setVisibility(View.GONE);
                        item.setCount(0);
                    }
                }
            });

            //History 이미지 클릭시 사진크게하기
            view.getHistory_medicine_image().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageClick(view.getHistory_medicine_image());
                }
            });
            view.getHistory_medicine_image2().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageClick(view.getHistory_medicine_image2());
                }
            });

            view.getHistory_memo().setHorizontallyScrolling(false);

            return view;
        }

    }
    void ImageClick(ImageView imageView){
        View rView = getLayoutInflater().inflate(R.layout.full_screen,null);
        ImageView a = rView.findViewById(R.id.full_image);
        a.setImageBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap());
        dialog = new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(rView);
        RelativeLayout relativeLayout = rView.findViewById(R.id.image_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
