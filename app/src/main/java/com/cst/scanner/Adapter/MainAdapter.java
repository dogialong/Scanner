package com.cst.scanner.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.cst.scanner.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        JSONObject json = null;
        String linkPath = "";
        JSONArray items;
        try {
            json = new JSONObject(obj.getImage());
            items = json.getJSONArray(Singleton.getGetInstance().key_json);
            linkPath = items.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.title.setText(obj.getNameFile().substring(0,14));
        String time = obj.getNameFile().substring(15,20);
        time = time.replace("-",":");
        holder.tvTime.setText(time);
        Picasso.with(context).load("file://" + linkPath).fit().centerCrop().into(holder.icon);
//        holder.icon.setImageResource(obj.getRes());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListViewClick.onClick(view,position);
            }
        });
        holder.tvTime.setOnClickListener(new View.OnClickListener() {
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListViewClick.onClick(view,position);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
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
    public void loadNewList(List<FileObject> arr) {
        this.arr.clear();
        this.arr.addAll(arr);
        notifyDataSetChanged();

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon,delete,share;
        public TextView title,tvTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.ivIcon);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            delete = (ImageView) itemView.findViewById(R.id.share);
            share = (ImageView) itemView.findViewById(R.id.delete);
        }
    }
}



