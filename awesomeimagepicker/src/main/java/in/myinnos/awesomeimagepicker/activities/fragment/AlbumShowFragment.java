package in.myinnos.awesomeimagepicker.activities.fragment;


import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.adapter.CustomAlbumSelectAdapter;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Album;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumShowFragment extends Fragment {

    private ArrayList<Album> listAlbum;
    private  Handler handler;
    private  Thread thread;
    private RecyclerView gridView;
    private LinearLayoutManager layoutManager;
    private CustomAlbumSelectAdapter adapter;
    private ProgressBar progressLoader;
    private Message message;
    private AlbumSelectCallBack albumSelectCallBack;

    private AlbumLoaderRunnable mAlbumLoaderRunnable;

    public AlbumShowFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantsCustomGallery.PERMISSION_GRANTED: {
                        loadAlbums();
                        break;
                    }
                    case ConstantsCustomGallery.FETCH_STARTED: {
                        progressLoader.setVisibility(View.VISIBLE);
                        break;
                    }
                    case ConstantsCustomGallery.FETCH_COMPLETED: {
                        if (adapter == null) {
                            adapter = new CustomAlbumSelectAdapter( getActivity(), listAlbum);
                            gridView.setLayoutManager(layoutManager);
                            gridView.setAdapter(adapter);
                            initActionView();
                            progressLoader.setVisibility(View.GONE);
                            orientationBasedUI();
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    }
                    case ConstantsCustomGallery.ERROR: {
                        progressLoader.setVisibility(View.GONE);
                        break;
                    }
                    default: {
                        super.handleMessage(msg);
                    }
                }
            }
        };

        mAlbumLoaderRunnable = new AlbumLoaderRunnable(getActivity(), new AlbumLoaderRunnable.OnListennerLoadAbumble() {
            @Override
            public void onError(String msg) {
                sendMessage(ConstantsCustomGallery.ERROR);
            }

            @Override
            public void onSuccess(ArrayList<Album> temp) {
                listAlbum = temp;
                sendMessage(ConstantsCustomGallery.FETCH_COMPLETED);
            }

            @Override
            public void onStart() {
                sendMessage(ConstantsCustomGallery.FETCH_STARTED);
            }
        });
        message = handler.obtainMessage();
        message.what = ConstantsCustomGallery.PERMISSION_GRANTED;
        message.sendToTarget();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View albumShowFragment = inflater.inflate(R.layout.fragment_album_show, container, false);
        initIdView(albumShowFragment);

        return albumShowFragment;
    }

    private void initIdView(View albumShowFragment) {

        gridView = (RecyclerView) albumShowFragment.findViewById(R.id.gridViewAlbumSelect);
        progressLoader = (ProgressBar) albumShowFragment.findViewById(R.id.progressLoader);
        layoutManager = new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false);
    }
    private void initActionView() {

        adapter.setOnItemClick(new CustomAlbumSelectAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                albumSelectCallBack.initPathSelect(listAlbum.get(position).name);
            }
        });
    }

    public void initAlbumSelectCallBack(AlbumSelectCallBack albumSelectCallBack){
        this.albumSelectCallBack= albumSelectCallBack;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler =null;
        }
        stopThread();
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        if (albumSelectCallBack != null) {
            albumSelectCallBack = null;
        }

        if (message != null) {
            message = null;
        }
        if (mAlbumLoaderRunnable != null) {
            mAlbumLoaderRunnable = null;
        }
        if (progressLoader != null) {
            progressLoader = null;
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        stopThread();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (mAlbumLoaderRunnable != null) {
            mAlbumLoaderRunnable = null;
        }

    }

    private static class AlbumLoaderRunnable implements Runnable {

        private final static String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA};

        public static  interface OnListennerLoadAbumble{

            public void onError(String msg);
            public void onSuccess(ArrayList<Album> listAlbum);
            public void onStart();
        }

        private OnListennerLoadAbumble mOnListennerLoadAbumble;
        private Context context;


        public AlbumLoaderRunnable(Context context, OnListennerLoadAbumble mOnListennerLoadAbumble){
            this.context = context;
            this.mOnListennerLoadAbumble = mOnListennerLoadAbumble;
        }


        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            if(mOnListennerLoadAbumble!=null)
                mOnListennerLoadAbumble.onStart();
            String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + " like ? OR " +MediaStore.Files.FileColumns.MIME_TYPE + " like ? OR "
                    + MediaStore.Files.FileColumns.MIME_TYPE + " like ? ";
            String[] selectionArgsPdf = new String[]{ "%jpg","%png","%jpeg" };
            Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                            selectionMimeType, selectionArgsPdf, MediaStore.Images.Media.DATE_MODIFIED);
            if (cursor == null) {
             //   sendMessage(ConstantsCustomGallery.ERROR);
                if(mOnListennerLoadAbumble!=null)
                    mOnListennerLoadAbumble.onError("");
                return;
            }
            ArrayList<Album> temp = new ArrayList<>(cursor.getCount());
            HashSet<Long> albumSet = new HashSet<>();
            File file;
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }
                    long albumId = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String album = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String image = cursor.getString(cursor.getColumnIndex(projection[2]));

                    if (!albumSet.contains(albumId)) {
                        file = new File(image);
                        if (file.exists()) {
                            temp.add(new Album(album, image));
                            albumSet.add(albumId);
                        }
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();
            if(mOnListennerLoadAbumble!=null){
                mOnListennerLoadAbumble.onSuccess(temp);
            }
        }
    }

    private void loadAlbums() {
        startThread(mAlbumLoaderRunnable);
    }

    private void startThread(Runnable runnable) {
        stopThread();
        if (thread == null) {
            thread = new Thread(runnable);
        }
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

    private void sendMessage(int what) {
        if (handler == null) {
            return;
        }
        message = handler.obtainMessage();
        message.what = what;
        message.sendToTarget();
    }

    private void orientationBasedUI() {
        final WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (adapter != null) {
            int size = metrics.widthPixels / 2;
            adapter.setLayoutParams(size);
        }
    }

}
