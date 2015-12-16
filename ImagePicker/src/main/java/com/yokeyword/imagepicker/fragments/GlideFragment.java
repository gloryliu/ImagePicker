package com.yokeyword.imagepicker.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yokeyword.imagepicker.R;

/**
 * 图片Fragment
 * * Created by Yokeyword on 2015/12/14.
 */
public class GlideFragment extends Fragment {
    private static final String ARG_URL = "arg_url";
    private String url;

    public static GlideFragment newInstance(String url) {
        GlideFragment fragment = new GlideFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(ARG_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_glide, container, false);
        final ImageView imgView = (ImageView) view.findViewById(R.id.img);
        // 如果需要加载gif图 建议只显示gif的第一帧 即使用asBitmap
//        if (url.endsWith(".gif")) {
//            Glide.with(this)
//                    .load(url)
//                    .asBitmap()
//                    .animate(android.R.anim.fade_in)
//                    .fitCenter()
//                    .into(imgView);
//        }
        Glide.with(this)
                .load(url)
                .fitCenter()
                .crossFade()
                .into(imgView);

        return view;
    }
}
