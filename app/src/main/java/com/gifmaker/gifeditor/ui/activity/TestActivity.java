package com.gifmaker.gifeditor.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.view.GPUImageFilterTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static com.gifmaker.gifeditor.ui.view.GPUImageFilterTools.createFilterForType;
import static com.gifmaker.gifeditor.ui.view.GPUImageFilterTools.getListFilter;

public class TestActivity extends AppCompatActivity {

    private GPUImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        thumbnail = (GPUImageView) findViewById(R.id.thumbnail);
        final GPUImageFilterTools.FilterList filterList = getListFilter();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);
        for (int i = 0; i<filterList.filters.size();i++){
            final int finalI = i;
            GPUImage.getBitmapForFilter(bitmap, createFilterForType(filterList.filters.get(i)), new GPUImage.CreatFilterImage() {
                @Override
                public void onSucces(Bitmap bitmap) {
                    File path = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File file = new File(path, filterList.names.get(finalI)+".png");
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}
