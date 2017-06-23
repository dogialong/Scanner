package com.cst.scanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cst.scanner.Adapter.AHelps;
import com.cst.scanner.BaseUI.BaseFragment;

import java.util.ArrayList;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by longdg on 16/06/2017.
 */

public class FragmentHelp extends BaseFragment {
    ImageView imgX;
    ViewPager pager;
    private int currentPage = 0 ;
    private static final Integer[] ArrayImage= {R.drawable.img1,R.drawable.img2,R.drawable.img3e,
            R.drawable.img4e,R.drawable.img5};
    private static final Integer[] ArrayImageV= {R.drawable.img1v,R.drawable.img2v,R.drawable.img3,
            R.drawable.img4v,R.drawable.img5v};
    private ArrayList<Integer> ImageArray = new ArrayList<Integer>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_helps,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (  Locale.getDefault().getDisplayLanguage().equals("Tiếng Việt")) {
            for (int i = 0; i < ArrayImage.length ; i++) {
                ImageArray.add(ArrayImageV[i]);
            }
        } else {
            for (int i = 0; i < ArrayImage.length ; i++) {
                ImageArray.add(ArrayImage[i]);
            }
        }


        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new AHelps(getContext(),ImageArray));
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        imgX = (ImageView) view.findViewById(R.id.imgX);
        imgX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
