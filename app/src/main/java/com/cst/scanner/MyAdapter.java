package com.cst.scanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by longdg on 13/06/2017.
 */

public class MyAdapter extends PagerAdapter {

    private ArrayList<FileObject> images;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> arrLinkPath;
    private IListViewClick iListViewClick;
    public MyAdapter(Context context, ArrayList<FileObject> images, ArrayList<String> arrLinkPath, IListViewClick iListViewClick) {
        this.context = context;
        this.images=images;
        this.arrLinkPath = arrLinkPath;
        inflater = LayoutInflater.from(context);
        this.iListViewClick = iListViewClick;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Singleton.getGetInstance().isStorage ? arrLinkPath.size() : images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);



            ImageView myImage = (ImageView) myImageLayout
                    .findViewById(R.id.image);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListViewClick.onClick(view,position);
            }
        });
            if(Singleton.getGetInstance().isStorage) {
                String obj = arrLinkPath.get(position);
                Picasso.with(context).load("file://" + obj).fit().centerCrop().into(myImage);
            } else {
                FileObject obj = images.get(position);
                Picasso.with(context).load("file://" + obj.getPathFile()).fit().centerCrop().into(myImage);
            }
            view.addView(myImageLayout, 0);


        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
