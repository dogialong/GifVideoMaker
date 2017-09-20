package pl.droidsonroids.gif;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

class InvalidationHandlerCustom extends Handler {

    static final int MSG_TYPE_INVALIDATION = -1;

    private final WeakReference<GifDrawableCustom> mDrawableRef;

    public InvalidationHandlerCustom(final GifDrawableCustom gifDrawable) {
        super(Looper.getMainLooper());
        mDrawableRef = new WeakReference<>(gifDrawable);
    }

    @Override
    public void handleMessage(final Message msg) {
        final GifDrawableCustom gifDrawable = mDrawableRef.get();
        if (gifDrawable == null) {
            return;
        }
        if (msg.what == MSG_TYPE_INVALIDATION) {
            gifDrawable.invalidateSelf();
        } else {
            for (AnimationListener listener : gifDrawable.mListeners) {
                listener.onAnimationCompleted(msg.what);
            }
        }
    }
}
