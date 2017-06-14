package com.cst.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cst.scanner.BaseUI.BaseActivity;
import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Database.DatabaseHandler;
import com.cst.scanner.Model.FileObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cst.scanner.R.id.position;

public class ActivitySlider extends BaseActivity implements View.OnClickListener{
    private static ViewPager mPager;
    private TextView tvName,tvPosition;
    private LinearLayout llBack,llDelete,llKey,llUp,llCamera;
    private  int currentPage = 1;
    private ArrayList<FileObject> arrs;
    private ArrayList<String> arrsLink;
    DatabaseHandler db;
    private int positonOfPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider2);
        init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                if(Singleton.getGetInstance().where.equals("ScanActivity")) {
                    Singleton.getGetInstance().arrayList.clear();
                    Intent i = new Intent(ActivitySlider.this,MainActivity.class);
                    startActivity(i);
                    this.finish();
                } else if (Singleton.getGetInstance().where.equals("Document")) {
                    Singleton.getGetInstance().arrayList.clear();
                    this.finish();
                } else if (Singleton.getGetInstance().where.equals("adapterImage")) {
                    this.finish();
                }
                break;
            case R.id.llDelete:
                showDialog(R.layout.dialog_delete, new IClick() {
                    @Override
                    public void click() {
                        Singleton.getGetInstance().arrayList = new ArrayList<FileObject>();
                        Intent i = new Intent(ActivitySlider.this,MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void click2() {

                    }
                }, true);


                break;
            case R.id.llKey:
                navToByReplace(getSupportFragmentManager(),new FragmentSercurity(),
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

    private void init   () {
        db =  new DatabaseHandler(getApplicationContext());
        arrs = Singleton.getGetInstance().arrayList;
        tvName = (TextView) findViewById(R.id.nameDoc);
        tvPosition = (TextView) findViewById(position);
        llBack = (LinearLayout)findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llDelete = (LinearLayout)findViewById(R.id.llDelete);
        llDelete.setOnClickListener(this);
        llKey = (LinearLayout)findViewById(R.id.llKey);
        llKey.setOnClickListener(this);
        llUp = (LinearLayout)findViewById(R.id.llArrow);
        llUp.setOnClickListener(this);
        llCamera = (LinearLayout)findViewById(R.id.llCamera);
        llCamera.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        if (Singleton.getGetInstance().isStorage) {
            arrs = (ArrayList<FileObject>) db.getListObject();
            FileObject object = arrs.get(Singleton.getGetInstance().positionSeleted);
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
        mPager.setAdapter(new MyAdapter(getApplicationContext(), arrs,arrsLink));
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
    }

    @Override
    public void onBackPressed() {
        if(Singleton.getGetInstance().where.equals("ScanActivity")) {
            Singleton.getGetInstance().arrayList.clear();
            Intent i = new Intent(ActivitySlider.this,MainActivity.class);
            startActivity(i);
            this.finish();
        } else if (Singleton.getGetInstance().where.equals("Document")) {
            Singleton.getGetInstance().arrayList.clear();
            this.finish();
        } else if (Singleton.getGetInstance().where.equals("adapterImage")) {
            this.finish();
        }
    }
}
