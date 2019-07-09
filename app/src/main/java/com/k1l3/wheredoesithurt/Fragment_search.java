package com.k1l3.wheredoesithurt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Fragment_search extends Fragment {
    String search_word;
    String data;
    TextView text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View viewGroup = inflater.inflate(R.layout.fragment_search, container, false);
        text = (TextView)viewGroup.findViewById(R.id.text);
        if(getArguments()!=null){
            search_word = getArguments().getString("search_word");

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    data= getXmlData();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data);
                            }
                        });
                    }
                }
            }).start();
        }
        return viewGroup;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private String getXmlData(){

        StringBuffer buffer=new StringBuffer();
        String str= search_word;
        String item_name = null , entp_name = null, etc_otc_code = null;
        ArrayList<String> ee_doc_data = new ArrayList<>();
        ArrayList<String> ud_doc_data = new ArrayList<>();
        int stopping = 0;
        try {
            Log.e(TAG, "getXmlData: " + getString(R.string.api_data_key));
            URL url = new URL("http://apis.data.go.kr/1471057/MdcinPrductPrmisnInfoService/getMdcinPrductItem?ServiceKey="+getString(R.string.api_data_key)+"&item_name="+str);
            InputStream is= url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) ;
                        else if(tag.equals("ITEM_NAME")){
                            xpp.next();
                            item_name=xpp.getText();
                        }
                        else if (tag.equals("ENTP_NAME")) {
                            xpp.next();
                            entp_name = xpp.getText();
                        } else if (tag.equals("ETC_OTC_CODE")) {
                            xpp.next();
                            etc_otc_code = xpp.getText();
                        }else if (tag.equals("EE_DOC_DATA")) {
                            stopping = 1;
                        }else if (tag.equals("PARAGRAPH")&&stopping==1) {
                            xpp.next();
                            ee_doc_data.add(xpp.getText());
                        }else if (tag.equals("UD_DOC_DATA")) {
                            stopping = 2;
                        } else if (tag.equals("PARAGRAPH")&&stopping==2) {
                            xpp.next();
                            String data = xpp.getText();
                            if(!data.substring(0,1).equals("<")) {
                                ud_doc_data.add(xpp.getText());
                            }
                        }else if(tag.equals("NB_DOC_DATA")){
                            stopping = 0;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) {
                            if(item_name!=null) {
                                buffer.append(" 이름 : " + item_name + "\n  회사이름: " + entp_name +"\n  종류: " + etc_otc_code
                                        +"\n  효능효과 :");
                            }
                            for(int i=0;i<ee_doc_data.size();i++){
                                buffer.append("\n "+ ee_doc_data.get(i));
                            }
                            buffer.append("\n  용법용량 : ");
                            for(int i=0;i<ud_doc_data.size();i++){
                                buffer.append("\n "+ ud_doc_data.get(i));
                            }
                            //GetImageFromURL(item_name);  //이미지다운
                            buffer.append("\n --------------------\n");
                            item_name = null;
                            entp_name = null;
                            etc_otc_code = null;
                            ee_doc_data.clear();
                            ud_doc_data.clear();
                        }
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
        }

        return buffer.toString();

    }

  /*  private Bitmap GetImageFromURL(String item_name) {
        Bitmap imgBitmap = null;
        String item_image_URL = null;
        try
        {
            URL url = new URL("http://apis.data.go.kr/1470000/MdcinGrnIdntfcInfoService/getMdcinGrnIdntfcInfoList?ServiceKey="+key+"&item_name="+item_name);
            InputStream is= url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) ;
                        else if(tag.equals("ITEM_IMAGE")){
                            xpp.next();
                            item_image_URL=xpp.getText();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:

                        break;
                }

                eventType= xpp.next();
            }

            URL iamge_url = new URL(item_image_URL);
            URLConnection conn = iamge_url.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return imgBitmap;
    }*/

}
