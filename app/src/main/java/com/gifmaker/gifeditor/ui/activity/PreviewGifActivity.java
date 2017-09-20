package com.gifmaker.gifeditor.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.utils.Develop;
import com.gifmaker.gifeditor.utils.FileUtils;
import com.gifmaker.gifeditor.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.gifmaker.gifeditor.R.id.pathGif;

public class PreviewGifActivity extends AppCompatActivity {

    private ImageView buttonBack, buttonInforGif, buttonEditGif, buttonShareGif, buttonDeleteGif,imagePauseMyGif;
    private GifImageView imageMyGif;
    private GifDrawable gifDrawableFromFile;
    private String pathImageGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview_gif);

        initIdView();
        initValueActivity();
        initAtionView();
    }

    private void initIdView() {
        buttonBack = (ImageView) findViewById(R.id.buttonBack);
        buttonInforGif = (ImageView) findViewById(R.id.buttonInforGif);
        buttonEditGif = (ImageView) findViewById(R.id.buttonEditGif);
        buttonShareGif = (ImageView) findViewById(R.id.buttonShareGif);
        buttonDeleteGif = (ImageView) findViewById(R.id.buttonDeleteGif);
        imagePauseMyGif = (ImageView) findViewById(R.id.imagePauseMyGif);
        imageMyGif = (GifImageView) findViewById(R.id.imageMyGif);
    }

    private void initValueActivity() {
        pathImageGif = getIntent().getStringExtra("pathImageGif");
        try {
            gifDrawableFromFile = new GifDrawable(pathImageGif);
            imageMyGif.setImageDrawable(gifDrawableFromFile);
        } catch (IOException e) {
            Toast.makeText(PreviewGifActivity.this, R.string.error_previewGif, Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    private void initAtionView() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonInforGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imageGif = new File(pathImageGif);
                Date lastModDate = new Date(imageGif.lastModified());
                DateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                String dateCreatGifText = df.format(lastModDate);
                MaterialDialog materialDialog = new MaterialDialog.Builder(PreviewGifActivity.this)
                        .title(R.string.informationGif)
                        .customView(R.layout.dialog_information_gif, false)
                        .positiveText("OK")
                        .build();
                DecimalFormat format = new DecimalFormat("0.00");
                String gifSize = format.format(imageGif.length() / (1024 * 1024));
                TextView nameGif = (TextView) materialDialog.findViewById(R.id.nameGif);
                TextView pathGif = (TextView) materialDialog.findViewById(R.id.pathGif);
                TextView sizeGif = (TextView) materialDialog.findViewById(R.id.sizeGif);
                TextView dateCreatGif = (TextView) materialDialog.findViewById(R.id.dateCreatGif);
                nameGif.setText(" " + imageGif.getName());
                pathGif.setText(" " + imageGif.getAbsolutePath());
                sizeGif.setText(" " + gifSize + "MB");
                dateCreatGif.setText(" " + dateCreatGifText);
                materialDialog.show();
            }
        });
        buttonEditGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewGifActivity.this, GifEditorActivity.class);
                intent.putExtra("input", "gif");
                intent.putExtra("Uri", pathImageGif);
                intent.putExtra("TotalFrame", gifDrawableFromFile.getNumberOfFrames());
                startActivity(intent);

            }
        });
        buttonShareGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File share = new File(pathImageGif);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/gif");
                Uri uri = Uri.fromFile(share);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share Emoji"));
            }
        });
        buttonDeleteGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(PreviewGifActivity.this)
                        .title(R.string.warning_delete_file)
                        .content(R.string.do_you_want_to_delete_file)
                        .positiveText("OK")
                        .negativeText(R.string.cancel)
                        .build();
                View posiotionAction = materialDialog.getActionButton(DialogAction.POSITIVE);
                posiotionAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileUtils.deleteFile(pathImageGif);
                        materialDialog.dismiss();
                        finish();
                    }
                });
                materialDialog.show();
            }
        });

        imageMyGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gifDrawableFromFile.isPlaying()){
                    gifDrawableFromFile.pause();
                    imagePauseMyGif.setVisibility(View.VISIBLE);
                }else{
                    gifDrawableFromFile.start();
                    imagePauseMyGif.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gifDrawableFromFile.recycle();
    }
}
