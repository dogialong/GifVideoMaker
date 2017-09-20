package com.gifmaker.gifeditor.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gifmaker.gifeditor.R;

import pl.droidsonroids.gif.GifGPUImageView;

public class SaveActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnShare, btnSave;
    private GifGPUImageView gifGPUImageView;
    private ImageView imgBack, imgGifPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        initIdView();
        initListener();
    }
    private void initIdView () {
        btnShare = (Button) findViewById(R.id.btnShare);
        btnSave = (Button) findViewById(R.id.btnSave);
        gifGPUImageView  = (GifGPUImageView) findViewById(R.id.imageViewPreviewGif);
        imgBack = (ImageView) findViewById(R.id.back);
        imgGifPlay = (ImageView) findViewById(R.id.imgGifPlay);

    }
    private void initListener() {
        btnShare.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        gifGPUImageView.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgGifPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShare:
                break;
            case R.id.btnSave:
                break;
            case R.id.imageViewPreviewGif:
                break;
            case R.id.back:
                break;
            case R.id.imgGifPlay:
                break;
            default:break;
        }
    }
}
