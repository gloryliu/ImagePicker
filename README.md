# ImagePicker
一个仅仅只依赖glide和v4包的图片选择器，支持单选和多选

## 截图
<img src="/screenshot/bukets.png" width="280px"/>
<img src="/screenshot/imgs.png" width="280px"/>
<img src="/screenshot/preview.png" width="280px"/>


###### Demo截图
<img src="/screenshot/demo.png" width="280px"/>

## 如何使用
```java
    // 单选
    startActivityForResult(ImagePickerActivity.getCallingIntent(MainActivity.this, false), REQ_SINGLE_PICKER);
    // 多选
    // 也可以 默认多选 startActivityForResult(new Intent(MainActivity.this,ImagePickerActivity.class),REQ_MULTIPLE_PICKER);
    startActivityForResult(ImagePickerActivity.getCallingIntent(MainActivity.this, true), REQ_MULTIPLE_PICKER);

     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_MULTIPLE_PICKER && resultCode == RESULT_OK) {
            // 获取多选图片的路径数组
            ArrayList<String> imgsPath = data.getStringArrayListExtra(ImagePickerActivity.EXTRA_MULTIPLE_PICKER);

            gridView.setAdapter(new PreviewAdapter(imgsPath));
        } else if (requestCode == REQ_SINGLE_PICKER && resultCode == RESULT_OK) {
           // 获取单选图片的路径
           // 也可以   ArrayList<String> imgsPath = data.getStringArrayListExtra(ImagePickerActivity.EXTRA_MULTIPLE_PICKER);
            String path = data.getStringExtra(ImagePickerActivity.EXTRA_SINGLE_PICKER);

            ArrayList<String> imgsPath = new ArrayList<>();
            imgsPath.add(path);
            gridView.setAdapter(new PreviewAdapter(imgsPath));
        }
    }
```

AndroidManifest.xml文件里
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

<activity android:name="com.yokeyword.imagepicker.ImagePickerActivity"/>
```
## License
``` text
Copyright 2015 YoKeyword

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```