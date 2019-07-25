package com.k1l3.wheredoesithurt;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.util.ArrayList;

public class Search_item extends ArrayList<Parcelable> {
    String item_name,entp_name,etc_otc_code,ee_doc_data,ud_doc_data;
    Bitmap item_image;
    public Search_item(){ }
    public Search_item(String medicine_name) {
        this.item_name = medicine_name;
    }

    public Search_item(String item_name, String entp_name, String etc_otc_code, String ee_doc_data, String ud_doc_data) {
        this.item_name = item_name;
        this.entp_name = entp_name;
        this.etc_otc_code = etc_otc_code;
        this.ee_doc_data = ee_doc_data;
        this.ud_doc_data = ud_doc_data;
    }

    public Search_item(Bitmap item_image){
        this.item_image = item_image;
    }
    public Search_item(String item_name, String entp_name, String etc_otc_code, String ee_doc_data, String ud_doc_data, Bitmap item_image){
        this.item_name = item_name;
        this.entp_name = entp_name;
        this.etc_otc_code = etc_otc_code;
        this.ee_doc_data = ee_doc_data;
        this.ud_doc_data = ud_doc_data;
        this.item_image = item_image;
    }
    public String getMedicine_name() {
        return item_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.item_name = medicine_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getEntp_name() {
        return entp_name;
    }

    public void setEntp_name(String entp_name) {
        this.entp_name = entp_name;
    }

    public String getEtc_otc_code() {
        return etc_otc_code;
    }

    public void setEtc_otc_code(String etc_otc_code) {
        this.etc_otc_code = etc_otc_code;
    }

    public String getEe_doc_data() {
        return ee_doc_data;
    }

    public void setEe_doc_data(String ee_doc_data) {
        this.ee_doc_data = ee_doc_data;
    }

    public String getUd_doc_data() {
        return ud_doc_data;
    }

    public void setUd_doc_data(String ud_doc_data) {
        this.ud_doc_data = ud_doc_data;
    }

    public Bitmap getItem_image() {
        return item_image;
    }

    public void setItem_image(Bitmap item_image) {
        this.item_image = item_image;
    }
}
