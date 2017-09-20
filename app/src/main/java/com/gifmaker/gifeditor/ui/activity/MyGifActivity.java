package com.gifmaker.gifeditor.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.model.controller.FileCacheHelper;
import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.ui.adatper.ListMyGifAdapter;
import com.gifmaker.gifeditor.utils.FileUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.droidsonroids.gif.GifDrawable;

public class MyGifActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingLayoutMyGif;
    private ImageView buttonBack;
    private RecyclerView listMyGif;
    private ArrayList<String> listPathFileGif;
    private FileCacheHelper mFileCacheHelper;
    private ListMyGifAdapter listMyGifAdapter;

    private LinearLayout detailGif, editGif, shareGif, deleteGif;

    private String pathImageGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_gif);

        initIdView();
        initValueActivity();
        initActionView();
    }

    private void initIdView() {
        slidingLayoutMyGif = (SlidingUpPanelLayout) findViewById(R.id.slidingLayoutMyGif);
        buttonBack = (ImageView) findViewById(R.id.buttonBack);
        listMyGif = (RecyclerView) findViewById(R.id.listMyGif);
        listMyGif.setLayoutManager(new GridLayoutManager(MyGifActivity.this, 2, GridLayoutManager.VERTICAL, false));
        listMyGif.setHasFixedSize(true);

        slidingLayoutMyGif.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        slidingLayoutMyGif.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingLayoutMyGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        detailGif = (LinearLayout) findViewById(R.id.detailGif);
        editGif = (LinearLayout) findViewById(R.id.editGif);
        shareGif = (LinearLayout) findViewById(R.id.shareGif);
        deleteGif = (LinearLayout) findViewById(R.id.deleteGif);

    }

    private void initValueActivity() {
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int size = metrics.widthPixels / 2 - 10;
        mFileCacheHelper = new FileCacheHelper(MyGifActivity.this);
        listPathFileGif = FileUtils.getListFileOfTypeFromDir(mFileCacheHelper.getDirFileOrCreateDir(GifHelper.DIR_OUTPUT_GIF), GifHelper.EXTENTION_GIF);
        listMyGifAdapter = new ListMyGifAdapter(MyGifActivity.this, listPathFileGif);
        listMyGifAdapter.setLayoutParams(size);
        listMyGif.setAdapter(listMyGifAdapter);
    }

    private void initActionView() {
        listMyGifAdapter.setOnItemClickListener(new ListMyGifAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.imageMyGif:
                        startActivity(new Intent(MyGifActivity.this, PreviewGifActivity.class).putExtra("pathImageGif", listPathFileGif.get(position)));
                        break;
                    case R.id.infoGif:
                        slidingLayoutMyGif.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        pathImageGif = listPathFileGif.get(position);
                        break;
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        detailGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayoutMyGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                File imageGif = new File(pathImageGif);
                Date lastModDate = new Date(imageGif.lastModified());
                DateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                String dateCreatGifText = df.format(lastModDate);
                MaterialDialog materialDialog = new MaterialDialog.Builder(MyGifActivity.this)
                        .title(R.string.informationGif)
                        .customView(R.layout.dialog_information_gif, false)
                        .positiveText("OK")
                        .build();
                DecimalFormat format = new DecimalFormat("0.00");
                String gifSize = format.format((double) imageGif.length() / (1024 * 1024));
                TextView nameGif = (TextView) materialDialog.findViewById(R.id.nameGif);
                TextView pathGif = (TextView) materialDialog.findViewById(R.id.pathGif);
                TextView sizeGif = (TextView) materialDialog.findViewById(R.id.sizeGif);
                TextView dateCreatGif = (TextView) materialDialog.findViewById(R.id.dateCreatGif);
                nameGif.setText(" " + imageGif.getName());
                pathGif.setText(" " + imageGif.getAbsolutePath());
                sizeGif.setText(" " + gifSize + " MB");
                dateCreatGif.setText(" " + dateCreatGifText);
                materialDialog.show();
            }
        });
        editGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayoutMyGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                Intent intent = new Intent(MyGifActivity.this, GifEditorActivity.class);
                intent.putExtra("input", "gif");
                intent.putExtra("Uri", pathImageGif);
                try {
                    intent.putExtra("TotalFrame", new GifDrawable(pathImageGif).getNumberOfFrames());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
        shareGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayoutMyGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                File share = new File(pathImageGif);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/gif");
                Uri uri = Uri.fromFile(share);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share Emoji"));
            }
        });
        deleteGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayoutMyGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                final MaterialDialog materialDialog = new MaterialDialog.Builder(MyGifActivity.this)
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
                        listPathFileGif.remove(pathImageGif);
                        listMyGifAdapter.notifyDataSetChanged(listPathFileGif);
                    }
                });
                materialDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listPathFileGif = FileUtils.getListFileOfTypeFromDir(mFileCacheHelper.getDirFileOrCreateDir(GifHelper.DIR_OUTPUT_GIF), GifHelper.EXTENTION_GIF);
        listMyGifAdapter.notifyDataSetChanged(listPathFileGif);
    }
}
