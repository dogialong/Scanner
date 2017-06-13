package com.cst.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cst.scanner.BaseUI.BaseFragment;

/**
 * Created by longdg on 06/06/2017.
 */

public class FragmentHelps extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helps,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.getInstance().getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getInstance().getButtonBack().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getNavTop().setVisibility(View.VISIBLE);
        MainActivity.getInstance().getTitleBarTop().setText(R.string.btnSupport);
        MainActivity.getInstance().getButtonBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }
}
