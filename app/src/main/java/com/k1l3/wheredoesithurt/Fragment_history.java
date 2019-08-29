package com.k1l3.wheredoesithurt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.RequestManager;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Fragment_history extends Fragment {
    public static final int CAMERA_IMAGE_REQUEST2 = 4;
    private View viewGroup;
    private ListView listView;
    private Adapter adapter;
    private Dialog dialog;
    private EditText history_search;
    private History_itemView historyItemView;
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
        private RequestManager requestManager;
        public int getCount() {
            return items.size();
        }

        @Override
        public Prescription getItem(int position) {
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
//
//            //History 이미지 클릭시 사진크게하기
//            view.getHistory_medicine_image2().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ImageClick(view.getHistory_medicine_image2());
//                }
//            });

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
            historyItemView = view;
            //히스토리 사진 처방전있을시 추가
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String filename = ((MainActivity)getActivity()).getId();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://wheredoesithurt-a9ce0.appspot.com").child(filename+"/").child("presc/")
                    .child(String.valueOf(position));
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.history_loading);
                    view.setHistory_medicine_image(bitmap2);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Bitmap bitmap = null;
                            try {
                                //Glide.with(getContext()).load(R.raw.history_loading).into(view.getHistory_medicine_image());
                                URL iamge_url = new URL(uri.toString());
                                URLConnection conn = iamge_url.openConnection();
                                conn.connect();
                                int nSize = conn.getContentLength();
                                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
                                bitmap = BitmapFactory.decodeStream(bis);
                                bis.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            final Bitmap finalBitmap = bitmap;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    view.setHistory_medicine_image(finalBitmap);
                                    view.getHistory_medicine_image().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageClick(view.getHistory_medicine_image());
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.getHistory_medicine_image().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builder
                                    .setMessage("처방전을 추가하세요")
                                    .setPositiveButton("앨범에서 가져오기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((MainActivity)getActivity()).startGalleryChooser(0,position);
                                        }
                                    })
                                    .setNegativeButton("사진찍기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((MainActivity)getActivity()).startCamera(0,position);
                                        }
                                    });
                            builder.create().show();
                        }
                    });
                }
            });
            //약사진
            StorageReference storageRef2 = storage.getReferenceFromUrl("gs://wheredoesithurt-a9ce0.appspot.com").child(filename+"/").child("medicine/")
                    .child(String.valueOf(position));
            storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.history_loading);
                    view.setHistory_medicine_image2(bitmap2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Bitmap bitmap = null;
                            try {
                                URL iamge_url = new URL(uri.toString());
                                URLConnection conn = iamge_url.openConnection();
                                conn.connect();
                                int nSize = conn.getContentLength();
                                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
                                bitmap = BitmapFactory.decodeStream(bis);
                                bis.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            final Bitmap finalBitmap = bitmap;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    view.setHistory_medicine_image2(finalBitmap);
                                    view.getHistory_medicine_image2().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageClick(view.getHistory_medicine_image2());
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.getHistory_medicine_image2().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builder
                                    .setMessage("처방전을 추가하세요")
                                    .setPositiveButton("앨범에서 가져오기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((MainActivity)getActivity()).startGalleryChooser(1,position);
                                        }
                                    })
                                    .setNegativeButton("사진찍기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((MainActivity)getActivity()).startCamera(1,position);
                                        }
                                    });
                            builder.create().show();
                        }
                    });
                }
            });


            return view;
        }
    }

}
