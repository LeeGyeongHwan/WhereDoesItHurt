package com.k1l3.wheredoesithurt;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;

import java.io.IOException;
import java.util.ArrayList;

public class Fragment_history extends Fragment {
    private View viewGroup;
    private ListView listView;
    private Adapter adapter;
    private Dialog dialog;
    private EditText history_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_history, container, false);
        ((MainActivity) getActivity()).toolbar_history();
        listView = viewGroup.findViewById(R.id.history_list_view);
        adapter = new Adapter();

        getPrescriptions();

        history_search = viewGroup.findViewById(R.id.history_search);
        history_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                adapter = new Adapter();
                getSearchPrescriptions(history_search.getText().toString());
                return false;
            }
        });
        return viewGroup;
    }

    private void getPrescriptions() {
        User user = User.getInstance();

        for (Prescription prescription : user.getPrescriptions()) {
            adapter.addItem(prescription);
        }

        listView.setAdapter(adapter);
    }

    private void getSearchPrescriptions(String searchWord) {
        User user = User.getInstance();

        for (Prescription prescription : user.getPrescriptions()) {
            if (prescription.getName().contains(searchWord)) {
                adapter.addItem(prescription);
            } else {
                for (int i = 0; i < prescription.getHashTag().size(); i++) {
                    if (prescription.getHashTag().get(i).contains(searchWord)) {
                        adapter.addItem(prescription);
                    }
                }
            }
        }

        listView.setAdapter(adapter);

        listView.setAdapter(adapter);
    }

    void ImageClick(ImageView imageView) {
        View rView = getLayoutInflater().inflate(R.layout.full_screen, null);
        PhotoView a = rView.findViewById(R.id.full_image);
        a.setImageBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
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

    class Adapter extends BaseAdapter {
        ArrayList<Prescription> items = new ArrayList<>();

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

        private void addItem(Prescription item) {
            items.add(item);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final History_itemView view = new History_itemView(viewGroup.getContext());
            final Prescription item = items.get(position);
            StringBuilder medicine = new StringBuilder(item.getMedicines().get(0).getName());

            //Hash태그에 들어가는것 추가 + Custom

            view.setPresc_date(item.getBegin());
            view.setMedicine_name(item.getName());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < item.getHashTag().size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(item.getHashTag().get(i));
                textView.setTextColor(Color.BLACK);
                textView.setBackground(getResources().getDrawable(R.drawable.history_tag));
                textView.setTextSize(13);
                textView.setIncludeFontPadding(false);
                textView.setPadding(30, 20, 30, 20);
                params.setMargins(0, 10, 10, 0);
                textView.setLayoutParams(params);
                view.getFlexboxlayout().addView(textView);
            }
            if (item.getMedicines().size() >= 1) {
                for (int i = 1; i < item.getMedicines().size(); i++) {
                    medicine.append(" / " + item.getMedicines().get(i).getName());
                }
            }
            view.setPresc_medicine(medicine.toString());

            //History에서 버튼누르면 밑에께나오기
            view.getFirst_layout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isVisible()) {
                        view.getLinearLayout().setVisibility(View.GONE);
                        view.getSeperator().setVisibility(View.GONE);
                        item.hide();
                    } else {
                        view.getLinearLayout().setVisibility(View.VISIBLE);
                        view.getSeperator().setVisibility(View.VISIBLE);
                        item.show();
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

            view.setHistory_memo(item.getMemo());
            view.getHistory_memo().setHorizontallyScrolling(false);
            view.getHistory_memo().setImeOptions(EditorInfo.IME_ACTION_DONE);
            view.getHistory_memo().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    User user = User.getInstance();

                    item.setMemo(view.getHistory_memo().getText().toString());
                    user.syncWithDatabase();

                    return false;
                }
            });

            //히스토리 사진 처방전있을시 추가
            if(User.getInstance().getPrescriptions().get(position).getPrescriptionImage()!=null){
                Bitmap bitmap = null;
                String uriString = User.getInstance().getPrescriptions().get(position).getPrescriptionImage();
                Uri uri = Uri.parse(uriString);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    view.setHistory_medicine_image(bitmap);
                } catch (IOException e) {
                }

            }
            return view;
        }

    }
}
