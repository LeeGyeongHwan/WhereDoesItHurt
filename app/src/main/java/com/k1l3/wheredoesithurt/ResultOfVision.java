package com.k1l3.wheredoesithurt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.User;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ResultOfVision extends AppCompatActivity {
    private final ArrayList<EditText> EditList1 = new ArrayList<>();
    private final ArrayList<EditText> EditList2 = new ArrayList<>();
    private final ArrayList<EditText> EditList3 = new ArrayList<>();
    private final ArrayList<EditText> EditList4 = new ArrayList<>();
    private Button nextPage, addMedBtn;
    private ImageButton cancelBtn;
    private EditText title_pre;
    private Prescription prescription;
    private ArrayList<Medicine> medicines;
    private int index = 0;
    private String name;
    private String uri;
    private ArrayList<Pair> medicine_kind;
    private Boolean isPregnant = false;
    private Boolean isDrinking = false;
    private Boolean isSmoking = false;
    private String dangerForPregnant = "약";
    private static final User user = User.getInstance();
    private boolean nextPaging = true;
    private Handler handler;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_analysis);

        cancelBtn = findViewById(R.id.cancelvision);
        nextPage = findViewById(R.id.nextpage);
        addMedBtn = findViewById(R.id.addMedBtn);
        title_pre = findViewById(R.id.title_prescription);
        dialog = new ProgressDialog(ResultOfVision.this);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                dialog.dismiss();
            }
        };
        prescription = new Prescription();
        Intent intent = getIntent();
        String getStr = intent.getStringExtra("result");
        int getCount = intent.getIntExtra("numbermedicine", 1);
        medicines = new ArrayList<>();
        uri = intent.getStringExtra("bitmap_name");
        cancelBtn = findViewById(R.id.cancelvision);
        nextPage = findViewById(R.id.nextpage);

        for (int i = 0; i < getCount; i++) {
            LinearLayout linearLayout = findViewById(R.id.startinflatelinear);

            LayoutInflater layoutInflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layoutInflater.inflate(R.layout.add_linear, linearLayout, true);
            EditText edit1 = findViewById(R.id.analysis_edit1_1);
            EditText edit2 = findViewById(R.id.analysis_edit1_2);
            EditText edit3 = findViewById(R.id.analysis_edit1_3);
            EditText edit4 = findViewById(R.id.analysis_edit1_4);
            edit1.setId(100 + i);
            edit2.setId(200 + i);
            edit3.setId(300 + i);
            edit4.setId(400 + i);
            EditList1.add(edit1);
            EditList2.add(edit2);
            EditList3.add(edit3);
            EditList4.add(edit4);
            index = i + 1;
        }

        if (getStr != null) {
            TextView text = findViewById(R.id.visiontext);
            text.setVisibility(View.VISIBLE);
            String[] words = getStr.split("\\s");

            for (int i = 0; i < words.length; i++) {
                int num = i / 4;
                switch (i % 4) {
                    case 0:
                        EditList1.get(num).setText(words[i]);
                        break;
                    case 1:
                        EditList2.get(num).setText(words[i]);
                        break;
                    case 2:
                        EditList3.get(num).setText(words[i]);
                        break;
                    case 3:
                        EditList4.get(num).setText(words[i]);
                        break;
                    default:
                        break;
                }
            }
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(ResultOfVision.this);
                dialog.setCancelable(false);
                dialog.setMessage("정보를 저장중입니다.");
                dialog.show();
                nextPaging = true;
                for (int i = 0; i < EditList1.size(); i++) {
                    if (EditList1.get(i).getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "약이름을 입력해주세요", Toast.LENGTH_LONG).show();
                        nextPaging = false;
                    }
                }
                if (nextPaging) {
                    Intent intent = new Intent(ResultOfVision.this, SetAlarmPage.class);
                    intent.putExtra("index", index);
                    intent.putExtra("alarmcount", EditList3.get(0).getText().toString());
                    intent.putExtra("alarmday", EditList4.get(0).getText().toString());

                    name = title_pre.getText().toString();
                    intent.putExtra("title", name);

                    addMedicines(intent);
                }
            }
        });

        addMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = findViewById(R.id.startinflatelinear);

                LayoutInflater layoutInflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                layoutInflater.inflate(R.layout.add_linear, linearLayout, true);
                EditText edit1 = findViewById(R.id.analysis_edit1_1);
                EditText edit2 = findViewById(R.id.analysis_edit1_2);
                EditText edit3 = findViewById(R.id.analysis_edit1_3);
                EditText edit4 = findViewById(R.id.analysis_edit1_4);
                edit1.setId(100 + index);
                edit2.setId(200 + index);
                edit3.setId(300 + index);
                edit4.setId(400 + index);
                index++;
                EditList1.add(edit1);
                EditList2.add(edit2);
                EditList3.add(edit3);
                EditList4.add(edit4);
            }
        });
    }

    private void addMedicines(final Intent intent) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                int isDefaultPregnant = 0;
                if (user.getUserInfo().getLifeStyles() != null) {
                    isDefaultPregnant = user.getUserInfo().getLifeStyles().getLifeStyles().get(2);
                }
                if (isPregnant && (isDefaultPregnant == 1)) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), dangerForPregnant + " 은/는 임산부에게 위험한 약입니다.\n의사나 약사와 다시 상의해보세요", Toast.LENGTH_LONG).show();
                    Looper.loop();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            dialog.setCancelable(false);
                            dialog.setMessage("정보를 저장중입니다.");
                            dialog.show();
                        }
                    });
                    medicine_kind = getXmlData(EditList1);
                    for (int i = 0; i < EditList1.size(); ++i) {
                        medicines.add(new Medicine(EditList1.get(i).getText().toString(),
                                Integer.parseInt(EditList2.get(i).getText().toString()),
                                Integer.parseInt(EditList3.get(i).getText().toString()),
                                Integer.parseInt(EditList4.get(i).getText().toString()),
                                medicine_kind.get(i).getKind(), medicine_kind.get(i).getCaution()));
                    }
                    prescription.setMedicines(medicines);
                    prescription.setName(name);
                    prescription.setTotalClick(0);
                    prescription.setPrescriptionImage(uri);
                    intent.putExtra("prescription", prescription);
                    intent.putExtra("isDrinking", isDrinking);
                    intent.putExtra("isSmoking", isSmoking);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private ArrayList<Pair> getXmlData(ArrayList<EditText> EditList) {
        StringBuffer buffer = new StringBuffer();
        String medicine_kind = null;
        String ee_doc_data = null;
        String kind = null;
        int itemCount;
        int caution = 0;
        ArrayList<Pair> item = new ArrayList<>();
        int stopping = 0;
        boolean stop = true;
        for (int i = 0; i < EditList.size(); ++i) {
            try {
                URL url = new URL("http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductItem?ServiceKey=" + getString(R.string.api_data_key) + "&item_name=" + EditList.get(i).getText().toString());
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));
                itemCount = -1;
                String tag;
                stop = true;
                xpp.next();
                int eventType = xpp.getEventType();

                while (stop) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();

                            if (tag.equals("item")) ;
                            else if (tag.equals("PACK_UNIT")) {
                                xpp.next();
                                medicine_kind = xpp.getText();
                                if (medicine_kind.contains("mL") || medicine_kind.contains("리터")) {
                                    kind = "물약";
                                } else {
                                    kind = "알약";
                                }

                            } else if (tag.equals("EE_DOC_DATA")) {
                                stopping = 1;
                            } else if (tag.equals("PARAGRAPH") && stopping == 1) {
                                xpp.next();
                                ee_doc_data = ee_doc_data + xpp.getText() + "\n";
                            } else if (tag.equals("ARTICLE") && stopping == 1) {
                                ee_doc_data = ee_doc_data + xpp.getAttributeValue(0) + "\n";
                            } else if (tag.equals("UD_DOC_DATA")) {
                                stopping = 0;
                            } else if (tag.equals("totalCount")) {
                                xpp.next();
                                itemCount = Integer.valueOf(xpp.getText());
                            } else if (tag.equals("NB_DOC_DATA")) {
                                stopping = 3;
                            } else if (tag.equals("PN_DOC_DATA")) {
                                stopping = 0;
                            } else if (stopping == 3) {
                                xpp.next();
                                String warning = xpp.getText();
                                //임부, 임산부, 임신   흡연,담배   알코올 음주
                                if (warning.contains("임부") || warning.contains("임산부") || warning.contains("임신")) {
                                    isPregnant = true;
                                    dangerForPregnant = EditList.get(i).getText().toString();

                                }
                                if(warning.contains("흡연") || warning.contains("담배")){
                                    isSmoking = true;
                                }
                                if(warning.contains("알코올")||warning.contains("음주")){
                                    isDrinking = true;
                                }
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag = xpp.getName();
                            if (tag.equals("item")) {
                                if (ee_doc_data.contains("빈혈")) {
                                    caution = caution + 1024;
                                }
                                if (ee_doc_data.contains("항응고")) {
                                    caution = caution + 512;
                                }
                                if (ee_doc_data.contains("고혈압")) {
                                    caution = caution + 256;
                                }
                                if (ee_doc_data.contains("결핵")) {
                                    caution = caution + 128;
                                }
                                if (ee_doc_data.contains("변비")) {
                                    caution = caution + 64;
                                }
                                if (ee_doc_data.contains("기관지확장")) {
                                    caution = caution + 32;
                                }
                                if (ee_doc_data.contains("골다공증")) {
                                    caution = caution + 16;
                                }
                                if (ee_doc_data.contains("통풍")) {
                                    caution = caution + 8;
                                }
                                if (ee_doc_data.contains("알레르기")) {
                                    caution = caution + 4;
                                }
                                if (ee_doc_data.contains("철분")) {
                                    caution = caution + 2;
                                }
                                if (ee_doc_data.contains("감기")) {
                                    caution = caution + 1;
                                }
                                Pair pair = new Pair(kind, caution);
                                item.add(pair);
                                kind = "";
                                ee_doc_data = "";
                                caution = 0;
                                stop = false;
                            }
                            if (tag.equals("items")) {
                                stop = false;
                            }
                            break;
                    }
                    if (itemCount == 0) {
                        item.add(new Pair("알약", caution));
                        break;
                    }
                    eventType = xpp.next();
                }

            } catch (Exception e) {
            }
        }
        return item;

    }

}
