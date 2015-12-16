package com.yokeyword.imagepicker.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.yokeyword.imagepicker.R;
import com.yokeyword.imagepicker.adapter.ExploreAdapter;
import com.yokeyword.imagepicker.model.BucketEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 手机上图片浏览界面
 * Created by Yokeyword on 2015/12/14.
 */
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String COLUMN_NAME_COUNT = "v_count";

    private Activity activity;
    private OnSwitchDirCallback callback;
    private GridView gridView;
    private ExploreAdapter adapter;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    public interface OnSwitchDirCallback {
        void onSwitchDir(String bucket_name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        try {
            callback = (OnSwitchDirCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnSwitchDirCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initData() {
        new PicExploreTask().execute();
    }

    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);

        adapter = new ExploreAdapter(activity, ExploreFragment.this);
        gridView.setAdapter(adapter);
    }

    class PicExploreTask extends AsyncTask<Void, Integer, List<BucketEntity>> {

        @Override
        protected List<BucketEntity> doInBackground(Void... params) {
            return getDirectory();
        }

        @Override
        protected void onPostExecute(List<BucketEntity> bucketEntities) {
            super.onPostExecute(bucketEntities);
            adapter.setDatas(bucketEntities);
        }
    }

    private List<BucketEntity> getDirectory() {
        List<BucketEntity> listFiles = new ArrayList<>();

        String[] mediaColumns = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                "COUNT(*) AS " + COLUMN_NAME_COUNT
        };

        // SELECT _data, COUNT(*) AS v_count  FROM video WHERE ( GROUP BY bucket_display_name)
        String selection = " 1=1 ) GROUP BY (" + MediaStore.Images.Media.BUCKET_DISPLAY_NAME;

        Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaColumns, selection, null, null);
        assert cursor != null;
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if(path.endsWith(".gif")){
                continue;
            }

            String bucket_name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            int count = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_COUNT));

            listFiles.add(new BucketEntity(bucket_name, count, path));
        }
        cursor.close();

        return listFiles;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BucketEntity bucketBean = adapter.getItem(position);
        callback.onSwitchDir(bucketBean.getName());
    }
}
