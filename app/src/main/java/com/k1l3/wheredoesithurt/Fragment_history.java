package com.k1l3.wheredoesithurt;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.Prescription;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class Fragment_history extends Fragment {
    ArrayList<String> a = new ArrayList<>();
    ArrayList<String> b = new ArrayList<>();
    View viewGroup;
    ListView listView;
    Adapter adapter;
    Button button;
    Dialog dialog;
    private Long id;
    private ArrayList<Prescription> prescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            id = getArguments().getLong("id");
        }

        prescription = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + id);
        ValueEventListener prescriptionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getPrescriptions(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        };

        viewGroup = inflater.inflate(R.layout.fragment_history, container, false);
        ((MainActivity) getActivity()).toolbar_history();
        listView = viewGroup.findViewById(R.id.history_list_view);
        button = viewGroup.findViewById(R.id.history_button);
        adapter = new Adapter();

        databaseReference.child("prescriptions").addListenerForSingleValueEvent(prescriptionListener);

        return viewGroup;
    }

    private void getPrescriptions(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                prescription.add(dataSnapshotChild.getValue(Prescription.class));
            }
            for (Prescription pres : prescription) {
                adapter.addItem(pres);
            }
        }
        listView.setAdapter(adapter);
    }


    class Adapter extends BaseAdapter {
        ArrayList<Prescription> items = new ArrayList<Prescription>();

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

        public void addItem(Prescription item) {
            items.add(item);
        }

        public void addItem(ArrayList<Prescription> item) {
            items = item;
        }

        public void deleteItem(Prescription item) {
            items.remove(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final History_itemView view = new History_itemView(viewGroup.getContext());
            final Prescription item = items.get(position);
            StringBuffer medicine = new StringBuffer(item.getMedicines().get(0).getName());

            //Hash태그에 들어가는것 추가 + Custom
            view.setPresc_date(item.getBegin());
            for (int i = 0; i < item.getHashTag().size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(item.getHashTag().get(i));
                textView.setTextColor(Color.BLACK);
                textView.setBackground(getResources().getDrawable(R.drawable.searchbox));
                textView.setTextSize(15);
                textView.setIncludeFontPadding(false);
                textView.setPadding(20, 20, 20, 20);
                view.getFlexboxlayout().addView(textView);
            }
            if (item.getMedicines().size() >= 1) {
                for (int i = 1; i < item.getMedicines().size(); i++) {
                    medicine.append(" / " + item.getMedicines().get(i).getName());
                }
            }
            view.setPresc_medicine(medicine.toString());

            //History에서 버튼누르면 밑에께나오기
            view.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isVisible()) {
                        view.getLinearLayout().setVisibility(View.GONE);
                        item.hide();
                    } else {
                        view.getLinearLayout().setVisibility(View.VISIBLE);
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

            view.getHistory_memo().setHorizontallyScrolling(false);

            return view;
        }

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
}
