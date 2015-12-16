package com.yokeyword.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yokeyword.imagepicker.ImagePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQ_MULTIPLE_PICKER = 100;
    private static final int REQ_SINGLE_PICKER = 200;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_pick_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ImagePickerActivity.getCallingIntent(MainActivity.this, false), REQ_SINGLE_PICKER);
            }
        });
        findViewById(R.id.btn_pick_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 也可以 默认多选
//                startActivityForResult(new Intent(MainActivity.this,ImagePickerActivity.class),REQ_MULTIPLE_PICKER);
                startActivityForResult(ImagePickerActivity.getCallingIntent(MainActivity.this, true), REQ_MULTIPLE_PICKER);
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_MULTIPLE_PICKER && resultCode == RESULT_OK) {
            ArrayList<String> imgsPath = data.getStringArrayListExtra(ImagePickerActivity.EXTRA_MULTIPLE_PICKER);

            gridView.setAdapter(new PreviewAdapter(imgsPath));
        } else if (requestCode == REQ_SINGLE_PICKER && resultCode == RESULT_OK) {
            // 也可以
//            ArrayList<String> imgsPath = data.getStringArrayListExtra(ImagePickerActivity.EXTRA_MULTIPLE_PICKER);
            String path = data.getStringExtra(ImagePickerActivity.EXTRA_SINGLE_PICKER);

            ArrayList<String> imgsPath = new ArrayList<>();
            imgsPath.add(path);
            gridView.setAdapter(new PreviewAdapter(imgsPath));
        }
    }

    class PreviewAdapter extends BaseAdapter {
        private List<String> items;
        private ViewHolder holder;
        private int width, height;

        public PreviewAdapter(List<String> items) {
            this.items = items;

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            width = (dm.widthPixels - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3 * 4, getResources().getDisplayMetrics())) / 4;
            height = width;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_img, parent, false);
                GridView.LayoutParams params = new GridView.LayoutParams(width, height);
                convertView.setLayoutParams(params);

                holder = new ViewHolder();
                holder.img = (ImageView) convertView.findViewById(R.id.img);

                convertView.setTag(R.id.img, holder);
            } else {
                holder = (ViewHolder) convertView.getTag(R.id.img);
            }

            Glide.with(MainActivity.this)
                    .load(new File(items.get(position)))
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.drawable.bg_grey)
                    .into(holder.img);

            return convertView;
        }

        class ViewHolder {
            ImageView img;
        }
    }
}
