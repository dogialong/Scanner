package com.cst.scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cst.scanner.BaseUI.BaseActivity;
import com.cst.scanner.BaseUI.Helper.Constant;
import com.cst.scanner.BaseUI.Helper.ImageHelper;
import com.cst.scanner.Database.DatabaseHandler;
import com.cst.scanner.Model.FileObject;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_CODE = 99;
    LinearLayout llAboutUs,llFiles,llNew,llHelp,llNav;
    RelativeLayout rlNav;
    TextView tvHomeBar;
    Button btnHomeBar,btnBack;
    public static MainActivity mainActivity;
    DatabaseHandler db ;
    public static android.support.v4.app.FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    // region initview
    private void init() {
        permissionsRequired();
        mainActivity = this;
        db = new DatabaseHandler(getApplicationContext());

        fragmentManager = getSupportFragmentManager();
        rlNav = (RelativeLayout) findViewById(R.id.bg_mavigation_top);
        llAboutUs = (LinearLayout) findViewById(R.id.llAboutus);
        llAboutUs.setOnClickListener(this);
        llFiles = (LinearLayout) findViewById(R.id.llFiles);
        llFiles.setOnClickListener(this);
        llNew = (LinearLayout) findViewById(R.id.llNew);
        llNew.setOnClickListener(this);
        llHelp = (LinearLayout) findViewById(R.id.llHelp);
        llHelp.setOnClickListener(this);
        llNav = (LinearLayout) findViewById(R.id.nav);
        llNav.setVisibility(View.GONE);

        tvHomeBar = (TextView) findViewById(R.id.tvHomeBar);
        btnHomeBar = (Button) findViewById(R.id.icback);
        btnHomeBar.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.icback1);
    }
    public TextView getTitleBarTop() {
        return tvHomeBar;
    }
    public Button getButtonBack() {
        return btnHomeBar;
    }
    public Button getButtonBac1k() {
        return btnBack;
    }
    public LinearLayout getNavTop() {
        return llNav;
    }
    public RelativeLayout getRelaNavTop() {
        return rlNav;
    }
    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getNavTop().setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llFiles:
                resetBackgroundCircle();
                llFiles.setBackgroundResource(R.drawable.circle_red);
                navToByReplace(fragmentManager,new FStorage(),"FStorage","FStorage",true,R.id.main_contain);
                break;
            case R.id.llAboutus:
                resetBackgroundCircle();
                llAboutUs.setBackgroundResource(R.drawable.circle_red);
                navToByReplace(fragmentManager,new FAboutUs(),"FAboutUs","FAboutUs",true,R.id.main_contain);
                break;
            case R.id.llNew:
                resetBackgroundCircle();
                llNew.setBackgroundResource(R.drawable.circle_red);
                startScan(Constant.OPEN_CAMERA);
                break;
            case R.id.llHelp:
                resetBackgroundCircle();
                llHelp.setBackgroundResource(R.drawable.circle_red);
                break;
            case R.id.icback:
                break;
            default:break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                String filePath = ImageHelper.saveImage(bitmap,"ScannerApp");
                db.addImage(new FileObject(filePath.substring(31,filePath.length()-1),filePath,"20170505","lock"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }
    public void resetBackgroundCircle() {
        llAboutUs.setBackgroundResource(R.drawable.circle_none);
        llNew.setBackgroundResource(R.drawable.circle_none);
        llHelp.setBackgroundResource(R.drawable.circle_none);
        llFiles.setBackgroundResource(R.drawable.circle_none);
    }

}
