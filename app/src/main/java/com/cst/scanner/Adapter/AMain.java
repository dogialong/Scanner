package com.cst.scanner.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cst.scanner.BaseUI.Helper.Singleton;
import com.cst.scanner.Custom.BlurImage;
import com.cst.scanner.Delegate.IListViewClick;
import com.cst.scanner.Model.FileObject;
import com.cst.scanner.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by longdg on 17/04/2017.
 */

public class AMain extends RecyclerView.Adapter<AMain.MyViewHolder> {
    public List<FileObject> arr;
    public boolean isGrid;
    IListViewClick iListViewClick;
    Context context;
    private int BLUR_PRECENTAGE = 50;
    Bitmap bitmapMerge = null;
    public AMain(Context mContext, List<FileObject> arr, IListViewClick iListViewClick) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
        if (obj.getStatus().equals("yes")) {
            holder.border.setBackgroundResource(R.color.colorTvGreen);
            //Configure target for
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    bitmapMerge = overlay(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE),BitmapFactory.decodeResource(context.getResources(),
//                            R.drawable.lock));
                    holder.icon.setImageBitmap(BlurImage.fastblur(bitmap, 1f, BLUR_PRECENTAGE));

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    holder.icon.setImageResource(R.drawable.loading);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            holder.icon.setTag(target);
            Picasso.with(context)
                    .load("file://" + linkPath)
                    .error(R.drawable.loading)
                    .placeholder(R.drawable.loading)
                    .into(target);
            holder.lock.setVisibility(View.VISIBLE);
//            Picasso.with(context).load("file://" + linkPath).resize(holder.icon.getMaxWidth(),holder.icon.getMaxHeight()).centerCrop().into(target);

        } else {
            holder.lock.setVisibility(View.GONE);
            Picasso.with(context).load("file://" + linkPath).fit().centerCrop().into(holder.icon);
            holder.border.setBackgroundResource(R.color.colorTvRed);
        }
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
        public ImageView icon,lock;
        public LinearLayout delete,share,border;
        public TextView title,tvTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.ivIcon);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            delete = (LinearLayout) itemView.findViewById(R.id.share);
            share = (LinearLayout) itemView.findViewById(R.id.delete);
            border = (LinearLayout) itemView.findViewById(R.id.llBorder);
            lock = (ImageView) itemView.findViewById(R.id.imgLock);
        }
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        int width = bmp1.getWidth();
        int height = bmp1.getHeight();
        float centerX = (width  - bmp2.getWidth()) * 0.5f -((width  - bmp2.getWidth()) * 0.5f)/4;
        float centerY = (height- bmp2.getHeight()) * 0.5f;
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        bmp2 =  Bitmap.createBitmap(bmp2, 0, 0, bmp2.getWidth(), bmp2.getHeight(), matrix, true);
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, centerX, centerY, null);
        return bmOverlay;
    }
}



