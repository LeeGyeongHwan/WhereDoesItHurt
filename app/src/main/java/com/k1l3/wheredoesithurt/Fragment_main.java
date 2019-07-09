package com.k1l3.wheredoesithurt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_main extends Fragment {
    EditText medicine_search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        final View viewGroup = inflater.inflate(R.layout.fragment_main,container, false);
        medicine_search=(EditText)viewGroup.findViewById(R.id.medicin_search);
        medicine_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        medicine_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!medicine_search.getText().toString().equals("")) {
                    Fragment fragment_search = new Fragment_search();
                    Bundle bundle = new Bundle();
                    bundle.putString("search_word", medicine_search.getText().toString());
                    fragment_search.setArguments(bundle);
                    replaceFragment(fragment_search);
                }
                else{
                    Toast.makeText(getContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return viewGroup;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void replaceFragment(@NonNull Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }


}
