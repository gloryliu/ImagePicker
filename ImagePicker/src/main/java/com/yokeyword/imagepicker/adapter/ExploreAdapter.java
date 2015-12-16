package com.yokeyword.imagepicker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yokeyword.imagepicker.R;

import com.bumptech.glide.Glide;
import com.yokeyword.imagepicker.model.BucketEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 相册 适配器
 * Created by Yokeyword on 2015/12/14.
 */
public class ExploreAdapter extends BaseAdapter {
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private List<BucketEntity> items = new ArrayList<>();
    private ViewHolder holder;

    private int width, height;

    public ExploreAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = (dm.widthPixels - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3 * 8, context.getResources().getDisplayMetrics())) / 2;
        height = (int) (width * 1.0);
    }

    public void setDatas(List<BucketEntity> beans) {
        items.clear();
        items.addAll(beans);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BucketEntity getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_explore, parent, false);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            holder.pic.setLayoutParams(params);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BucketEntity item = items.get(position);

        Glide.with(fragment)
                .load(new File(item.getPath()))
                .placeholder(R.drawable.bg_grey)
                .centerCrop()
                .crossFade()
                .into(holder.pic);

        String bucket_name = item.getName();
        if (bucket_name.toLowerCase().equals("camera")) {
            holder.name.setText(com.yokeyword.imagepicker.R.string.yo_my_pic);
        } else if (bucket_name.toLowerCase().equals("screenshots")) {
            holder.name.setText(com.yokeyword.imagepicker.R.string.yo_screenshots);
        } else {
            holder.name.setText(bucket_name);
        }

        holder.count.setText(String.format(context.getString(com.yokeyword.imagepicker.R.string.yo_count), item.getCount()));

        return convertView;
    }

    private class ViewHolder {
        ImageView pic;
        TextView name, count;
    }
}
