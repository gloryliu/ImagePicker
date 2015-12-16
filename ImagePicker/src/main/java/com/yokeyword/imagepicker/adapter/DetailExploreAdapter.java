package com.yokeyword.imagepicker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.yokeyword.imagepicker.R;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片空间适配器
 * Created by Yokeyword on 2015/12/14.
 */
public class DetailExploreAdapter extends BaseAdapter {

    private Fragment fragment;
    private AbsListView absListView;
    private LayoutInflater inflater;
    private List<File> items = new ArrayList<>();
    private ViewHolder holder;

    // 用来控制CheckBox的选中状况
    private static List<Boolean> mChecked;

    private int width, height;

    public DetailExploreAdapter(Context context, Fragment fragment, AbsListView absListView) {
        this.fragment = fragment;
        this.absListView = absListView;
        inflater = LayoutInflater.from(context);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = (dm.widthPixels - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2 * 4, context.getResources().getDisplayMetrics())) / 3;
        height = width;
    }

    public void setDatas(List<File> beans) {
        items.clear();
        items.addAll(beans);
        initData();
        notifyDataSetChanged();
    }

    private void initData() {
        mChecked = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mChecked.add(false);
        }
    }

    public boolean getChecked(int position) {
        return mChecked.get(position);
    }

    public void setChecked(int position) {
        mChecked.set(position, !mChecked.get(position));
        notifyItemChanged(absListView, position);
    }

    public boolean isNoneChecked() {
        for (int i = 0; i < getCount(); i++) {
            if (getChecked(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 得到选中的文件path
     *
     * @return
     */
    public ArrayList<String> getCheckedList() {
        ArrayList<String> checkedFiles = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (mChecked.get(i)) {
                File f = items.get(i);
                checkedFiles.add(f.getAbsolutePath());
            }
        }

        return checkedFiles;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public File getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_explore_detail, parent, false);
            GridView.LayoutParams params = new GridView.LayoutParams(width, height);
            convertView.setLayoutParams(params);

            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        File item = items.get(position);
        Glide.with(fragment)
                .load(new File(item.getAbsolutePath()))
                .placeholder(R.drawable.bg_grey)
                .centerCrop()
                .crossFade()
                .into(holder.pic);

        if (mChecked.get(position)) {
            holder.pic.setAlpha(0.4f);
            holder.checkbox.setChecked(true);
        } else {
            holder.pic.setAlpha(1f);
            holder.checkbox.setChecked(false);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView pic;
        CheckBox checkbox;
    }

    private void notifyItemChanged(AbsListView absView, int position) {

        if (absView != null) {
            int start = absView.getFirstVisiblePosition();
            for (int i = start, j = absView.getLastVisiblePosition(); i <= j; i++)
                if (position == i) {
                    View view = absView.getChildAt(i - start);
                    getView(i, view, absView);
                    break;
                }
        }
    }
}
