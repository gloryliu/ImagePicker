package com.yokeyword.imagepicker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yokeyword.imagepicker.ImagePickerActivity;
import com.yokeyword.imagepicker.R;
import com.yokeyword.imagepicker.adapter.GlideFragmentAdapter;

import java.util.ArrayList;


/**
 * 图片浏览界面ViewPager
 * Created by Yokeyword on 2015/12/14.
 */
public class PreviewFragment extends Fragment {
    private static final String ARG_IMGS = "arg_imgs";
    private static final String ARG_IS_MULTIPLE = "arg_is_multiple";

    private static final int INIT_POSITION = 0;

    private Activity activity;
    private ViewPager viewPager;
    private TextView tvCount, tvBtnYes, tvPickPicCount;

    private boolean isMultiplePick;
    private ArrayList<String> imgList = new ArrayList<>();
    private GlideFragmentAdapter adapter;

    public static PreviewFragment newInstance(ArrayList<String> imgs, boolean isMultiplePick) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ARG_IMGS, imgs);
        bundle.putBoolean(ARG_IS_MULTIPLE, isMultiplePick);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgList = getArguments().getStringArrayList(ARG_IMGS);
        isMultiplePick = getArguments().getBoolean(ARG_IS_MULTIPLE, true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
        tvBtnYes = (TextView) view.findViewById(R.id.tv_btn_yes);
        tvPickPicCount = (TextView) view.findViewById(R.id.tv_pick_pic_count);

        tvPickPicCount.setText(String.format(getString(R.string.yo_select_pic_count), imgList.size()));

        if (imgList.size() > 1) {
            tvCount.setText(INIT_POSITION + 1 + "/" + imgList.size());
        } else {
            tvCount.setText("");
        }

        adapter = new GlideFragmentAdapter(getChildFragmentManager(), imgList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(INIT_POSITION);
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCount.setText(position + 1 + "/" + imgList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                if (!isMultiplePick) {
                    data.putExtra(ImagePickerActivity.EXTRA_SINGLE_PICKER, imgList.get(0));
                }
                data.putStringArrayListExtra(ImagePickerActivity.EXTRA_MULTIPLE_PICKER, imgList);
                activity.setResult(Activity.RESULT_OK, data);
                activity.finish();
            }
        });

    }
}
