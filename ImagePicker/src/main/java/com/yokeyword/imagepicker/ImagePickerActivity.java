package com.yokeyword.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yokeyword.imagepicker.fragments.DetailExploreFragment;
import com.yokeyword.imagepicker.fragments.ExploreFragment;
import com.yokeyword.imagepicker.fragments.PreviewFragment;

import java.util.ArrayList;

/**
 * 图库浏览界面
 * Created by Yokeyword on 2015/12/14.
 */
public class ImagePickerActivity extends FragmentActivity implements ExploreFragment.OnSwitchDirCallback, DetailExploreFragment.OnPreviewCallback {
    public static final String EXTRA_MULTIPLE_PICKER = "extra_multiple_picker";
    public static final String EXTRA_SINGLE_PICKER = "extra_single_picker";
    private static final String EXTRA_IS_MULTIPLE = "extra_is_multiple";

    private TextView tvBarTitle;
    private boolean isMultiplePick = true;

    public static Intent getCallingIntent(Context context, boolean multiplePick) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(EXTRA_IS_MULTIPLE, multiplePick);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMultiplePick = bundle.getBoolean(EXTRA_IS_MULTIPLE, true);
        }

        initView();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, ExploreFragment.newInstance()).commit();
            tvBarTitle.setText(R.string.yo_pick_pic);
        } else {
            // 回退栈里包含 PicPreviewFragment时 TitleBar标题为"预览"
            if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
                tvBarTitle.setText(R.string.yo_preview);
            }
        }

    }

    protected void initView() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_bar_back);
        tvBarTitle = (TextView) findViewById(R.id.tv_bat_title);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 切换目录
     */
    private void switchDir(String bucket_name) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.add(R.id.container, DetailExploreFragment.newInstance(bucket_name,isMultiplePick ));
        trans.addToBackStack(null);
        try {
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预览
     */
    private void preview(ArrayList<String> imgs) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.add(R.id.container, PreviewFragment.newInstance(imgs,isMultiplePick));
        trans.addToBackStack(null);
        try {
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            tvBarTitle.setText(R.string.yo_pick_pic);
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSwitchDir(String bucket_name) {
        switchDir(bucket_name);
    }

    @Override
    public void onPreview(ArrayList<String> imgs) {
        tvBarTitle.setText(R.string.yo_preview);
        preview(imgs);
    }
}
