package com.cst.scanner.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.cst.scanner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by longdg on 17/04/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    public List<FileObject> arr;
    public boolean isGrid;
    IListViewClick iListViewClick;
    Context context;
    public MainAdapter(Context mContext,List<FileObject> arr, IListViewClick iListViewClick) {
        this.arr = arr;
        this.iListViewClick = iListViewClick;
        this.context = mContext;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.storage_layout, null);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        FileObject obj = arr.get(position);
        holder.title.setText(obj.getNameFile().substring(31,obj.getNameFile().length()));
        Log.d("MainAdapter", "onBindViewHolder: " + obj.getNameFile() + "  " + obj.getPathFile());
        Picasso.with(context).load("file://" + obj.getNameFile()).fit().centerCrop().into(holder.icon);
//        holder.icon.setImageResource(obj.getRes());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListViewClick.onClick(view,position);
            }
        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListViewClick.onClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title;
        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.ivIcon);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}



