package com.cst.scanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.cst.scanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by longdg on 11/05/2017.
 */

public class ImageAdapter extends BaseAdapter {
    ArrayList<FileObject> objs;
    Context context;
    IListViewClick iListViewClick;
    public ImageAdapter(Context context, ArrayList<FileObject> objs,IListViewClick iListViewClick) {
        this.objs = objs;
        this.context = context;
        this.iListViewClick = iListViewClick;
    }

    @Override
    public int getCount() {
        return objs != null ? objs.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LVViewHolder holder = null;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_lv, parent,false);
            holder = new LVViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.imgview);
            convertView.setTag(holder);
        } else {
            holder = (LVViewHolder) convertView.getTag();
        }
        Picasso.with(context).load("file://" + objs.get(position).getPathFile()).fit().centerCrop().into(holder.img);
        return convertView;
    }

    public class LVViewHolder {

        public ImageView img;

        public LVViewHolder() {
        }
    }
}

