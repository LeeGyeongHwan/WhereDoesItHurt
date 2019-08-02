package com.k1l3.wheredoesithurt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Search_itemView extends LinearLayout {
    TextView medicine_name;
    ImageView search_medicine_image;
    public Search_itemView(Context context) {
        super(context);
        init(context);
    }

    public Search_itemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_item,this,true);
        medicine_name = (TextView)findViewById(R.id.medicin_name);
        search_medicine_image = (ImageView)findViewById(R.id.search_medicine_image);
        search_medicine_image.setBackground(new ShapeDrawable(new OvalShape()));
        search_medicine_image.setClipToOutline(true);

    }

    public TextView getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String name) {
        this.medicine_name.setText(name);
    }

    public ImageView getSearch_medicine_image(){ return search_medicine_image;}

    public void setSearch_medicine_image(Bitmap image){ this.search_medicine_image.setImageBitmap(image);}
}
