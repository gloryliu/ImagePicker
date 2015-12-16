package com.yokeyword.imagepicker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yokeyword.imagepicker.fragments.GlideFragment;

import java.util.List;

/**
 * 图片Fragment 适配器
 * Created by Yokeyword on 2015/12/14.
 */
public class GlideFragmentAdapter extends FragmentPagerAdapter {
    private List<String> imgs;

    private int mCount = 0;

    public GlideFragmentAdapter(FragmentManager fm, List<String> imgs) {
        super(fm);
        this.imgs = imgs;
        mCount = imgs.size();
    }

    @Override
    public Fragment getItem(int position) {
        return GlideFragment.newInstance(imgs.get(position % mCount));
    }

    @Override
    public int getCount() {
        return mCount;
    }
}