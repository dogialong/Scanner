package com.cst.scanner.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AImage2 extends RecyclerView.Adapter<AImage2.MyViewHolder> {

    ArrayList<String> objs;
    Context context;
    IListViewClick iListViewClick;

    public AImage2(Context context, ArrayList<String> objs, IListViewClick iListViewClick) {
        this.objs = objs;
        this.context = context;
        this.iListViewClick = iListViewClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lv, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        Picasso.with(context).load("file://" + objs.get(position)).fit().centerCrop().into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListViewClick.onClick(view,position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return objs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            img = (ImageView) itemView.findViewById(R.id.imgview);

        }
    }
}
