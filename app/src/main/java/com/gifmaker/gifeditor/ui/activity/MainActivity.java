package com.gifmaker.gifeditor.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.presenter.GifPresenter;
import com.gifmaker.gifeditor.ui.fragment.SlideShowFragment;
import com.gifmaker.gifeditor.ui.view.OnGetVideoListener;
import com.gifmaker.gifeditor.utils.ConfigApp;
import com.gifmaker.gifeditor.utils.FileUtils;
import com.gifmaker.gifeditor.utils.MemoryUtils;
import com.gifmaker.gifeditor.utils.PermissionUtils;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.RelativeLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.myinnos.awesomeimagepicker.activities.SelectImageActivity;
import in.myinnos.awesomeimagepicker.activities.SelectImageActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static com.gifmaker.gifeditor.utils.ConfigApp.LIMIT_IMAGE_SELECT_INPUT_GIF;
import static com.gifmaker.gifeditor.utils.ConfigApp.MIN_IMAGE_SELECT_INPUT_GIF;


public class MainActivity extends BaseActivity implements OnGetVideoListener {
    CircleImageView openCamera;
    private RelativeLayout buttonCreatGif;
    private RelativeLayout buttonInputFromVideo;
    private RelativeLayout buttonInputFromPhoto;
    private SlidingUpPanelLayout slidingLayoutMain;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private OnGetVideoListener onGetVideoListener;
    private GifPresenter mGifPresenter;
    private LinearLayout buttonMygif;
    private android.widget.ImageView visibleSliding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        PermissionUtils.requestStoragePermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionUtils.REQESTCODE_WRITE_EXTERNAL_STORAGE, new PermissionUtils.OnRequestPermissionsResult() {
            @Override
            public void permissionAccept(int requestCode) {
                return;
            }

            @Override
            public void permissionCancel(int requestCode) {
                finish();
            }
        });
        PermissionUtils.requestStoragePermission(this, Manifest.permission.CAMERA, PermissionUtils.REQUESTCODE_CAMERA, new PermissionUtils.OnRequestPermissionsResult() {
            @Override
            public void permissionAccept(int requestCode) {
                return;
            }

            @Override
            public void permissionCancel(int requestCode) {
                finish();
            }
        });
        initIdView();
        initActionView();
        initView();

    }

    private void initIdView() {
        buttonCreatGif = (RelativeLayout) findViewById(R.id.buttonCreatGif);
        openCamera = (CircleImageView) findViewById(R.id.open_camera);
        buttonInputFromVideo = (RelativeLayout) findViewById(R.id.buttonInputFromVideo);
        buttonInputFromPhoto = (RelativeLayout) findViewById(R.id.buttonInputFromPhoto);
        slidingLayoutMain = (SlidingUpPanelLayout) findViewById(R.id.slidingLayoutMain);
        buttonMygif = (LinearLayout) findViewById(R.id.buttonMygif);
        visibleSliding = (android.widget.ImageView) findViewById(R.id.visibleSliding);
    }

    private void initActionView() {
        buttonCreatGif.setOnClickListener(onClickListener);
        buttonInputFromVideo.setOnClickListener(onClickListener);
        buttonInputFromPhoto.setOnClickListener(onClickListener);
        buttonMygif.setOnClickListener(onClickListener);
        onGetVideoListener = this;
        openCamera.setOnClickListener(onClickListener);
        visibleSliding.setOnClickListener(onClickListener);

        slidingLayoutMain.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        slidingLayoutMain.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    private void initView() {
        goToFragment(new SlideShowFragment());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonCreatGif:
                    buttonCreatGifClick();
                    break;
                case R.id.buttonInputFromVideo:
                    buttonInputFromVideoClick();
                    break;
                case R.id.buttonInputFromPhoto:
                    buttonInputFromPhotoClick();
                    break;
                case R.id.open_camera:
                    dispatchTakeVideoIntent();
                    break;
                case R.id.buttonMygif:
                    buttonMygifClick();
                    break;
                case R.id.visibleSliding:
                    slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    break;
            }
        }
    };


    //OPEN camera to take video
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private void buttonCreatGifClick() {
        if (slidingLayoutMain != null
                && (slidingLayoutMain.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                || slidingLayoutMain.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
    }

    private void buttonInputFromVideoClick() {
        slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        try {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("video/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("video/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, ConfigApp.REQUEST_TAKE_GALLERY_VIDEO);
        } catch (Exception e) {
        }
    }

    private void buttonInputFromPhotoClick() {
        Intent intent = new Intent(MainActivity.this, SelectImageActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT_IMAGE_SELECT_INPUT_GIF);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_MIN, MIN_IMAGE_SELECT_INPUT_GIF);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

    private void buttonMygifClick() {
        startActivity(new Intent(MainActivity.this, MyGifActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(FileUtils.getPathFile(getApplicationContext(), data.getData()));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long second = (Long.parseLong(time) / 1000) % 60;
                if (second >= 5) {
                    startActivity(new Intent(MainActivity.this, CutVideoInputCreatGifActivity.class).putExtra("Uri", data.getData().toString()));
                } else {
                    error();
                }
            }
        } else if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ConfigApp.REQUEST_TAKE_GALLERY_VIDEO) {
                onGetVideoListener.onprogress(data.getData().toString());
            } else if (requestCode == ConstantsCustomGallery.REQUEST_CODE) {
                ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
                if (images == null || images.size() == 0) {
                    return;
                } else {
                    startActivity(new Intent(MainActivity.this, GifEditorActivity.class)
                            .putExtra("input", "photo")
                            .putExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES, images));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingLayoutMain != null &&
                (slidingLayoutMain.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingLayoutMain.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        System.exit(0);
    }

    private void goToFragment(Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit, R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        ft.replace(R.id.mainFrameLayout, fragment);
        ft.commit();
    }

    private void unregisterListener() {
        if (onGetVideoListener != null) {
            onGetVideoListener = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        slidingLayoutMain.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        unregisterListener();
        MemoryUtils.releaseMemoryByGlide(this);
    }

    @Override
    public void sucsess(String path) {
        startActivity(new Intent(MainActivity.this, CutVideoInputCreatGifActivity.class).putExtra("Uri", path));
        mGifPresenter = null;
    }


    @Override
    public void error() {
        Toast.makeText(getApplicationContext(), "The time to play a video at least 5 seconds", Toast.LENGTH_SHORT).show();
        if (mGifPresenter != null) {
            mGifPresenter = null;
        }
    }

    @Override
    public void onprogress(String path) {
        mGifPresenter = new GifPresenter(getApplicationContext(), FileUtils.getPathFile(getApplicationContext(), Uri.parse(path)));
        if (mGifPresenter.getDurationVideo() < 5) {
            error();
        } else {
            sucsess(path);
        }
    }
}
