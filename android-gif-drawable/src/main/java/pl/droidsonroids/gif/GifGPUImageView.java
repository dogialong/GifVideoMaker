package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 * Created by mr.logic on 8/19/2017.
 */

public class GifGPUImageView extends GPUImageView {

    private GifDrawableRender mGifDrawableRender;

    public GifGPUImageView(Context context) {
        super(context);
    }

    public GifGPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private GifDrawableRender.OnPreviewDraw mOnPreviewDraw = new GifDrawableRender.OnPreviewDraw() {
        @Override
        public void onDraw(Bitmap bitmap) {
            setImage(bitmap);
        }
    };


    public void setImageFromePathFile(String pathFileImageGif) {
        if (mGifDrawableRender != null) {
            mGifDrawableRender.reset();
        }
        try {
            mGifDrawableRender = new GifDrawableRender(pathFileImageGif);
            mGifDrawableRender.setOnPreviewDraw(mOnPreviewDraw);
            mGifDrawableRender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        if (mGifDrawableRender != null) {
            return mGifDrawableRender.isPlaying();
        } else {
            return false;
        }
    }

    public void pause() {
        if (mGifDrawableRender != null)
            mGifDrawableRender.pause();
    }

    public void resume() {
        if (mGifDrawableRender != null)
            mGifDrawableRender.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

    }

    public void setSpeed(float speed) {
        mGifDrawableRender.setSpeed(speed);

    }

    public void recycle() {
        if (mGifDrawableRender != null)
            mGifDrawableRender.recycle();
    }


    static class GifDrawableRender extends GifDrawableCustom {

        private OnPreviewDraw mOnPreviewDraw;
        private String TAG = GifDrawableRender.class.getSimpleName();


        public void setOnPreviewDraw(OnPreviewDraw onPreviewDraw) {
            mOnPreviewDraw = onPreviewDraw;
        }

        public GifDrawableRender(@NonNull Resources res, @RawRes @DrawableRes int id) throws Resources.NotFoundException, IOException {
            super(res, id);
        }

        public GifDrawableRender(@NonNull AssetManager assets, @NonNull String assetName) throws IOException {
            super(assets, assetName);
        }

        public GifDrawableRender(@NonNull String filePath) throws IOException {
            super(filePath);
        }

        public GifDrawableRender(@NonNull File file) throws IOException {
            super(file);
        }

        public GifDrawableRender(@NonNull InputStream stream) throws IOException {
            super(stream);
        }

        public GifDrawableRender(@NonNull AssetFileDescriptor afd) throws IOException {
            super(afd);
        }

        public GifDrawableRender(@NonNull FileDescriptor fd) throws IOException {
            super(fd);
        }

        public GifDrawableRender(@NonNull byte[] bytes) throws IOException {
            super(bytes);
        }

        public GifDrawableRender(@NonNull ByteBuffer buffer) throws IOException {
            super(buffer);
        }

        public GifDrawableRender(@Nullable ContentResolver resolver, @NonNull Uri uri) throws IOException {
            super(resolver, uri);
        }

        protected GifDrawableRender(@NonNull InputSource inputSource, @Nullable GifDrawableCustom oldDrawable, @Nullable ScheduledThreadPoolExecutor executor, boolean isRenderingTriggeredOnDraw, @NonNull GifOptions options) throws IOException {
            super(inputSource, oldDrawable, executor, isRenderingTriggeredOnDraw, options);
        }

        GifDrawableRender(GifInfoHandle gifInfoHandle, GifDrawableCustom oldDrawable, ScheduledThreadPoolExecutor executor, boolean isRenderingTriggeredOnDraw) {
            super(gifInfoHandle, oldDrawable, executor, isRenderingTriggeredOnDraw);
        }


        @Override
        public void draw(@NonNull Canvas canvas) {
            super.draw(canvas);

            if (mOnPreviewDraw != null)
                mOnPreviewDraw.onDraw(mBuffer);

        }

        public static interface OnPreviewDraw {
            public void onDraw(Bitmap bitmap);
        }
    }

}
