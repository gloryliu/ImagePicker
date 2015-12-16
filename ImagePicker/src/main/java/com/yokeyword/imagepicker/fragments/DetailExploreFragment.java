package com.yokeyword.imagepicker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yokeyword.imagepicker.ImagePickerActivity;
import com.yokeyword.imagepicker.R;
import com.yokeyword.imagepicker.adapter.DetailExploreAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 手机上图片详细 界面
 * Created by Yokeyword on 2015/12/14.
 */
public class DetailExploreFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String ARG_BUCKET = "arg_bucket";
    private static final String ARG_IS_MULTIPLE = "arg_is_multiple";

    private GridView gridView;
    private TextView tvBtnPreview, tvBtnYes, tvPickPicCount;
    private Activity activity;

    private OnPreviewCallback callback;

    // 专辑名称
    private String bucket_name = "";
    private boolean isMultiplePick;
    private int lastPosition = -1;
    DetailExploreAdapter adapter;

    public static DetailExploreFragment newInstance(String bucket_name, boolean isMultiplePick) {
        DetailExploreFragment fragment = new DetailExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_BUCKET, bucket_name);
        bundle.putBoolean(ARG_IS_MULTIPLE, isMultiplePick);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnPreviewCallback {
        void onPreview(ArrayList<String> imgs);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        try {
            callback = (OnPreviewCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnPreviewCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_detail, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        new PicDetailExploreTask().execute();
    }

    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.gridView);
        tvBtnPreview = (TextView) view.findViewById(R.id.tv_btn_preview);
        tvBtnYes = (TextView) view.findViewById(R.id.tv_btn_yes);
        tvPickPicCount = (TextView) view.findViewById(R.id.tv_pick_pic_count);

        gridView.setOnItemClickListener(this);
        tvBtnYes.setOnClickListener(this);
        tvBtnPreview.setOnClickListener(this);

        adapter = new DetailExploreAdapter(activity, DetailExploreFragment.this, gridView);
        gridView.setAdapter(adapter);
    }

    class PicDetailExploreTask extends AsyncTask<String, Integer, List<File>> {

        @Override
        protected List<File> doInBackground(String... params) {
            return getPics();
        }

        @Override
        protected void onPostExecute(List<File> files) {
            super.onPostExecute(files);
            adapter.setDatas(files);
        }
    }

    private List<File> getPics() {
        List<File> listFiles = new ArrayList<>();

        String[] mediaColumns = new String[]{
                MediaStore.Images.Media.DATA,
        };
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";

        Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaColumns, selection, new String[]{bucket_name}, null);
        assert cursor != null;
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if (data.endsWith(".gif")) {
                continue;
            }
            listFiles.add(new File(data));
        }
        cursor.close();

        return listFiles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bucket_name = getArguments().getString(ARG_BUCKET);
        isMultiplePick = getArguments().getBoolean(ARG_IS_MULTIPLE, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isMultiplePick) {  // single mode
            if (lastPosition >= 0 && position != lastPosition) {
                adapter.setChecked(lastPosition);
            }
        }

        adapter.setChecked(position);

        tvPickPicCount.setText(String.format(getString(R.string.yo_select_pic_count), adapter.getCheckedList().size()));

        if (adapter.isNoneChecked()) {
            tvBtnPreview.setTextColor(getResources().getColor(R.color.yo_text_light));
            tvBtnYes.setTextColor(getResources().getColor(R.color.yo_text_light));
        } else {
            tvBtnPreview.setTextColor(getResources().getColor(R.color.yo_blue));
            tvBtnYes.setTextColor(getResources().getColor(R.color.yo_blue));
        }

        if (adapter.getChecked(position)) {
            lastPosition = position;
        } else {
            lastPosition = -1;
        }
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> checkedPics = adapter.getCheckedList();

        if (checkedPics.size() == 0) {
            Toast.makeText(activity, R.string.yo_tip_select, Toast.LENGTH_LONG).show();
        } else {
            if (v.getId() == R.id.tv_btn_preview) {
                callback.onPreview(checkedPics);
            } else if (v.getId() == R.id.tv_btn_yes) {
                Intent data = new Intent();
                if (!isMultiplePick) {
                    data.putExtra(ImagePickerActivity.EXTRA_SINGLE_PICKER, checkedPics.get(0));
                }
                data.putStringArrayListExtra(ImagePickerActivity.EXTRA_MULTIPLE_PICKER, checkedPics);
                activity.setResult(Activity.RESULT_OK, data);
                activity.finish();
            }
        }
    }
}
