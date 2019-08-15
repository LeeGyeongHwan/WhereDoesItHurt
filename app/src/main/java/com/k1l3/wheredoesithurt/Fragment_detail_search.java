package com.k1l3.wheredoesithurt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment_detail_search extends Fragment {
    View viewGroup;
    Search_item item;
    TextView name, coper_name, medicin_type, medicin_effect, medicin_ud_doc;
    ImageView medicine_image;
    LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_detail_search, container, false);
        medicine_image = (ImageView) viewGroup.findViewById(R.id.medicine_image);
        name = (TextView) viewGroup.findViewById(R.id.name);
        coper_name = (TextView) viewGroup.findViewById(R.id.coper_name);
        medicin_type = (TextView) viewGroup.findViewById(R.id.medicin_type);
        medicin_effect = (TextView) viewGroup.findViewById(R.id.medicin_effect);
        medicin_ud_doc = (TextView) viewGroup.findViewById(R.id.medicin_ud_doc);
        linearLayout = (LinearLayout) viewGroup.findViewById(R.id.detail_search_linear);
        if (getArguments() != null) {
            item = (Search_item) getArguments().getSerializable("data");
            if (item.getItem_image() != null) {
                medicine_image.setImageBitmap(item.getItem_image());
                linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                medicine_image.setVisibility(View.GONE);
                linearLayout.setGravity(Gravity.CENTER);
            }
            name.setText(item.getItem_name());
            coper_name.setText(item.getEntp_name());
            medicin_type.setText(item.getEtc_otc_code());
            medicin_effect.setText(item.getEe_doc_data());
            medicin_ud_doc.setText(item.getUd_doc_data());
        }
        return viewGroup;
    }


}
