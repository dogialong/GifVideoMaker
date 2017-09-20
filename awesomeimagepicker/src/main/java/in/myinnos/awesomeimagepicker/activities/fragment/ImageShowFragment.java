package in.myinnos.awesomeimagepicker.activities.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.adapter.CustomImageSelectAdapter;
import in.myinnos.awesomeimagepicker.adapter.ListImageChooseAdapter;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageShowFragment extends Fragment {


    private View imageShowFragment;

    private String pathAlbum;

    private ArrayList<Image> images;
    private ArrayList<Image> imagesSelect;

    private LinearLayout linearSelect;

    private TextView tvSelectCount;
    private ProgressBar loader;
    private RecyclerView recyclerView;
    private CustomImageSelectAdapter adapter;
    private int countSelected;
    private ContentObserver observer;
    private  Handler handler;
    private  Thread thread;
    private RecyclerView listImageSelect;
    private ListImageChooseAdapter listImageChooseAdapter;

    private ImageSelectCallBack imageSelectCallBack;

    private final String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private ImageLoaderRunnable mImageLoaderRunnable;
    private int count_limit = 50;

    public ImageShowFragment() {
        // Required empty public constructor
    }

    public static ImageShowFragment newInsFragmentWorkout(String album, ArrayList<Image> imageArrayList) {
        ImageShowFragment mFragment = new ImageShowFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("pathAlbum", album);
        mBundle.putParcelableArrayList("listSeclect", imageArrayList);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        pathAlbum = bundle.getString("pathAlbum");
        imagesSelect = bundle.getParcelableArrayList("listSeclect");
        countSelected = imagesSelect.size();
        images = new ArrayList<>();
        mImageLoaderRunnable = new ImageLoaderRunnable(getContext(), pathAlbum, images, new ImageLoaderRunnable.OnImageLoaderRunaable() {
            @Override
            public void onError() {
                sendMessage(ConstantsCustomGallery.ERROR);
            }

            @Override
            public void onSuccess(ArrayList<Image> arrayList) {
                images = arrayList;
                sendMessage(ConstantsCustomGallery.FETCH_COMPLETED);
            }

            @Override
            public void onStart() {
                sendMessage(ConstantsCustomGallery.FETCH_STARTED);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantsCustomGallery.PERMISSION_GRANTED: {
                        loadImages();
                        break;
                    }
                    case ConstantsCustomGallery.FETCH_STARTED: {
                        loader.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case ConstantsCustomGallery.FETCH_COMPLETED: {
                        for (int i = 0; i < imagesSelect.size(); i++) {
                            for (int j = 0; j < images.size(); j++) {
                                if (imagesSelect.get(i).path.equals(images.get(j).getPath())) {
                                    images.get(j).isSelected = !images.get(j).isSelected;
                                }
                            }
                        }
                        adapter = new CustomImageSelectAdapter(getActivity(), images);
                        recyclerView.setAdapter(adapter);
                        initAction();
                        listImageChooseAdapter = new ListImageChooseAdapter(getContext(),imagesSelect);
                        listImageChooseAdapter.setOnItemClickListener(new ListImageChooseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                for (int i = 0; i < images.size(); i++) {
                                    if (images.get(i).getPath().equals(imagesSelect.get(position).getPath())) {
                                        countSelected--;
                                        images.get(i).isSelected = !images.get(i).isSelected;
                                        adapter.notifyDataSetChanged();
                                        tvSelectCount.setText(getString(R.string.total_image_add_more) + " " + (count_limit - countSelected));
                                        tvSelectCount.setVisibility(View.VISIBLE);
                                        if (countSelected == 0) {
                                            linearSelect.setVisibility(GONE);
                                        } else {
                                            linearSelect.setVisibility(View.VISIBLE);
                                        }
                                        break;
                                    }
                                }
                                imagesSelect.remove(position);
                                imageSelectCallBack.listImage(imagesSelect);
                                adapter.notifyDataSetChanged();
                                listImageChooseAdapter.notifyDataSetChanged();
                            }
                        });
                        listImageSelect.setAdapter(listImageChooseAdapter);
                        loader.setVisibility(GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        orientationBasedUI();
                        if (countSelected > 0) {
                            linearSelect.setVisibility(View.VISIBLE);
                            tvSelectCount.setText(getString(R.string.total_image_add_more) + " " + (count_limit- countSelected));
                        }

                        break;
                    }

                    case ConstantsCustomGallery.ERROR: {
                        loader.setVisibility(GONE);
                        break;
                    }

                    default: {
                        super.handleMessage(msg);
                    }
                }
            }
        };
        Message message = handler.obtainMessage();
        message.what = ConstantsCustomGallery.PERMISSION_GRANTED;
        message.sendToTarget();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageShowFragment = inflater.inflate(R.layout.fragment_image_show, container, false);
        initIdView();

        return imageShowFragment;
    }

    private void initIdView() {
        recyclerView = (RecyclerView) imageShowFragment.findViewById(R.id.gridViewImageSelect);
        listImageSelect = (RecyclerView) imageShowFragment.findViewById(R.id.listImageSelect);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listImageSelect.setLayoutManager(layoutManager);
        tvSelectCount = (TextView) imageShowFragment.findViewById(R.id.tvSelectCount);
        loader = (ProgressBar) imageShowFragment.findViewById(R.id.loader);
        linearSelect = (LinearLayout) imageShowFragment.findViewById(R.id.linearSelect);

    }

    private void initAction() {
        adapter.setOnItemClickListener(new CustomImageSelectAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                toggleSelection(position);
                tvSelectCount.setText(getString(R.string.total_image_add_more) + " " + (count_limit - countSelected));
                if (countSelected == 0) {
                    linearSelect.setVisibility(GONE);
                } else {
                    linearSelect.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    public void initImageSelectCallBack(ImageSelectCallBack imageSelectCallBack) {
        this.imageSelectCallBack = imageSelectCallBack;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageShowFragment = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (thread != null) {
            thread.interrupt();

        }

        if (adapter != null) {
            adapter = null;
        }
        if (listImageChooseAdapter != null) {
            listImageChooseAdapter.clearGlide();
            listImageChooseAdapter = null;
        }
        if (mImageLoaderRunnable != null) {
            mImageLoaderRunnable = null;
        }

        if (loader != null) {
            loader = null;
        }
    }

    private void loadImages() {
        startThread(mImageLoaderRunnable);
    }

    private void toggleSelection(int position) {
        if (images.size() > 0 && position != -1) {
            if (!images.get(position).isSelected && countSelected >= (count_limit)) {
                Toast.makeText(
                        getContext(),
                        String.format(getString(R.string.limit_exceeded), count_limit),
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            images.get(position).isSelected = !images.get(position).isSelected;
            Image imageSeleted = null;
            for(Image image : imagesSelect) {
                if(image.id == (images.get(position).id)) {
                    imageSeleted = image;
                }
            }
            if (images.get(position).isSelected) {
                countSelected++;
                imagesSelect.add(images.get(position));
                imageSelectCallBack.listImage(imagesSelect);
            } else {
                countSelected--;
                if (imageSeleted!= null) {
                    imagesSelect.remove(imageSeleted);
                }
                imageSelectCallBack.listImage(imagesSelect);
            }
            adapter.notifyDataSetChanged();
            listImageChooseAdapter.notifyDataSetChanged();
        }

    }


    private void orientationBasedUI() {
        final WindowManager windowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (adapter != null) {
            int size = metrics.widthPixels / 3;
            adapter.setLayoutParams(size);
        }
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(),3,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();
    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static  class ImageLoaderRunnable implements Runnable {
        private static final String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        private OnImageLoaderRunaable mOnImageLoaderRunaable;
        private Context context;
        private String pathAlbum;
        private ArrayList<Image> images;
        private interface OnImageLoaderRunaable {
            public void onError();

            public void onSuccess(ArrayList<Image> arrayList);

            public void onStart();
        }
        public ImageLoaderRunnable(Context context, String pathAlbum, ArrayList<Image> images, OnImageLoaderRunaable onImageLoaderRunaable) {
            this.mOnImageLoaderRunaable = onImageLoaderRunaable;
            this.context = context;
            this.pathAlbum = pathAlbum;
            this.images = images;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            if (mOnImageLoaderRunaable == null) {
                mOnImageLoaderRunaable.onStart();
            }
            File file;
            HashSet<Long> selectedImages = new HashSet<>();
            if (images != null) {
                Image image;
                for (int i = 0, l = images.size(); i < l; i++) {
                    image = images.get(i);
                    file = new File(image.path);
                    if (file.exists() && image.isSelected) {
                        selectedImages.add(image.id);
                    }
                }
            }
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{pathAlbum}, MediaStore.Images.Media.DATE_ADDED);
            if (cursor == null) {
                mOnImageLoaderRunaable.onError();
                return;
            }
            int tempCountSelected = 0;
            ArrayList<Image> temp = new ArrayList<>(cursor.getCount());
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }
                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                    boolean isSelected = selectedImages.contains(id);
                    if (isSelected) {
                        tempCountSelected++;
                    }
                    file = null;
                    try {
                        file = new File(path);
                    } catch (Exception e) {
                        Log.d("Exception : ", e.toString());
                    }
                    if (file.exists()) {
                        temp.add(new Image(id, name, path, isSelected));
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();
            if (images == null) {
                images = new ArrayList<>();
            }
            images.clear();
            images.addAll(temp);
            mOnImageLoaderRunaable.onSuccess(temp);
        }
    }

    private void sendMessage(int what) {
        sendMessage(what, 0);
    }

    private void sendMessage(int what, int arg1) {
        if (handler == null) {
            return;
        }
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.sendToTarget();
    }
}
