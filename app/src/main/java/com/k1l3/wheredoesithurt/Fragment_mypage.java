package com.k1l3.wheredoesithurt;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static android.support.constraint.Constraints.TAG;

public class Fragment_mypage extends Fragment {
    View viewGroup;
    String image,name,email;
    LinearLayout my_default_time,my;
    FragmentManager manager;
    FragmentTransaction transaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_mypage,container, false);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        ((MainActivity)getActivity()).toolbar_mypage();
        image = getArguments().getString("profile");
        name = getArguments().getString("name");
        email = getArguments().getString("email");
        ImageView profileImage = viewGroup.findViewById(R.id.profileImage);
        TextView profileName = viewGroup.findViewById(R.id.profileName);
        TextView profileEmail = viewGroup.findViewById(R.id.email);
        Glide.with(this).load(image).into(profileImage);
        profileName.setText(name);
        profileEmail.setText(email);
        //todo 프로필 사진 라운딩 및 크기맞추기
        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        profileImage.setClipToOutline(true);

        Button logout_button = (Button) viewGroup.findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).logout();
            }
        });
        //기본 알람시간 페이지로 가기
        my_default_time = (LinearLayout)viewGroup.findViewById(R.id.my_default_time);
        my_default_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_my_default_time());
            }
        });
        //나의 정보 페이지로 가기

        return viewGroup;
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        transaction= manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
        Log.e(TAG,"값 : " + String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }
}
