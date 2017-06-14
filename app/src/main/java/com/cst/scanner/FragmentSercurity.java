package com.cst.scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.scanner.BaseUI.BaseFragment;
import com.cst.scanner.BaseUI.Helper.Singleton;

import java.util.ArrayList;


/**
 * Created by longdg on 13/06/2017.
 */

public class FragmentSercurity extends BaseFragment implements View.OnClickListener {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv0, tvBack, tvOk;
    ImageView img1, img2, img3, img4, img5, img6;
    EditText edPass;
    int count = 0;
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList<ImageView> arr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.key_sercurity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences(Singleton.getGetInstance().KEY_SHAREPREFERENCE, Context.MODE_PRIVATE);
        editor = preferences.edit();
        arr = new ArrayList<>();
        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);
        img4 = (ImageView) view.findViewById(R.id.img4);
        img5 = (ImageView) view.findViewById(R.id.img5);
        img6 = (ImageView) view.findViewById(R.id.img6);

        arr.add(img1);
        arr.add(img2);
        arr.add(img3);
        arr.add(img4);
        arr.add(img5);
        arr.add(img6);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv1.setOnClickListener(this);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv2.setOnClickListener(this);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv3.setOnClickListener(this);
        tv4 = (TextView) view.findViewById(R.id.tv4);
        tv4.setOnClickListener(this);
        tv5 = (TextView) view.findViewById(R.id.tv5);
        tv5.setOnClickListener(this);
        tv6 = (TextView) view.findViewById(R.id.tv6);
        tv6.setOnClickListener(this);
        tv7 = (TextView) view.findViewById(R.id.tv7);
        tv7.setOnClickListener(this);
        tv8 = (TextView) view.findViewById(R.id.tv8);
        tv8.setOnClickListener(this);
        tv9 = (TextView) view.findViewById(R.id.tv9);
        tv9.setOnClickListener(this);
        tv0 = (TextView) view.findViewById(R.id.tv0);
        tv0.setOnClickListener(this);
        tvBack = (TextView) view.findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv0:
                if(count < 6) {
                    stringBuilder.append("0");
                    count++;
                }
                update(count);
                break;
            case R.id.tv1:
                if(count < 6) {
                    stringBuilder.append("1");
                    count++;
                }
                update(count);
                break;
            case R.id.tv2:
                if(count < 6) {
                    stringBuilder.append("2");
                    count++;
                }
                update(count);
                break;
            case R.id.tv3:
                if(count < 6) {
                    stringBuilder.append("3");
                    count++;
                }
                update(count);
                break;
            case R.id.tv4:
                if(count < 6) {
                    stringBuilder.append("4");
                    count++;
                }
                update(count);
                break;
            case R.id.tv5:
                if(count < 6) {
                    stringBuilder.append("5");
                    count++;
                }
                update(count);
                break;
            case R.id.tv6:
                if(count < 6) {
                    stringBuilder.append("6");
                    count++;
                }
                update(count);
                break;
            case R.id.tv7:
                if(count < 6) {
                    stringBuilder.append("7");
                    count++;
                }
                update(count);
                break;
            case R.id.tv8:
                if(count < 6) {
                    stringBuilder.append("8");
                    count++;
                }
                update(count);
                break;
            case R.id.tv9:
                if(count < 6) {
                    stringBuilder.append("9");
                    count++;
                }
                update(count);
                break;
            case R.id.tvBack:
                delete();
                break;
            case R.id.tvOk:
                if (count < 6) {
                    showDialog(R.layout.dialog_sercurity, new IClick() {
                        @Override
                        public void click() {

                        }

                        @Override
                        public void click2() {

                        }
                    }, false);
                } else {
                    Log.d(TAG, "stringBuilder: " + stringBuilder.toString());
                    editor.putString(Singleton.getGetInstance().linkUrlImage,stringBuilder.toString());
                    editor.commit();
                    navToByReplace(getActivity().getSupportFragmentManager(),new FStorage(),"FStorage","FStorage",true,R.id.rlKey);
                }

                break;
            default:
                break;
        }
    }

    private void update(int count) {
        if (count <= 6) {
            for (int i = 0; i < count; i++) {
                arr.get(i).setVisibility(View.VISIBLE);
            }
        }

    }

    private void delete() {
        stringBuilder.delete(0,stringBuilder.length());
        count = 0;
        for (int i = 0; i < arr.size(); i++) {
            arr.get(i).setVisibility(View.GONE);
        }


    }
}
