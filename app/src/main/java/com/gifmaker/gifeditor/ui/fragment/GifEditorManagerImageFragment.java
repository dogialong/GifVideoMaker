package com.gifmaker.gifeditor.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gifmaker.gifeditor.R;
import com.gifmaker.gifeditor.ui.activity.GifEditorActivity;
import com.gifmaker.gifeditor.ui.adatper.ManagerImageItemAdapter;
import com.gifmaker.gifeditor.ui.view.GifEditorManagerImageCallBack;
import com.gifmaker.gifeditor.ui.view.OnStartDragListener;
import com.gifmaker.gifeditor.ui.view.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.SelectImageActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.helpers.Singleton;
import in.myinnos.awesomeimagepicker.models.Image;

import static com.gifmaker.gifeditor.R.id.imgDeleteItemManage;
import static com.gifmaker.gifeditor.utils.ConfigApp.LIMIT_IMAGE_SELECT_INPUT_GIF;

/**
 * Created by PhungVanQuang on 7/31/2017.
 */

public class GifEditorManagerImageFragment extends BaseFragment implements View.OnClickListener, OnStartDragListener {

    private View rootView;
    private ArrayList<Image> listDataImages = new ArrayList<>();
    private ArrayList<Boolean> arrIsSelected = new ArrayList<>();
    private RecyclerView listImageInputToGif;
    private ManagerImageItemAdapter managerImageItemAdapter;
    private GifEditorManagerImageCallBack managerImageCallBack;
    private int positionSelected = -1;
    public static final int MAX_IMAGE_LIMIT = 50;
    private LinearLayoutManager layoutManager;
    private TextView tvSizeFrames, tvDragandDrop;
    private RelativeLayout imgPushUp,btnCancel;
    private RelativeLayout rlManagerImage, rlDropText;
    private ItemTouchHelper mItemTouchHelper;
    private Button btnAddImage;
    private ImageView imgIconPushup;
    private int defaultWith = 0;
    private int defaultHeight = 0;
    public static GifEditorManagerImageFragment newInsFragmentWorkout(ArrayList<Image> images) {
        GifEditorManagerImageFragment mFragment = new GifEditorManagerImageFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList(ConstantsCustomGallery.INTENT_EXTRA_IMAGES, images);
        mFragment.setArguments(mBundle);
        return mFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDataImages = getArguments().getParcelableArrayList(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editor_manager_image, null, false);
        initIdView();
        initView();
        initValue();
        return rootView;
    }

    private void initValue() {
        for (int i = 0; i < listDataImages.size(); i++) {
            arrIsSelected.add(false);
        }

    }

    private void initIdView() {
        rlManagerImage = (RelativeLayout) rootView.findViewById(R.id.rlManagerImage);
        listImageInputToGif = (RecyclerView) rootView.findViewById(R.id.listImageInputToGif);
        tvSizeFrames = (TextView) rootView.findViewById(R.id.tvSizeFrame);
        tvDragandDrop = (TextView) rootView.findViewById(R.id.tvDragandDrop);
        imgIconPushup = (ImageView) rootView.findViewById(R.id.imgIconPushup);
        imgPushUp = (RelativeLayout) rootView.findViewById(R.id.btnPushup);
        rlDropText = (RelativeLayout) rootView.findViewById(R.id.rlDropText);
        imgPushUp.setOnClickListener(this);
        btnAddImage = (Button) rootView.findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(this);
        btnCancel = (RelativeLayout) rootView.findViewById(R.id.buttonCancelCustomImageToGif);
        btnCancel.setOnClickListener(this);
        hideDragText();

    }

    public void setVerticalView() {
        defaultWith = getScreenWidth();
        defaultHeight = rlManagerImage.getMeasuredHeight();
        mItemTouchHelper.attachToRecyclerView(listImageInputToGif);
        layoutManager = null;
        layoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        listImageInputToGif.setLayoutManager(layoutManager);
        showDragText();
        setBackgroundPushUp();
//        managerImageItemAdapter.notifyDataSetChanged();
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public void setHozizontalListView() {

        mItemTouchHelper.attachToRecyclerView(null);
        layoutManager = null;
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        listImageInputToGif.setLayoutManager(layoutManager);
        managerImageItemAdapter.notifyDataSetChanged();
        hideDragText();
        setBackgroundPushDown();
    }

    private void hideDragText() {
        this.rlDropText.setVisibility(View.GONE);
    }

    private void showDragText() {
        this.rlDropText.setVisibility(View.VISIBLE);
    }

    private void setBackgroundPushUp() {
        imgIconPushup.setImageResource(R.drawable.ic_arrowdown);
    }

    private void setBackgroundPushDown() {
        imgIconPushup.setImageResource(R.drawable.ic_arrowup);
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        listImageInputToGif.setLayoutManager(layoutManager);
        managerImageItemAdapter = new ManagerImageItemAdapter(getContext(), listDataImages, arrIsSelected, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(managerImageItemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);

        listImageInputToGif.setAdapter(managerImageItemAdapter);

        managerImageItemAdapter.setOnItemClickListener(new ManagerImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case imgDeleteItemManage:
                        removeImage(position, managerImageItemAdapter);
                        break;
                    default:
                        break;
                }
            }

        });
        tvSizeFrames.setText(String.valueOf(listDataImages.size()) + " frames");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GifEditorActivity) getActivity()).initGifAction(GifEditorManagerImageFragment.class.toString());
        if (Singleton.getGetInstance().isUpdated) {
            checkLimitItem(Singleton.getGetInstance().listImageUpdated, listDataImages.size());
            Singleton.getGetInstance().isUpdated = false;
        }
        showManagerImage();
    }

    public void showManagerImage() {
        if (rlManagerImage != null) {
            if (rlManagerImage.getVisibility() == View.GONE) {
                rlManagerImage.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initManagerCallBack(GifEditorManagerImageCallBack managerImageCallBack) {
        this.managerImageCallBack = managerImageCallBack;
    }

    private void addImage(int position) {
        Intent intent = new Intent(getActivity(), SelectImageActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT_IMAGE_SELECT_INPUT_GIF-listDataImages.size());
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_MIN, 0);
        intent.putExtra(ConstantsCustomGallery.UPDATE_LIST, "true");
        intent.putExtra("postion", position);
        intent.putParcelableArrayListExtra(ConstantsCustomGallery.LIST_SELECTED,listDataImages);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE_ADD_IMAGE);
    }

    ;

    private void removeImage(int index, RecyclerView.Adapter adapter) {
        if (listDataImages.size() - 1 >= 5) {
            listDataImages.remove(index);
            arrIsSelected.remove(arrIsSelected.get(index));

            if (((GifEditorActivity) getActivity()).listDataImages != null) {
                ((GifEditorActivity) getActivity()).listDataImages = new ArrayList<>();
                ((GifEditorActivity) getActivity()).listDataImages.addAll(listDataImages);
            }
            managerImageCallBack.onRemoveList();
            adapter.notifyItemRemoved(index);
            tvSizeFrames.setText(String.valueOf(listDataImages.size()) + " frames");
        } else {
            Toast.makeText(getContext(), "Must have 5 images to make gif.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateList(ArrayList<Image> arrUpdated, int position) {
        listDataImages.clear();
        listDataImages.addAll(arrUpdated);
        removeItemIfOverLimit(listDataImages);
        arrIsSelected.clear();
        for (int i = 0; i < listDataImages.size(); i++) {
            arrIsSelected.add(false);
        }
        if (((GifEditorActivity) getActivity()).listDataImages != null) {
            ((GifEditorActivity) getActivity()).listDataImages = new ArrayList<>();
            ((GifEditorActivity) getActivity()).listDataImages.addAll(listDataImages);
        }
        managerImageCallBack.onUpdateList();
        managerImageItemAdapter.notifyDataSetChanged();
        tvSizeFrames.setText(String.valueOf(listDataImages.size()) + " frames");
    }

    private void removeItemIfOverLimit(ArrayList<Image> arrImage) {
        if (arrImage.size() > MAX_IMAGE_LIMIT) {
            for (int i = arrImage.size() - 1; i >= MAX_IMAGE_LIMIT; i--) {
                arrImage.remove(i);
            }
        }

    }

    private void checkLimitItem(ArrayList<Image> arrUpdated, int position) {
        if (listDataImages.size() >= MAX_IMAGE_LIMIT) {
            Toast.makeText(getContext(), "Limited", Toast.LENGTH_SHORT).show();
        } else {
            updateList(arrUpdated, position);
        }
    }

    private boolean isUp = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPushup:
                if (isUp) {
                    setHozizontalListView();
                    if (((GifEditorActivity) getActivity()) != null  && ((GifEditorActivity) getActivity()).widthDefaultOfFrame != 0) {
                        ((GifEditorActivity) getActivity()).drawViewFragmentDown();
                    }  else {
                        ((GifEditorActivity) getActivity()).drawViewFragmentDown(defaultWith,defaultHeight);
                    }
                } else {
                    setVerticalView();
                    if (((GifEditorActivity) getActivity()) != null) {
                        ((GifEditorActivity) getActivity()).drawViewFragment();
                    }
                }
                isUp = !isUp;
                break;
            case R.id.btnAddImage:
                addImage(listDataImages.size() - 1);
                break;
            case R.id.buttonCancelCustomImageToGif:
                if (isUp) {
                    setHozizontalListView();
                    if (((GifEditorActivity) getActivity()) != null  && ((GifEditorActivity) getActivity()).widthDefaultOfFrame != 0) {
                        ((GifEditorActivity) getActivity()).drawViewFragmentDown();
                    }  else {
                        ((GifEditorActivity) getActivity()).drawViewFragmentDown(defaultWith,defaultHeight);
                    }
                    if (rlManagerImage.getVisibility() == View.GONE) {
                        rlManagerImage.setVisibility(View.VISIBLE);
                    }
                    isUp = !isUp;
                } else {
                    rlManagerImage.setVisibility(View.GONE);
                    managerImageCallBack.hideList(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

        if (isUp) {
            mItemTouchHelper.startDrag(viewHolder);
        }

    }
}
