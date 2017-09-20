package pl.droidsonroids.gif;

/**
 * Builder for {@link GifDrawable} which can be used to construct new drawables
 * by reusing old ones.
 */
public class GifDrawableCustomBuilder extends  GifDrawableInit<GifDrawableCustomBuilder>{

    @Override
    protected GifDrawableCustomBuilder self() {
        return this;
    }
}
