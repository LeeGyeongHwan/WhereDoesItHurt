package com.k1l3.wheredoesithurt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class Fragment_search extends Fragment {
    View viewGroup;
    String search_word;
    ArrayList<Search_item> data;
    ListView listView;
    Adapter adapter;
    ImageView search_medicine_image;
    Bitmap imgBitmap = null, no_medicine_image = null;
    FragmentManager manager;
    FragmentTransaction transaction;
    EditText medicine_search;
    Toolbar toolBar;
    TextView backgroundchange;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_search, container, false);
        ((MainActivity) getActivity()).toolbar_search();
        listView = (ListView) viewGroup.findViewById(R.id.search_list_view);
        medicine_search = (EditText) viewGroup.findViewById(R.id.medicin_search_2);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();

        no_medicine_image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.search_img);
        search_medicine_image = (ImageView) viewGroup.findViewById(R.id.search_medicine_image);
        adapter = new Adapter();
        medicine_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        medicine_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!medicine_search.getText().toString().equals("")) {
                    getArguments().putString("search_word", medicine_search.getText().toString());
                    setListView(medicine_search.getText().toString());
                } else {
                    Toast.makeText(getContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        if (getArguments() != null) {
            search_word = getArguments().getString("search_word");
            setListView(search_word);
        }
        return viewGroup;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private ArrayList<Search_item> getXmlData(String str) {

        StringBuffer buffer = new StringBuffer();
        String item_name = null, entp_name = null, etc_otc_code = null, ee_doc_data = "", ud_doc_data = "";
        ArrayList<Search_item> item = new ArrayList<>();
        int stopping = 0;
        try {
            URL url = new URL("http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductItem?ServiceKey=" + getString(R.string.api_data_key) + "&item_name=" + str);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("ITEM_NAME")) {
                            xpp.next();
                            item_name = xpp.getText();
                        } else if (tag.equals("ENTP_NAME")) {
                            xpp.next();
                            entp_name = xpp.getText();
                        } else if (tag.equals("ETC_OTC_CODE")) {
                            xpp.next();
                            etc_otc_code = xpp.getText();
                        } else if (tag.equals("EE_DOC_DATA")) {
                            stopping = 1;
                        } else if (tag.equals("PARAGRAPH") && stopping == 1) {
                            xpp.next();
                            ee_doc_data = ee_doc_data + xpp.getText() + "\n";
                        } else if (tag.equals("UD_DOC_DATA")) {
                            stopping = 2;
                        } else if (tag.equals("PARAGRAPH") && stopping == 2) {
                            xpp.next();
                            String data = xpp.getText();
                            if (!data.substring(0, 1).equals("<")) {
                                ud_doc_data = ud_doc_data + xpp.getText() + "\n";
                            }
                        } else if (tag.equals("NB_DOC_DATA")) {
                            stopping = 0;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) {
                            Search_item budget = new Search_item(item_name, entp_name, etc_otc_code, ee_doc_data, ud_doc_data);
                            item.add(budget);

                            item_name = null;
                            entp_name = null;
                            etc_otc_code = null;
                            ee_doc_data = "";
                            ud_doc_data = "";
                        }
                        break;
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
        }

        return item;

    }

    private void replaceFragment(@NonNull Fragment fragment) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
        Log.e(TAG, "값 : " + String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }

    private Bitmap GetImageFromURL(String item_name) {
        Bitmap imgBitmap = null;
        String item_image_URL = null;
        try {
            URL url = new URL("http://apis.data.go.kr/1470000/MdcinGrnIdntfcInfoService/getMdcinGrnIdntfcInfoList?ServiceKey=" + getString(R.string.api_data_key) + "&item_name=" + item_name);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("ITEM_IMAGE")) {
                            xpp.next();
                            item_image_URL = xpp.getText();
                            Log.e(TAG, item_image_URL);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:

                        break;
                }

                eventType = xpp.next();
            }
            try {
                URL iamge_url = new URL(item_image_URL);
                URLConnection conn = iamge_url.openConnection();
                conn.connect();
                int nSize = conn.getContentLength();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
                imgBitmap = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "오류 1 :" + e.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "오류 2 : " + e.toString());
        }
        return imgBitmap;
    }

    public void setListView(final String search_word) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                data = getXmlData(search_word);
                for (int i = 0; i < data.size(); i++) {
                    imgBitmap = GetImageFromURL(data.get(i).getItem_name());
                    if (imgBitmap != null) {
                        data.get(i).setItem_image(imgBitmap);
                    } else {
                        data.get(i).setItem_image(no_medicine_image);
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < data.size(); i++) {
                                adapter.addItem(data);
                                listView.setAdapter(adapter);
                            }
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (data.get(position).getItem_image() == no_medicine_image) {
                                data.get(position).setItem_image(null);
                            }
                            Fragment fragment_detail_search = new Fragment_detail_search();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", data.get(position));
                            fragment_detail_search.setArguments(bundle);
                            replaceFragment(fragment_detail_search);
                        }
                    });
                }

            }
        }).start();
    }

    class Adapter extends BaseAdapter {
        ArrayList<Search_item> items = new ArrayList<Search_item>();

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

        public void addItem(Search_item item) {
            items.add(item);
        }

        public void addItem(ArrayList<Search_item> item) {
            items = item;
        }

        public void deleteItem(Search_item item) {
            items.remove(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Search_itemView view = new Search_itemView(viewGroup.getContext());
            Search_item item = items.get(position);
            view.setMedicine_name(item.getMedicine_name());
            view.setSearch_medicine_image(item.getItem_image());
            return view;
        }
    }
}
