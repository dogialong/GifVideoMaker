package com.gifmaker.gifeditor.ui.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.model.ItemFontText;
import com.gifmaker.gifeditor.model.controller.AddFrameHelper;
import com.gifmaker.gifeditor.model.controller.GifHelper;
import com.gifmaker.gifeditor.model.controller.GifSaveInputHelper;
import com.gifmaker.gifeditor.presenter.GifPresenter;
import com.gifmaker.gifeditor.ui.adatper.FontAdapter;
import com.gifmaker.gifeditor.ui.adatper.ViewPagerColorAdapter;
import com.gifmaker.gifeditor.ui.fragment.GifEditorAddFrameFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorAddImageFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorAddStickerFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorAddTextFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorBrightnessFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorCropImageFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorEffectGifFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorManagerImageFragment;
import com.gifmaker.gifeditor.ui.fragment.GifEditorSpeedGifManager;
import com.gifmaker.gifeditor.ui.method.ListFontText;
import com.gifmaker.gifeditor.ui.view.GifEditorAddFrameItemCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorAddStickerItemCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorBrightnessImageCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorColorSelectCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorEffectImageCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorManagerImageCallBack;
import com.gifmaker.gifeditor.ui.view.GifEditorSpeedGifCallBack;
import com.gifmaker.gifeditor.ui.view.GifExtractFrameFromVideoViewUser;
import com.gifmaker.gifeditor.ui.view.GifMakeViewUser;
import com.gifmaker.gifeditor.utils.BitMapUtils;
import com.gifmaker.gifeditor.utils.Develop;
import com.gifmaker.gifeditor.utils.FileUtils;
import com.gifmaker.gifeditor.utils.Logger;
import com.gifmaker.gifeditor.utils.StringUtils;
import com.rey.material.widget.LinearLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.stickerview.BitmapStickerIcon;
import com.stickerview.DeleteIconEvent;
import com.stickerview.DrawableSticker;
import com.stickerview.EditTextAddIconEvent;
import com.stickerview.FlipHorizontallyEvent;
import com.stickerview.RotateIconEvent;
import com.stickerview.Sticker;
import com.stickerview.StickerView;
import com.stickerview.TextSticker;
import com.stickerview.ZoomIconEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifGPUImageView;
import pl.droidsonroids.gif.GifImageView;


public class GifEditorActivity extends AppCompatActivity implements GifEditorManagerImageCallBack, GifEditorEffectImageCallBack,
        GifEditorBrightnessImageCallBack, GifEditorAddFrameItemCallBack, GifEditorSpeedGifCallBack, GifEditorAddImageFragment.sendData, GifEditorAddStickerItemCallBack, StickerView.OnStickerOperationListener {
    private ImageView buttonChangeFontEditTextToGif;
    private ImageView buttonChangeColorEditTextToGif;
    private ImageView buttonChangeEditTextToGif;
    private RelativeLayout relativeRecycleView;
    private View addTextPopupWindowRootView;
    private FontAdapter fontAdapter;
    private ArrayList<ItemFontText> itemFontTexts;
    public ArrayList<Image> listDataImages;
    private String typeInputData;
    private String selectedVideoUriString;
    private int startVideo, endVideo;
    private RelativeLayout relativeLayoutRootview;
    private int heightKeyboard = 0;
    private int indexBitmapFrameImage = 0;
    private int indexListFrameImage = 0;
    private int widthImageView = 0, heightImageView = 0;
    private InputMethodManager imm;
    private Boolean chekKeyboard = false;
    private ImageView imgBackWardOff, imgForwardOff;
    private ImageView saveImageGif, previewImageGif, back, buttonManagerImage, buttonSpeedGif, buttonBrightnessImage,
            buttonCropImageGif, buttonEffectGif, buttonAddStickerToGif, buttonAddTextToGif, buttonAddFrameToGif, buttonAddImageToGif, backEdit, imgGifPlay,imgFacebook;
    private GifEditorManagerImageFragment managerImageFragment;
    private GifEditorEffectGifFragment editorEffectGifFragment;
    private GifEditorBrightnessFragment brightnessFragment;
    private GifEditorAddFrameFragment addFrameFragment;
    private GifEditorSpeedGifManager speedGifManager;
    private GifEditorAddStickerFragment addStickerFragment;
    private HorizontalScrollView scrollView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private EditText addTextEditText;

    private GifPresenter mGifPresenter;
    private GifHelper mGifHelper;

    private float brightnessImage;
    private GPUImageFilter effectCurrentImage;

    private SlidingUpPanelLayout slidingLayoutEditGif;
    private com.rey.material.widget.FrameLayout frameWrapper;
    private MaterialDialog progressDialog;
    private String fileNameAllImagePath;
    private float fpsGif = 1.0f;

    private String textChangeAddToGif = "";

    private int percentExtractAllFrame = 0;
    public int widthDefaultOfFrame = 0;
    private int heightDefaultOFrame = 0;
    private boolean isHidingListManager = false;
    private boolean isReverse = false;

    private boolean isPlayGif = true;

    private GifGPUImageView imageViewPreviewGif;
    private GifImageView imageViewPreviewSuccesGif;
    private GifDrawable gifDrawableSuccesGif;
    private String pathImageGifEditor;
    private String pathImageGifEditorSucces;

    private Sticker stickerChangetext = null;
    private StickerView allItemBitMapAddToGif;
    private TextSticker textToadd;
    private DrawableSticker stickeradd;
    private BitmapStickerIcon deleteIcon, editText, zoomIcon, flipIcon, rotateIcon;
    private List<View> addViewsFrame;
    private View imageRootView;
    private Button btnSave;
    public ViewPager addTextColorPickerRecyclerView;

    private Bitmap bitmapAllAddToGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_image);
        initIdView();
        initValue();
        initActionView();
        initIconView();
    }

    private void initValueFragment() {
        allItemBitMapAddToGif.setLocked(false);
        allItemBitMapAddToGif.setConstrained(true);
        allItemBitMapAddToGif.setDrawingCacheEnabled(true);
        allItemBitMapAddToGif.buildDrawingCache();
        managerImageFragment = new GifEditorManagerImageFragment().newInsFragmentWorkout(listDataImages);
        managerImageFragment.initManagerCallBack(this);

        brightnessFragment = new GifEditorBrightnessFragment();
        brightnessFragment.initBrightnessImageCallBack(this);
        editorEffectGifFragment = new GifEditorEffectGifFragment();
        editorEffectGifFragment.initEffectImageCallBack(this);
        speedGifManager = new GifEditorSpeedGifManager();
        speedGifManager.initSpeedImageCallBack(this);

        widthDefaultOfFrame = frameWrapper.getMeasuredWidth();
        heightDefaultOFrame = frameWrapper.getMeasuredHeight();
        goToFragment(managerImageFragment);

        widthImageView = allItemBitMapAddToGif.getWidth();
        heightImageView = allItemBitMapAddToGif.getHeight();

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void initValue() {
        addViewsFrame = new ArrayList<>();
        listDataImages = new ArrayList<>();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        itemFontTexts = ListFontText.getListItemFontText(GifEditorActivity.this);
        typeInputData = getIntent().getStringExtra("input");
        if (typeInputData.equals("video")) {
            inputFromVideo();
        } else if (typeInputData.equals("gif")) {
            inputFromGif();

        } else if (typeInputData.equals("photo")) {
            inputFromPhoto();
        }
        brightnessImage = 0;

        setEnableForwardOff(true);
        setEnableBackWardOff(false);
    }

    private void inputFromPhoto() {
        listDataImages = getIntent().getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
        mGifHelper = new GifHelper(getApplicationContext());
        mGifPresenter = new GifPresenter(getApplicationContext(), mGifHelper);
        progressDialog = creatProgressDialog(R.string.progress_image);
        progressDialog.setProgress(0);
        progressDialog.show();
        fileNameAllImagePath = mGifPresenter.copyImageSelectedToInput(listDataImages, new GifHelper.CopyImageToOutput() {
            @Override
            public void onSuccess(int positionCopy) {
                progressDialog.setProgress((positionCopy * 100) / listDataImages.size());
                if (positionCopy == listDataImages.size() - 1) {
                    String[] s = fileNameAllImagePath.split("/");
                    String name = s[s.length - 1];
                    mGifPresenter.makeGif(fileNameAllImagePath, name, fpsGif, "jpeg", new GifMakeViewUser() {
                        @Override
                        public void showLoading() {

                        }

                        @Override
                        public void showProgres(int percent) {

                        }

                        @Override
                        public void hidLoading() {

                        }

                        @Override
                        public void showError(String error) {

                        }

                        @Override
                        public void showSuccess(String msg) {
                            if (!imageViewPreviewGif.isPlaying()) {
                                progressDialog.dismiss();
                                pathImageGifEditor = msg;
                                imageViewPreviewGif.setImageFromePathFile(pathImageGifEditor);
                                initValueFragment();
                            }
                        }
                    });
                }
            }
        });
    }

    private void inputFromVideo() {
        progressDialog = creatProgressDialog(R.string.progress_video);
        progressDialog.setProgress(0);
        progressDialog.show();
        selectedVideoUriString = getIntent().getStringExtra("Uri");
        startVideo = getIntent().getIntExtra("startVideo", 0);
        endVideo = getIntent().getIntExtra("endVideo", 0);
        int duration = endVideo - startVideo;
        mGifHelper = new GifHelper(getApplicationContext(), selectedVideoUriString);
        mGifPresenter = new GifPresenter(getApplicationContext(), mGifHelper, new GifExtractFrameFromVideoViewUser() {
            @Override
            public void showLoading() {
            }

            @Override
            public void showProgres(int percent) {
                progressDialog.setProgress(percent * 90 / 20);
                percentExtractAllFrame = percent;
            }

            @Override
            public void hidLoading() {
            }

            @Override
            public void showError(String error) {
            }

            @Override
            public void showSuccess(String msg) {
                if (percentExtractAllFrame == 19) {
                    if (StringUtils.stringIsNotEmpty(msg)) {
                        ArrayList<String> arrLinkPath = mGifPresenter.getPathFromVideo(msg, startVideo);
                        Logger.e(Develop.TAG, "arrLinkPath.size()" + arrLinkPath.size());
                        listDataImages = mGifPresenter.getListImage(arrLinkPath);
                        Logger.e(Develop.TAG, "listDataImages.size()" + listDataImages.size());
                        fileNameAllImagePath = msg;
                    }

                    String[] s = fileNameAllImagePath.split("/");
                    String name = s[s.length - 1];
                    mGifPresenter.makeGif(fileNameAllImagePath, name, fpsGif, "png", new GifMakeViewUser() {
                        @Override
                        public void showLoading() {

                        }

                        @Override
                        public void showProgres(int percent) {

                        }

                        @Override
                        public void hidLoading() {

                        }

                        @Override
                        public void showError(String error) {

                        }

                        @Override
                        public void showSuccess(String msg) {
                            if (!imageViewPreviewGif.isPlaying()) {
                                progressDialog.dismiss();
                                pathImageGifEditor = msg;
                                imageViewPreviewGif.setImageFromePathFile(pathImageGifEditor);
                                initValueFragment();
                            }
                        }
                    });
                }
            }
        });
        mGifPresenter.extractAllFrameFromVideo(startVideo, duration);
    }

    private void inputFromGif() {
        final int totalFrame = getIntent().getIntExtra("TotalFrame", 20);
        progressDialog = creatProgressDialog(R.string.progress_gif);
        progressDialog.setProgress(0);
        progressDialog.show();
        selectedVideoUriString = getIntent().getStringExtra("Uri");
        pathImageGifEditor = selectedVideoUriString;
        mGifHelper = new GifHelper(getApplicationContext(), selectedVideoUriString, totalFrame);
        mGifPresenter = new GifPresenter(getApplicationContext(), mGifHelper, new GifExtractFrameFromVideoViewUser() {
            @Override
            public void showLoading() {
            }

            @Override
            public void showProgres(int percent) {
                progressDialog.setProgress(percent * 100 / totalFrame);
            }

            @Override
            public void hidLoading() {
            }

            @Override
            public void showError(String error) {
                Toast.makeText(getApplicationContext(), "Extra frame error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showSuccess(String msg) {

                if (!imageViewPreviewGif.isPlaying()) {
                    progressDialog.dismiss();
                    if (StringUtils.stringIsNotEmpty(msg)) {
                        ArrayList<String> arrLinkPath = mGifPresenter.getPathFromGif(msg, totalFrame);
                        listDataImages = mGifPresenter.getListImage(arrLinkPath);
                        fileNameAllImagePath = msg;
                        imageViewPreviewGif.setImageFromePathFile(pathImageGifEditor);
                    }
                    initValueFragment();
                }
            }
        });
        mGifPresenter.extractAllFrameFromGif();
    }


    private void initIdView() {
        frameWrapper = (com.rey.material.widget.FrameLayout) findViewById(R.id.customImageToGif);
        saveImageGif = (ImageView) findViewById(R.id.saveImageGif);
        previewImageGif = (ImageView) findViewById(R.id.previewImage);
        buttonManagerImage = (ImageView) findViewById(R.id.buttonManagerImage);
        buttonSpeedGif = (ImageView) findViewById(R.id.buttonSpeedGif);
        buttonBrightnessImage = (ImageView) findViewById(R.id.buttonBrightnessImage);
        buttonCropImageGif = (ImageView) findViewById(R.id.buttonCropImageGif);
        buttonEffectGif = (ImageView) findViewById(R.id.buttonEffectGif);
        buttonAddStickerToGif = (ImageView) findViewById(R.id.buttonAddStickerToGif);
        buttonAddTextToGif = (ImageView) findViewById(R.id.buttonAddTextToGif);
        buttonAddFrameToGif = (ImageView) findViewById(R.id.buttonAddFrameToGif);
        buttonAddImageToGif = (ImageView) findViewById(R.id.buttonAddImageToGif);
        back = (ImageView) findViewById(R.id.back);
        backEdit = (ImageView) findViewById(R.id.backEdit);
        btnSave = (Button) findViewById(R.id.btnSave);
        imgGifPlay = (ImageView) findViewById(R.id.imgGifPlay);
        imgBackWardOff = (ImageView) findViewById(R.id.imgBackWardOff);
        imgForwardOff = (ImageView) findViewById(R.id.imgForwardOff);
        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        slidingLayoutEditGif = (SlidingUpPanelLayout) findViewById(R.id.slidingLayoutEditGif);
        relativeLayoutRootview = (RelativeLayout) findViewById(R.id.relative_rootview);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        allItemBitMapAddToGif = (StickerView) findViewById(R.id.parentImageStickerView);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayoutEditGif);
        slidingLayoutEditGif.setTouchEnabled(false);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        imageViewPreviewGif = (GifGPUImageView) findViewById(R.id.imageViewPreviewGif);
        imageViewPreviewSuccesGif = (GifImageView) findViewById(R.id.imageViewPreviewSuccesGif);
    }

    private void initActionView() {
        saveImageGif.setOnClickListener(onClickListener);
        previewImageGif.setOnClickListener(onClickListener);
        imgBackWardOff.setOnClickListener(onClickListener);
        imgForwardOff.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        backEdit.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        imgFacebook.setOnClickListener(onClickListener);
        buttonManagerImage.setOnClickListener(onClickListener);
        buttonSpeedGif.setOnClickListener(onClickListener);
        buttonBrightnessImage.setOnClickListener(onClickListener);
        buttonCropImageGif.setOnClickListener(onClickListener);
        buttonEffectGif.setOnClickListener(onClickListener);
        buttonAddStickerToGif.setOnClickListener(onClickListener);
        buttonAddTextToGif.setOnClickListener(onClickListener);
        buttonAddFrameToGif.setOnClickListener(onClickListener);
        buttonAddImageToGif.setOnClickListener(onClickListener);
        imageViewPreviewSuccesGif.setOnClickListener(onClickListener);
        allItemBitMapAddToGif.setOnStickerOperationListener(this);
    }

    public void drawViewFragment() {
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fragment_slide_up);
        animation.setStartOffset(0);
        RelativeLayout.LayoutParams newViewParams = new RelativeLayout.LayoutParams(getScreenWidth(), getScreenHeight());

        View view = (View) findViewById(R.id.customImageToGif);
        view.setLayoutParams(newViewParams);
        view.startAnimation(animation);
        scrollView.setVisibility(View.GONE);
        animation = null;
    }

    public void drawViewFragmentDown() {
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fragment_slide_down);
        animation.setStartOffset(0);
        RelativeLayout.LayoutParams newViewParams = new RelativeLayout.LayoutParams(widthDefaultOfFrame, heightDefaultOFrame);
        View view = (View) findViewById(R.id.customImageToGif);
        view.startAnimation(animation);
        view.setLayoutParams(newViewParams);
        scrollView.setVisibility(View.VISIBLE);
        animation = null;
    }

    public void drawViewFragmentDown(int width, int height) {
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fragment_slide_down);
        animation.setStartOffset(0);
        RelativeLayout.LayoutParams newViewParams = new RelativeLayout.LayoutParams(width, height);
        View view = (View) findViewById(R.id.customImageToGif);
        view.startAnimation(animation);
        view.setLayoutParams(newViewParams);
        scrollView.setVisibility(View.VISIBLE);
        animation = null;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.saveImageGif:
                    saveImageGifClick();
                    break;
                case R.id.previewImage:

                    if (imageViewPreviewGif.isPlaying()) {
                        imageViewPreviewGif.pause();
                        previewImageGif.setBackgroundResource(R.drawable.ic_pause);
                    } else {
                        imageViewPreviewGif.resume();
                        previewImageGif.setBackgroundResource(R.drawable.ic_play);
                    }
                    break;
                case R.id.imgBackWardOff:
                    if (!isReverse) {
                        setEnableBackWardOff(true);
                        setEnableForwardOff(false);
                        Collections.reverse(listDataImages);
                        isReverse = true;
                    }
                    break;
                case  R.id.imgFacebook:
                    shareGif(pathImageGifEditorSucces);
                    break;
                case R.id.imgForwardOff:
                    if (isReverse) {
                        setEnableForwardOff(true);
                        setEnableBackWardOff(false);
                        Collections.reverse(listDataImages);
                        isReverse = false;
                    }
                    break;
                case R.id.imageViewPreviewSuccesGif:
                    if (gifDrawableSuccesGif.isPlaying()) {
                        gifDrawableSuccesGif.pause();
                        imgGifPlay.setVisibility(View.VISIBLE);
                    } else {
                        gifDrawableSuccesGif.start();
                        imgGifPlay.setVisibility(View.GONE);
                    }
                    break;
                case R.id.back:
                    final MaterialDialog materialDialog = new MaterialDialog.Builder(GifEditorActivity.this)
                            .title(R.string.title_discard_edit)
                            .content(R.string.warning_discard_edit)
                            .positiveText(R.string.discard)
                            .negativeText(R.string.keep)
                            .build();
                    View positiveAction = materialDialog.getActionButton(DialogAction.POSITIVE);
                    positiveAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialDialog.dismiss();
                            destroyActivity();
                        }
                    });
                    materialDialog.show();
                    break;
                case R.id.backEdit:
                    slidingLayoutEditGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    imageViewPreviewSuccesGif.setImageDrawable(null);
                    imageViewPreviewSuccesGif.refreshDrawableState();
                    if (gifDrawableSuccesGif != null)
                        gifDrawableSuccesGif.pause();
                    break;
                case R.id.btnSave:
                    File fileTempGif = new File(pathImageGifEditorSucces);
                    File oututGif = new File(mGifHelper.getDirOutputImageGif().getAbsolutePath(), fileTempGif.getName());
                    try {
                        FileUtils.copyFile(fileTempGif, oututGif);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pushNotification(pathImageGifEditorSucces);
                    destroyActivity();
                    break;
                case R.id.buttonManagerImage:
                    if (isHidingListManager) {
                        managerImageFragment.showManagerImage();
                    } else {
                        goToFragment(managerImageFragment);
                    }
                    break;
                case R.id.buttonSpeedGif:
                    isHidingListManager = false;
                    goToFragment(speedGifManager);
                    break;
                case R.id.buttonBrightnessImage:
                    isHidingListManager = false;
                    goToFragment(brightnessFragment);
                    break;
                case R.id.buttonCropImageGif:
                    isHidingListManager = false;
                    goToFragment(new GifEditorCropImageFragment());
                    break;
                case R.id.buttonEffectGif:
                    isHidingListManager = false;
                    goToFragment(editorEffectGifFragment);
                    break;
                case R.id.buttonAddStickerToGif:
                    isHidingListManager = false;
                    addStickerFragment = new GifEditorAddStickerFragment();
                    addStickerFragment.initAddStickerCallBack(GifEditorActivity.this);
                    goToFragment(addStickerFragment);
                    break;
                case R.id.buttonAddTextToGif:
                    isHidingListManager = false;
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    openAddTextPopupWindow("", -1, null);

                    break;
                case R.id.buttonAddFrameToGif:
                    isHidingListManager = false;
                    addFrameFragment = new GifEditorAddFrameFragment();
                    addFrameFragment.initAddFrameImageCallBack(GifEditorActivity.this);
                    goToFragment(addFrameFragment);
                    break;
                case R.id.buttonAddImageToGif:
                    isHidingListManager = false;
                    goToFragment(new GifEditorAddImageFragment());
                    break;
            }
        }
    };

    private void saveImageGifClick() {
        allItemBitMapAddToGif.setShowBolder(false);
        allItemBitMapAddToGif.invalidate();
        progressDialog = creatProgressDialog(R.string.creat_gif);
        progressDialog.setProgress(0);
        progressDialog.show();
        progressImageInput();
    }

    private void progressImageInput() {
        bitmapAllAddToGif = allItemBitMapAddToGif.getDrawingCache();
        imageViewPreviewGif.pause();
        GifHelper.CopyImageToOutput mCopyImageToOutput = new GifHelper.CopyImageToOutput() {
            @Override
            public void onSuccess(int positionCopy) {
                {
                    progressDialog.setProgress(((positionCopy + 1) * 90) / listDataImages.size());
                    if (positionCopy == (listDataImages.size() - 1)) {
                        String[] s = fileNameAllImagePath.split("/");
                        String name = s[s.length - 1];
                        mGifPresenter.makeGif(fileNameAllImagePath, name.hashCode() + "", fpsGif, "jpeg", new GifMakeViewUser() {
                            @Override
                            public void showLoading() {

                            }

                            @Override
                            public void showProgres(int percent) {

                            }

                            @Override
                            public void hidLoading() {

                            }

                            @Override
                            public void showError(String error) {

                            }

                            @Override
                            public void showSuccess(String msg) {
                                progressDialog.dismiss();
                                slidingLayoutEditGif.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                pathImageGifEditorSucces = msg;
                                try {
                                    gifDrawableSuccesGif = new GifDrawable(pathImageGifEditorSucces);
                                    imageViewPreviewSuccesGif.setImageDrawable(gifDrawableSuccesGif);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        };

        GifSaveInputHelper gifSaveInputHelper = new GifSaveInputHelper(GifEditorActivity.this, bitmapAllAddToGif, indexListFrameImage, indexBitmapFrameImage, widthImageView, heightImageView, mCopyImageToOutput);

        GPUImageFilter gpuImageFilter = imageViewPreviewGif.getFilter();
        for (int indexImageInGif = 0; indexImageInGif < listDataImages.size(); indexImageInGif++) {
            String pathInputImage = listDataImages.get(indexImageInGif).getPath();
            String pathOutputImage = fileNameAllImagePath + String.format("%04d", indexImageInGif) + ".jpeg";

            if (effectCurrentImage == null && brightnessImage == 0) {
                gifSaveInputHelper.saveImageNoEffect(pathInputImage, indexImageInGif, pathOutputImage);
            } else {
                gifSaveInputHelper.saveImageWithEffect(gpuImageFilter, pathInputImage, indexImageInGif, pathOutputImage);
            }
        }
    }


    public void initGifAction(String nameFragment) {
        buttonManagerImage.setImageResource(R.drawable.ic_editor_off);
        buttonSpeedGif.setImageResource(R.drawable.ic_speed_off);
        buttonBrightnessImage.setImageResource(R.drawable.ic_brightness_off);
        buttonCropImageGif.setImageResource(R.drawable.ic_crop_off);
        buttonEffectGif.setImageResource(R.drawable.ic_fillter_off);
        buttonAddStickerToGif.setImageResource(R.drawable.ic_sticker_off);
        buttonAddTextToGif.setImageResource(R.drawable.ic_text_off);
        buttonAddFrameToGif.setImageResource(R.drawable.ic_frame_off);
        buttonAddImageToGif.setImageResource(R.drawable.ic_manage_image_off);

        if (nameFragment.equals(GifEditorManagerImageFragment.class.toString())) {
            buttonManagerImage.setImageResource(R.drawable.ic_editor_on);
        } else if (nameFragment.equals(GifEditorSpeedGifManager.class.toString())) {
            buttonSpeedGif.setImageResource(R.drawable.ic_speed_on);
        } else if (nameFragment.equals(GifEditorBrightnessFragment.class.toString())) {
            buttonBrightnessImage.setImageResource(R.drawable.ic_brightness_on);
        } else if (nameFragment.equals(GifEditorCropImageFragment.class.toString())) {
            buttonCropImageGif.setImageResource(R.drawable.ic_crop_on);
        } else if (nameFragment.equals(GifEditorEffectGifFragment.class.toString())) {
            buttonEffectGif.setImageResource(R.drawable.ic_fillter_on);
        } else if (nameFragment.equals(GifEditorAddStickerFragment.class.toString())) {
            buttonAddStickerToGif.setImageResource(R.drawable.ic_sticker_on);
        } else if (nameFragment.equals(GifEditorAddTextFragment.class.toString())) {
            buttonAddTextToGif.setImageResource(R.drawable.ic_text_on);
        } else if (nameFragment.equals(GifEditorAddFrameFragment.class.toString())) {
            buttonAddFrameToGif.setImageResource(R.drawable.ic_frame_on);
        } else if (nameFragment.equals(GifEditorAddImageFragment.class.toString())) {
            buttonAddImageToGif.setImageResource(R.drawable.ic_manage_image_on);
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingLayoutEditGif != null &&
                (slidingLayoutEditGif.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingLayoutEditGif.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingLayoutEditGif.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            final MaterialDialog materialDialog = new MaterialDialog.Builder(GifEditorActivity.this)
                    .title(R.string.title_discard_edit)
                    .content(R.string.warning_discard_edit)
                    .positiveText(R.string.discard)
                    .negativeText(R.string.keep)
                    .build();
            View positiveAction = materialDialog.getActionButton(DialogAction.POSITIVE);
            positiveAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();

                }
            });
            materialDialog.show();
        }
    }

    private void destroyActivity() {
        imageViewPreviewGif.recycle();
        if (gifDrawableSuccesGif != null)
            gifDrawableSuccesGif.recycle();
        FileUtils.deleteFileInFolder(mGifHelper.getDirInputImageGif().getAbsolutePath());
        FileUtils.deleteFileInFolder(mGifHelper.getDirOutputTempImageGif().getAbsolutePath());
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goToFragment(Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit, R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        ft.replace(R.id.customImageToGif, fragment);
        ft.commit();
    }

    private void openAddTextPopupWindow(String text, final int colorCode, Typeface typeface2) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
        addTextEditText = (EditText) addTextPopupWindowRootView.findViewById(R.id.add_text_edit_text);
        addTextEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("onEditorAction", "onEditorAction: ");
                return false;
            }
        });
        android.widget.ImageView buttonApplyEditTextToGif = (android.widget.ImageView) addTextPopupWindowRootView.findViewById(R.id.buttonApplyEditTextToGif);
        android.widget.ImageView buttonEditTextToGif = (android.widget.ImageView) addTextPopupWindowRootView.findViewById(R.id.buttonEditTextToGif);
        buttonChangeFontEditTextToGif = (android.widget.ImageView) addTextPopupWindowRootView.findViewById(R.id.buttonChangeFontEditTextToGif);
        buttonChangeColorEditTextToGif = (android.widget.ImageView) addTextPopupWindowRootView.findViewById(R.id.buttonChangeColorEditTextToGif);
        buttonChangeEditTextToGif = (android.widget.ImageView) addTextPopupWindowRootView.findViewById(R.id.buttonChangeEditTextToGif);
        buttonChangeEditTextToGif.setImageResource(R.drawable.ic_edittext_on);
        buttonChangeEditTextToGif.setEnabled(false);
        relativeRecycleView = (RelativeLayout) addTextPopupWindowRootView.findViewById(R.id.relative_recycle);
        addTextColorPickerRecyclerView = (ViewPager) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);


        RecyclerView.LayoutManager manager = new LinearLayoutManager(GifEditorActivity.this);
        final RecyclerView addTextFontPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_font_picker_recycler_view);
        addTextFontPickerRecyclerView.setLayoutManager(manager);
        addTextFontPickerRecyclerView.setHasFixedSize(true);
        final ArrayList<Typeface> listFont = ListFontText.getListTypeFace(GifEditorActivity.this);
        Log.e("linh", listFont.size() + "");
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightKeyboard);
        relativeRecycleView.setLayoutParams(layoutParams1);
        relativeLayoutRootview.addView(addTextPopupWindowRootView);

        final GifEditorColorSelectCallBack gifEditorColorSelectCallBack = new GifEditorColorSelectCallBack() {
            @Override
            public void color(int colorCode) {
                addTextEditText.setTextColor(colorCode);
            }
        };

        fontAdapter = new FontAdapter(GifEditorActivity.this, itemFontTexts, listFont, new FontAdapter.IListClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                addTextEditText.setTypeface(listFont.get(position));

            }
        });


        if (heightKeyboard != 0) {
            final ViewPagerColorAdapter viewPagerColorAdapter = new ViewPagerColorAdapter(getSupportFragmentManager(), heightKeyboard, colorCode, gifEditorColorSelectCallBack);
            addTextColorPickerRecyclerView.setAdapter(viewPagerColorAdapter);
        }
        addTextFontPickerRecyclerView.setAdapter(fontAdapter);

        if (StringUtils.stringIsNotEmpty(text)) {
            addTextEditText.setText(text);
            addTextEditText.setTextColor(colorCode == -1 ? getResources().getColor(R.color.white) : colorCode);
            addTextEditText.setTypeface(typeface2 == null ? listFont.get(0) : typeface2);
        }

        slidingLayoutEditGif.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                slidingLayoutEditGif.getWindowVisibleDisplayFrame(r);
                int screenHeight = slidingLayoutEditGif.getRootView().getHeight();
                if (heightKeyboard == 0) {
                    heightKeyboard = screenHeight - (r.bottom);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightKeyboard);
                    relativeRecycleView.setLayoutParams(layoutParams);
                    if (addTextPopupWindowRootView.getParent() != null) {
                        ((ViewGroup) addTextPopupWindowRootView.getParent()).removeView(addTextPopupWindowRootView);
                    }
                    if (heightKeyboard != 0) {
                        final ViewPagerColorAdapter viewPagerColorAdapter = new ViewPagerColorAdapter(getSupportFragmentManager(), heightKeyboard, colorCode, gifEditorColorSelectCallBack);
                        addTextColorPickerRecyclerView.setAdapter(viewPagerColorAdapter);
                    }
                    relativeLayoutRootview.addView(addTextPopupWindowRootView);
                    addTextEditText.requestFocus();

                }
            }
        });
        if (heightKeyboard > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightKeyboard);
            relativeRecycleView.setLayoutParams(layoutParams);
            if (addTextPopupWindowRootView.getParent() != null) {
                ((ViewGroup) addTextPopupWindowRootView.getParent()).removeView(addTextPopupWindowRootView);
            }
            relativeLayoutRootview.addView(addTextPopupWindowRootView);
            addTextEditText.requestFocus();
        }

        buttonChangeColorEditTextToGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangeColorEditTextToGif.setImageResource(R.drawable.ic_color_on);
                buttonChangeFontEditTextToGif.setImageResource(R.drawable.ic_font_off);
                buttonChangeEditTextToGif.setImageResource(R.drawable.ic_edittext_off);
                buttonChangeEditTextToGif.setEnabled(true);
                addTextColorPickerRecyclerView.setVisibility(View.VISIBLE);
                addTextFontPickerRecyclerView.setVisibility(View.GONE);
                chekKeyboard = false;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
        buttonChangeFontEditTextToGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangeFontEditTextToGif.setImageResource(R.drawable.ic_font_on);
                buttonChangeEditTextToGif.setImageResource(R.drawable.ic_edittext_off);
                buttonChangeColorEditTextToGif.setImageResource(R.drawable.ic_color_off);
                buttonChangeEditTextToGif.setEnabled(true);
                addTextColorPickerRecyclerView.setVisibility(View.GONE);
                addTextFontPickerRecyclerView.setVisibility(View.VISIBLE);
                chekKeyboard = false;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        buttonChangeEditTextToGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangeEditTextToGif.setImageResource(R.drawable.ic_edittext_on);
                buttonChangeFontEditTextToGif.setImageResource(R.drawable.ic_font_off);
                buttonChangeColorEditTextToGif.setImageResource(R.drawable.ic_color_off);
                addTextColorPickerRecyclerView.setVisibility(View.GONE);
                addTextFontPickerRecyclerView.setVisibility(View.GONE);
                if (chekKeyboard == false) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    chekKeyboard = true;
                }
            }
        });

        buttonApplyEditTextToGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chekKeyboard = false;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                relativeLayoutRootview.removeView(addTextPopupWindowRootView);
                if (!addTextEditText.getText().toString().equals("")) {
                    if (stickerChangetext != null) {
                        ((TextSticker) stickerChangetext).setText(addTextEditText.getText().toString());
                        ((TextSticker) stickerChangetext).setTextColor(addTextEditText.getCurrentTextColor());
                        ((TextSticker) stickerChangetext).setTypeface(addTextEditText.getTypeface());
                        ((TextSticker) stickerChangetext).resizeText();
                        allItemBitMapAddToGif.replace(stickerChangetext);
                        allItemBitMapAddToGif.invalidate();
                        stickerChangetext = null;
                    } else
                        allItemBitMapAddToGif.addSticker(addText(addTextEditText.getText().toString(), addTextEditText.getCurrentTextColor(), addTextEditText.getTypeface()));


                }
            }
        });
        buttonEditTextToGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chekKeyboard = false;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                relativeLayoutRootview.removeView(addTextPopupWindowRootView);
                if (StringUtils.stringIsNotEmpty(textChangeAddToGif)) {
                    textChangeAddToGif = "";
                }
            }
        });
    }

    private void setEnableBackWardOff(boolean isEnable) {
        if (isEnable) {
            imgBackWardOff.setImageResource(R.drawable.ic_backward_on);
        } else {
            imgBackWardOff.setImageResource(R.drawable.ic_backward_off);
        }
    }

    private void setEnableForwardOff(boolean isEnable) {
        if (isEnable) {
            imgForwardOff.setImageResource(R.drawable.ic_forward_on);
        } else {
            imgForwardOff.setImageResource(R.drawable.ic_forward_off);
        }
    }

    @Override
    public void onUpdateList() {
    }

    @Override
    public void onRemoveList() {
    }

    @Override
    public boolean hideList(boolean isHide) {
        this.isHidingListManager = isHide;
        return this.isHidingListManager;
    }

    // click effects for image
    @Override
    public void setFiltersImage(GPUImageFilter effectsImage) {
        this.effectCurrentImage = effectsImage;
        if (effectsImage != null) {
            if (brightnessImage == 0) {
                imageViewPreviewGif.setFilter(effectCurrentImage);
            } else {
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(effectCurrentImage);
                GPUImageBrightnessFilter gpuImageBrightnessFilter = new GPUImageBrightnessFilter(brightnessImage);
                filters.add(gpuImageBrightnessFilter);
                GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup(filters);
                imageViewPreviewGif.setFilter(gpuImageFilterGroup);
            }
        } else {
            if (brightnessImage != 0) {
                GPUImageBrightnessFilter gpuImageBrightnessFilter = new GPUImageBrightnessFilter(brightnessImage);
                imageViewPreviewGif.setFilter(gpuImageBrightnessFilter);
            } else {
                imageViewPreviewGif.setFilter(null);
            }
        }
    }

    @Override
    public void intiBrightnessImage(float valueBrightness) {
        this.brightnessImage = valueBrightness;

        if (brightnessImage == 0) {
            if (effectCurrentImage != null) {
                imageViewPreviewGif.setFilter(effectCurrentImage);
            } else {
                imageViewPreviewGif.setFilter(null);
            }
        } else {
            if (effectCurrentImage == null) {
                GPUImageBrightnessFilter gpuImageBrightnessFilter = new GPUImageBrightnessFilter(brightnessImage);
                imageViewPreviewGif.setFilter(gpuImageBrightnessFilter);
            } else {
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(effectCurrentImage);
                GPUImageBrightnessFilter gpuImageBrightnessFilter = new GPUImageBrightnessFilter(brightnessImage);
                filters.add(gpuImageBrightnessFilter);
                GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup(filters);
                imageViewPreviewGif.setFilter(gpuImageFilterGroup);
            }
        }
    }

    private MaterialDialog creatProgressDialog(int title) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(R.string.waiting)
                .progressIndeterminateStyle(true)
                .progress(false, 100)
                .cancelable(false)
                //.positiveText("Cancel")
                .build();
        return materialDialog;
    }

    private void pushNotification(String pathImageGif) {
        Toast.makeText(GifEditorActivity.this, getResources().getString(R.string.toast_screenshot_successful) + pathImageGif, Toast.LENGTH_SHORT).show();
        String extension = pathImageGif.substring(pathImageGif.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);

        File file = new File(pathImageGif);
        if (mimeType == null)
            mimeType = "*/*";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);
        intent.setDataAndType(data, mimeType);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);
        NotificationManager mManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()).setContentTitle(getResources().getString(R.string.creat_gif_complete)).setAutoCancel(true).setContentIntent(pIntent).setContentText(pathImageGif).setSmallIcon(R.mipmap.ic_launcher);
        mManager.notify(100, builder.build());
    }

    private void shareGif(String pathGif) {
        File share = new File(pathGif);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        Uri uri = Uri.fromFile(share);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share Emoji"));
    }

    @Override
    public void initFpsImageGif(float fpsImageGif) {
        this.fpsGif = 1 / fpsImageGif;
        imageViewPreviewGif.setSpeed(fpsGif);
    }

    @Override
    public void dataImage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Bitmap bitmapdecode = BitMapUtils.decodeSampledImageToBitmap(bitmap, 100, 100);
            initActionIconAddSticker();
            allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, flipIcon, zoomIcon, rotateIcon));
            allItemBitMapAddToGif.addSticker(addImageSticker(bitmapdecode));
            allItemBitMapAddToGif.invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addImage(Bitmap bitmap) {
        Bitmap bitmapdecode = BitMapUtils.decodeSampledImageToBitmap(bitmap, 100, 100);
        initActionIconAddSticker();
        allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, flipIcon, zoomIcon, rotateIcon));
        allItemBitMapAddToGif.addSticker(addImageSticker(bitmapdecode));
        allItemBitMapAddToGif.invalidate();
    }

    @Override
    public void indexFrameAddToGif(int indexOfListFrame, int indexFrame) {
        if (indexOfListFrame == 0 && indexFrame == 0) {
            if (addViewsFrame.size() == 0) {

            } else {
                addViewsFrame.remove(0);
                addViewsFrame.remove(imageRootView);
            }
        } else if (addViewsFrame.size() > 0) {
            addViewsFrame.remove(0);
            allItemBitMapAddToGif.removeView(imageRootView);
        }
        int resourceId = getResources().getIdentifier("list" + indexOfListFrame, "array", this.getPackageName());
        String[] listStick = getResources().getStringArray(resourceId);
        int idSticker = getResources().getIdentifier(listStick[indexFrame], "drawable", this.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), idSticker);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageRootView = inflater.inflate(R.layout.add_frame_item, null);
        ImageView imageframe = (ImageView) imageRootView.findViewById(R.id.img_addframe);
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (indexOfListFrame != 0 && indexFrame != 0) {
            Bitmap bitmapresize = AddFrameHelper.insertFrameType(GifEditorActivity.this, allItemBitMapAddToGif.getWidth(), allItemBitMapAddToGif.getHeight(), indexOfListFrame, indexFrame);

            imageframe.setImageBitmap(bitmapresize);
        }
        if (indexOfListFrame != 0 && indexFrame != 0) {
            allItemBitMapAddToGif.addView(imageRootView);
            addViewsFrame.add(imageRootView);
        }
    }

    @Override
    public void indexStickerAddToGif(int indexOfListSticker, int indexsticker) {
        int resourceId = getResources().getIdentifier("stick" + indexOfListSticker, "array", this.getPackageName());
        String[] listStick = getResources().getStringArray(resourceId);
        int idSticker = getResources().getIdentifier(listStick[indexsticker], "drawable", this.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), idSticker);
        initActionIconAddSticker();
        allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, flipIcon, zoomIcon, rotateIcon));
        allItemBitMapAddToGif.addSticker(addImageSticker(bitmap));
        allItemBitMapAddToGif.invalidate();

    }

    private void initIconView() {
        deleteIcon = new BitmapStickerIcon(this, BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_closetext),
                BitmapStickerIcon.LEFT_TOP);
        //  deleteIcon.setIconEvent(new DeleteIconEvent());
        editText = new BitmapStickerIcon(this, BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_editob),
                BitmapStickerIcon.RIGHT_TOP);
        // editText.setIconEvent(new EditTextAddIconEvent());
        zoomIcon = new BitmapStickerIcon(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_expand), BitmapStickerIcon.RIGHT_BOTOM);
        // zoomIcon.setIconEvent(new ZoomIconEvent());
        flipIcon = new BitmapStickerIcon(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_releft),
                BitmapStickerIcon.RIGHT_TOP);
        rotateIcon = new BitmapStickerIcon(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_rotateob),
                BitmapStickerIcon.LEFT_BOTTOM);

        //flipIcon.setIconEvent(new FlipHorizontallyEvent());
        previewImageGif.setBackgroundResource(R.drawable.ic_play);
    }

    private DrawableSticker addImageSticker(Bitmap bitmapadd) {
        allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, rotateIcon));
        stickeradd = new DrawableSticker(this, bitmapadd);
        return stickeradd;
    }

    private void initActionIconAddSticker() {
        deleteIcon.setIconEvent(new DeleteIconEvent());
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        zoomIcon.setIconEvent(new ZoomIconEvent());
        rotateIcon.setIconEvent(new RotateIconEvent());
    }

    private void initActionAddText() {
        deleteIcon.setIconEvent(new DeleteIconEvent());
        rotateIcon.setIconEvent(new RotateIconEvent());
        zoomIcon.setIconEvent(new ZoomIconEvent());
        editText.setIconEvent(new EditTextAddIconEvent());
    }

    private TextSticker addText(String text, int color, Typeface typeface) {
        textToadd = new TextSticker(this);
        initActionAddText();
        allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, editText, zoomIcon, rotateIcon));
        textToadd.setDrawable(getResources().getDrawable(
                R.drawable.sticker_transparent_background));
        textToadd.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        textToadd.setText(text);
        textToadd.setTextColor(color);
        textToadd.setTypeface(typeface);
        textToadd.resizeText();
        return textToadd;
    }

    @Override
    public void onStickerAdded(@NonNull Sticker sticker) {
        allItemBitMapAddToGif.setShowBolder(true);
    }

    @Override
    public void onStickerClicked(@NonNull Sticker sticker) {
        allItemBitMapAddToGif.setShowBolder(true);
        if (sticker instanceof TextSticker) {
            initActionAddText();
            allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, editText, zoomIcon, rotateIcon));
        }
        if (sticker instanceof DrawableSticker) {
            initActionIconAddSticker();
            allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, flipIcon, zoomIcon, rotateIcon));
        }
    }

    @Override
    public void onStickerDeleted(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerDragFinished(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerZoomFinished(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerFlipped(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerDoubleTapped(@NonNull Sticker sticker) {

    }

    @Override
    public void onStartDrag(Sticker sticker) {
        if (sticker instanceof TextSticker) {
            initActionAddText();
            allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, editText, rotateIcon, zoomIcon));
        }
        if (sticker instanceof DrawableSticker) {
            initActionIconAddSticker();
            allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, flipIcon, zoomIcon, rotateIcon));
        }
    }

    @Override
    public void onEditTextAdd(Sticker sticker) {
        if (sticker instanceof TextSticker) {
            initActionAddText();
            allItemBitMapAddToGif.setIcons(Arrays.asList(deleteIcon, editText, flipIcon, zoomIcon, rotateIcon));
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            openAddTextPopupWindow(((TextSticker) sticker).getText(), ((TextSticker) sticker).getColor(), ((TextSticker) sticker).getTypace());
            stickerChangetext = sticker;
        }
    }

    @Override
    public void onClickOutSticker() {
        allItemBitMapAddToGif.setShowBolder(false);
        allItemBitMapAddToGif.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapAllAddToGif != null) {
            bitmapAllAddToGif.recycle();
        }
    }
}


