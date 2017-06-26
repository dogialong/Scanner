package com.cst.scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cst.scanner.BaseUI.BaseFragment;
import com.cst.scanner.BaseUI.Helper.Singleton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by longdg on 2017-06-20.
 */

public class FragmentPreview extends BaseFragment implements View.OnClickListener{
    String url;
    ImageView img;
    LinearLayout llX,llTurnL,llTurnR,llComment,llEdit,llBack;
    int countR, countL;
    EditText edComment;
    Bitmap bitmapRotate = null;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_preview,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = getActivity().getSharedPreferences(Singleton.getGetInstance().KEY_SHAREPREFERENCE, Context.MODE_PRIVATE);
        editor = preferences.edit();
        img = (ImageView) view.findViewById(R.id.img);
        llTurnL = (LinearLayout) view.findViewById(R.id.llTurnL);
        llTurnR = (LinearLayout) view.findViewById(R.id.llTurnR);
        llBack = (LinearLayout) view.findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llTurnR.setOnClickListener(this);
        llTurnL.setOnClickListener(this);
        llComment = (LinearLayout) view.findViewById(R.id.llComent);
        llComment.setOnClickListener(this);
        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview).fit().centerInside().into(img);
        edComment = (EditText) view.findViewById(R.id.edComment);
        edComment.setFocusable(false);
        edComment.setClickable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTurnL:
                switch (countL) {
                    case 0:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(-90).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        countL++;
                        break;
                    case 1:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(-180).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        countL++;
                        break;
                    case 2:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(-270).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        countL++;
                        break;
                    case 3:
                        countL=0;
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(-360).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        break;
                    default:break;
                }

                break;
            case R.id.llTurnR:
                switch (countR) {
                    case 0:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(90).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        countR++;
                        break;
                    case 1:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(180).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        countR++;
                        break;
                    case 2:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(270).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        countR++;
                        break;
                    case 3:
                        Picasso.with(getContext()).load("file://"+Singleton.getGetInstance().linkImagePreview)
                                .rotate(360).fit().centerInside().into(img);
                        bitmapRotate = getBitmapFromImageViewDrawCanvas(img);
                        break;
                    default:break;
                }
                break;
            case R.id.llComent:
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                edComment.setFocusableInTouchMode(true);
                if(edComment.requestFocus()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                break;
            case R.id.llBack:

                getActivity().getSupportFragmentManager().popBackStack();
                if (bitmapRotate != null) {
                    saveImage(bitmapRotate,Singleton.getGetInstance().linkImagePreview);
                }
                editor.putString(Singleton.getGetInstance().linkImagePreview,edComment.getText().toString());
                editor.commit();
            default: break;
        }
    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public  String saveImage(Bitmap bitmap,String namefile) {
        boolean success = false;
        String filePath = "";

        File image = new File(namefile);
        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
//            bitmap.createScaledBitmap(bitmap, 300, 300, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (success) {
            filePath = image.getAbsolutePath();
        } else {
            Log.d(TAG, "saveImage: failed");
        }

        return filePath;
    }



}
