package com.cst.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cst.scanner.BaseUI.BaseFragment;
import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Database.DatabaseHandler;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cst.scanner.R.id.position;

public class ActivitySlider extends BaseFragment implements View.OnClickListener{
    private static ViewPager mPager;
    private TextView tvName,tvPosition,tvComment;
    private LinearLayout llBack,llDelete,llKey,llUp,llCamera;
    private  int currentPage = 1;
    private ArrayList<FileObject> arrs;
    private ArrayList<String> arrsLink;
    DatabaseHandler db;
    private int positonOfPage;
    private SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_slider,container,false);
        init(view);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                if(Singleton.getGetInstance().where.equals("ScanActivity")) {
                    Singleton.getGetInstance().arrayList.clear();
                    Intent i = new Intent(getActivity(),MainActivity.class);
                    startActivity(i);

                } else if (Singleton.getGetInstance().where.equals("Document")) {
                    MainActivity.getInstance().getRelaNavTop().setVisibility(View.VISIBLE);
                    Singleton.getGetInstance().arrayList.clear();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else if (Singleton.getGetInstance().where.equals("adapterImage")) {
                    MainActivity.getInstance().getRelaNavTop().setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.llDelete:
                showDialog(R.layout.dialog_delete, new IClick() {
                    @Override
                    public void click() {
                        Singleton.getGetInstance().arrayList = new ArrayList<FileObject>();
                        Intent i = new Intent(getActivity(),MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void click2() {

                    }
                }, true);


                break;
            case R.id.llKey:
                navToByReplace(getActivity().getSupportFragmentManager(),new FragmentSercurity(),
                        "FragmentSercurity","FragmentSercurity",true,R.id.rlMain);
                Singleton.getGetInstance().linkUrlImage= arrs.get(positonOfPage).getPathFile();
                break;
            case R.id.llArrow:
                break;
            case R.id.llCamera:
                break;
            default:break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init   (View v) {

        preferences = getActivity().getSharedPreferences(Singleton.getGetInstance().KEY_SHAREPREFERENCE, Context.MODE_PRIVATE);
        db =  new DatabaseHandler(getContext());
        arrs = Singleton.getGetInstance().arrayList;
        tvName = (TextView) v.findViewById(R.id.nameDoc);
        tvPosition = (TextView)v.findViewById(position);
        tvComment = (TextView) v.findViewById(R.id.comment);
        tvComment.setMovementMethod(new ScrollingMovementMethod());
        llBack = (LinearLayout)v.findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llDelete = (LinearLayout)v.findViewById(R.id.llDelete);
        llDelete.setOnClickListener(this);
        llKey = (LinearLayout)v.findViewById(R.id.llKey);
        llKey.setOnClickListener(this);
        llUp = (LinearLayout)v.findViewById(R.id.llArrow);
        llUp.setOnClickListener(this);
        llCamera = (LinearLayout)v.findViewById(R.id.llCamera);
        llCamera.setOnClickListener(this);
        mPager = (ViewPager)v.findViewById(R.id.pager);
        if (Singleton.getGetInstance().isStorage) {
            arrs = (ArrayList<FileObject>) db.getListObject();
            FileObject object = arrs.get(Singleton.getGetInstance().positionSeleted);
            Singleton.getGetInstance().fileObject = object;
            arrsLink =  new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(object.getImage());
                JSONArray items = jsonObject.getJSONArray(Singleton.getGetInstance().key_json);
                for (int i = 0; i < items.length(); i++) {
                    arrsLink.add(items.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            arrs = Singleton.getGetInstance().arrayList;
            arrsLink = new ArrayList<>();
        }
        mPager.setAdapter(new MyAdapter(getContext(), arrs, arrsLink, new IListViewClick() {
            @Override
            public void onClick(View v, int position) {
                if(Singleton.getGetInstance().isStorage) {
                    Singleton.getGetInstance().linkImagePreview = arrsLink.get(position);
                    navToByAdd(getActivity().getSupportFragmentManager(),new FragmentPreview(),"FragmentPreview","FragmentPreview",true,R.id.rlMain);
                } else {

                }

            }
        }));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positonOfPage = position;
                if(Singleton.getGetInstance().isStorage) {
                    tvPosition.setText(position+1 + "/" + arrsLink.size());
                } else {
                    tvPosition.setText(position+1 + "/" + arrs.size());
                }
                if (preferences.getString(arrsLink.get(position),"").equals("")) {
                    tvComment.setVisibility(View.GONE);
                } else {
                    tvComment.setText(preferences.getString(arrsLink.get(position),""));
                    tvComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tvName.setText(arrs.get(0).getNameFile());
        if(Singleton.getGetInstance().isStorage) {
            tvPosition.setText(currentPage + "/" + arrsLink.size());
        } else {
            tvPosition.setText(currentPage + "/" + arrs.size());
        }
        if (arrsLink.size()> 0) {
            if (preferences.getString(arrsLink.get(0),"").equals("")) {
                tvComment.setVisibility(View.GONE);
            } else {
                tvComment.setText(preferences.getString(arrsLink.get(0),""));
                tvComment.setVisibility(View.VISIBLE);
            }
        }
    }

}
