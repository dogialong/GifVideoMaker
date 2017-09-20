package in.myinnos.awesomeimagepicker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.activities.fragment.AlbumSelectCallBack;
import in.myinnos.awesomeimagepicker.activities.fragment.AlbumShowFragment;
import in.myinnos.awesomeimagepicker.activities.fragment.ImageSelectCallBack;
import in.myinnos.awesomeimagepicker.activities.fragment.ImageShowFragment;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.helpers.Singleton;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by PhungVanQuang on 8/11/2017.
 */

public class SelectImageActivity extends HelperActivity implements AlbumSelectCallBack, ImageSelectCallBack {


    public static String TAG = "quang.phungvan";
    private AlbumShowFragment albumShowFragment;
    private ImageShowFragment imageShowFragment;
    private TextView tvProfile,tvDone;

    public int limit;

    private ArrayList<Image> imagesSelect;

    private LinearLayout liFinish;
    private String flagUpdateListGif = "";
    private int positionNeedToUpdate = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.select_image_activity);
        setView(findViewById(R.id.select_image_activity));

        final Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        if (intent != null) {
            limit = intent.getIntExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, ConstantsCustomGallery.DEFAULT_LIMIT);
            ConstantsCustomGallery.min = intent.getIntExtra(ConstantsCustomGallery.INTENT_EXTRA_MIN, ConstantsCustomGallery.DEFAULT_MIN);
            imagesSelect = intent.getParcelableArrayListExtra(ConstantsCustomGallery.LIST_SELECTED);
        }
        if (imagesSelect == null) {
            imagesSelect = new ArrayList<>();
        }
        flagUpdateListGif = intent.getStringExtra(ConstantsCustomGallery.UPDATE_LIST);
        positionNeedToUpdate = intent.getIntExtra("position",-1);

        initIdView();
        initAction();

        albumShowFragment = new AlbumShowFragment();
        albumShowFragment.initAlbumSelectCallBack(this);
        goToFragment(albumShowFragment);
        tvProfile.setText(R.string.album_view);
        tvDone.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (albumShowFragment != null) {
            albumShowFragment = null;
        }
        if (imageShowFragment != null) {
            imageShowFragment = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (albumShowFragment != null) {
            albumShowFragment = null;
        }
        if (imageShowFragment != null) {
            imageShowFragment = null;
        }
    }

    private void initIdView() {
        liFinish = (LinearLayout) findViewById(R.id.liFinish);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        tvDone = (TextView) findViewById(R.id.tvDone);
    }

    private void initAction() {
        liFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainLayoutSelectImage);
                if (f instanceof AlbumShowFragment) {
                    finish();
                } else {
                    albumShowFragment = new AlbumShowFragment();
                    albumShowFragment.initAlbumSelectCallBack(SelectImageActivity.this);
                    goToFragment(albumShowFragment);
                    tvProfile.setText(R.string.album_view);
                    tvDone.setVisibility(View.GONE);
                }
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneSelect();
            }
        });
    }

    public void goToFragment(Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit, R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        ft.replace(R.id.mainLayoutSelectImage, fragment);
        ft.commit();
    }


    @Override
    public void initPathSelect(String pathAlbumSelect) {
        imageShowFragment = ImageShowFragment.newInsFragmentWorkout(pathAlbumSelect,imagesSelect);
        imageShowFragment.initImageSelectCallBack(this);
        goToFragment(imageShowFragment);
        tvProfile.setText(pathAlbumSelect);
        tvDone.setVisibility(View.VISIBLE);
    }

    @Override
    public void doneSelect() {

        if (imagesSelect.size() < ConstantsCustomGallery.min) {
            Toast.makeText(
                    getApplicationContext(),
                    String.format(getString(R.string.min_exceeded), ConstantsCustomGallery.min),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (flagUpdateListGif != null && flagUpdateListGif.equals("true")) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES, imagesSelect);
            setResult(RESULT_OK, intent);
            finish();
            Singleton.getGetInstance().isUpdated = true;
            Singleton.getGetInstance().listImageUpdated = imagesSelect;
            Singleton.getGetInstance().positionNeedToUpdate = positionNeedToUpdate;
        } else {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES, imagesSelect);
            setResult(RESULT_OK, intent);
            finish();
            Singleton.getGetInstance().isUpdated = false;
            Singleton.getGetInstance().listImageUpdated.clear();
        }
    }

    @Override
    public void listImage(ArrayList<Image> imagesSelect) {
        this.imagesSelect = imagesSelect;
    }
}
