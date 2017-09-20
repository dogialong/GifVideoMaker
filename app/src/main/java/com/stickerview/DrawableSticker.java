package com.stickerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {
  private Bitmap bitmap;
  private Drawable drawable;
  private Rect realBounds;
  private Context context;

  public DrawableSticker(Context context,Bitmap bitmap) {
    this.bitmap = bitmap;
    this.context = context;
    this.drawable =new BitmapDrawable(context.getResources(),bitmap);
    realBounds = new Rect(0, 0, getWidth(), getHeight());
  }

  @NonNull
  @Override
  public Drawable getDrawable() {
    return drawable;
  }

  @Override
  public DrawableSticker setDrawable(@NonNull Drawable drawable) {
    this.drawable = drawable;
    return this;
  }
  public DrawableSticker setBitmap(Bitmap bitmap){
    this.bitmap = bitmap;
    return this;
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    canvas.save();
    canvas.concat(getMatrix());
    drawable.setBounds(realBounds);
    drawable.draw(canvas);
    canvas.restore();
  }

  @NonNull
  @Override
  public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    drawable.setAlpha(alpha);
    return this;
  }

  @Override
  public int getWidth() {
    return drawable.getIntrinsicWidth();
  }

  @Override
  public int getHeight() {
    return drawable.getIntrinsicHeight();
  }

  @Override
  public void release() {
    super.release();
    if (drawable != null) {
      drawable = null;
    }
  }
}
