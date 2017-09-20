/*
Copyright 2015 Alex Florescu
Copyright 2014 Stephan Tittel and Yahoo Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.gifmaker.gifeditor.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.gifmaker.gifeditor.R;
import com.rey.material.widget.ImageView;

import java.math.BigDecimal;
import java.util.List;

public class RangeSeekBar<T extends Number> extends ImageView {
    private List<OnRangeSeekBarListener> mListeners;
    public static final int ACTIVE_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5);
    public static final int INVALID_POINTER_ID = 255;
    public static final int ACTION_POINTER_INDEX_MASK = 0x0000ff00, ACTION_POINTER_INDEX_SHIFT = 8;
    public static final Integer DEFAULT_MINIMUM = 0;
    public static final Integer DEFAULT_MAXIMUM = 100;
    public static final Integer DEFAULT_STEP = 1;
    private static final int LINE_HEIGHT_IN_DP = 1;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected T absoluteMinValue, absoluteMaxValue, absoluteStepValue;
    protected NumberType numberType;
    protected double absoluteMinValuePrim, absoluteMaxValuePrim, absoluteStepValuePrim;
    protected double normalizedMinValue = 0d;
    protected double normalizedMaxValue = 1d;
    protected double minDeltaForDefault = 0;
    private Thumb pressedThumb = null;
    private boolean notifyWhileDragging = false;
    private OnRangeSeekBarChangeListener<T> listenerOnRangeSeekBarChangeListener;
    private OnRangeSeekBarListener listenerOnRangeSeekBarListener;
    private float downMotionX;
    private int activePointerId = INVALID_POINTER_ID;
    private int scaledTouchSlop;
    private boolean isDragging;
    private RectF rect;
    private boolean alwaysActive;
    private int activeColor;
    private int defaultColor;
    private boolean activateOnDefaultValues;
    private int width;
    private int height;
    private double distanceDpPerSecond;
    private double valuePerSecond;
    private static int TIME_LIMIT = 5;
    private boolean isCheckFirstInit = false;

    public RangeSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @SuppressWarnings("unchecked")
    private T extractNumericValueFromAttributes(TypedArray a, int attribute, int defaultValue) {
        TypedValue tv = a.peekValue(attribute);
        if (tv == null) {
            return (T) Integer.valueOf(defaultValue);
        }
        int type = tv.type;
        if (type == TypedValue.TYPE_FLOAT) {
            return (T) Float.valueOf(a.getFloat(attribute, defaultValue));
        } else {
            return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
        }
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs == null) {
            setRangeToDefaultValues();
            activeColor = ACTIVE_COLOR;
            defaultColor = getResources().getColor(R.color.yellow);
            alwaysActive = false;
            activateOnDefaultValues = false;
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);
            try {
                setRangeValues(
                        extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMinValue, DEFAULT_MINIMUM),
                        extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM),
                        extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_step, DEFAULT_STEP)
                );
                activeColor = a.getColor(R.styleable.RangeSeekBar_activeColor, ACTIVE_COLOR);
                defaultColor =getResources().getColor(R.color.yellow);
                alwaysActive = a.getBoolean(R.styleable.RangeSeekBar_alwaysActive, false);

                activateOnDefaultValues = a.getBoolean(R.styleable.RangeSeekBar_activateOnDefaultValues, false);
            } finally {
                a.recycle();
            }
        }
        setValuePrimAndNumberType();
        rect = new RectF(0, 0, getWidth(), 0);
        setFocusable(true);
        setFocusableInTouchMode(true);
        scaledTouchSlop = 30;

    }

    public void setRangeValues(T minValue, T maxValue) {
        this.absoluteMinValue = minValue;
        this.absoluteMaxValue = maxValue;
        setValuePrimAndNumberType();
    }

    public void setRangeValues(T minValue, T maxValue, T step) {
        this.absoluteStepValue = step;
        setRangeValues(minValue, maxValue);
    }

    public void setValuePerSeconde(double value) {
        valuePerSecond = (double) TIME_LIMIT / value;
    }

    @SuppressWarnings("unchecked")
    private void setRangeToDefaultValues() {
        this.absoluteMinValue = (T) DEFAULT_MINIMUM;
        this.absoluteMaxValue = (T) DEFAULT_MAXIMUM;
        this.absoluteStepValue = (T) DEFAULT_STEP;
        setValuePrimAndNumberType();
    }

    private void setValuePrimAndNumberType() {
        absoluteMinValuePrim = absoluteMinValue.doubleValue();
        absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
        absoluteStepValuePrim = absoluteStepValue.doubleValue();
        numberType = NumberType.fromNumber(absoluteMinValue);
    }

    @SuppressWarnings("unused")
    public void resetSelectedValues() {
        setSelectedMinValue(absoluteMinValue);
        setSelectedMaxValue(absoluteMaxValue);
    }

    @SuppressWarnings("unused")
    public boolean isNotifyWhileDragging() {
        return notifyWhileDragging;
    }

    @SuppressWarnings("unused")
    public void setNotifyWhileDragging(boolean flag) {
        this.notifyWhileDragging = flag;
    }

    public T getAbsoluteMinValue() {
        return absoluteMinValue;
    }


    public T getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    @SuppressWarnings("unchecked")
    private T roundOffValueToStep(T value) {
        double d = Math.round(value.doubleValue() / absoluteStepValuePrim) * absoluteStepValuePrim;
        return (T) numberType.toNumber(Math.max(absoluteMinValuePrim, Math.min(absoluteMaxValuePrim, d)));
    }

    public T getSelectedMinValue() {
        return roundOffValueToStep(normalizedToValue(normalizedMinValue));
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setSelectedMinValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMinValue(0d);
        } else {
            setNormalizedMinValue(valueToNormalized(value));
        }
    }

    public T getSelectedMaxValue() {
        return roundOffValueToStep(normalizedToValue(normalizedMaxValue));
    }

    public void setSelectedMaxValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (5 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMaxValue(1d);
        } else {
            setNormalizedMaxValue(valueToNormalized(value));
        }
    }

    @SuppressWarnings("unused")
    public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener) {
        this.listenerOnRangeSeekBarChangeListener = listener;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        int pointerIndex;
        final int action = event.getAction();
        distanceDpPerSecond = (double) ((TIME_LIMIT * getWidth()) / getAbsoluteMaxValue().intValue());

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = event.getPointerId(event.getPointerCount() - 1);
                pointerIndex = event.findPointerIndex(activePointerId);
                downMotionX = event.getX(pointerIndex);
                pressedThumb = evalPressedThumb(downMotionX);
                setPressed(false);
                attemptClaimDrag();
                break;

            case MotionEvent.ACTION_MOVE:
                if (pressedThumb != null) {
                    if (isDragging) {
                        invalidate();
                        trackTouchEvent(event);
                    } else {
                        pointerIndex = event.findPointerIndex(activePointerId);
                        final float x = event.getX(pointerIndex);
                        if (Math.abs(x - downMotionX) > scaledTouchSlop) {
                            setPressed(true);
                            invalidate();
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            attemptClaimDrag();
                        }

                    }
                    if (listenerOnRangeSeekBarChangeListener != null) {
                        listenerOnRangeSeekBarChangeListener.onRangeSeekBarRealtimeValuesChange(this, getSelectedMinValue(), getSelectedMaxValue());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                onStopTrackingTouch();
                pressedThumb = null;
//                invalidate();
                if (listenerOnRangeSeekBarChangeListener != null) {
                    listenerOnRangeSeekBarChangeListener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = event.getPointerCount() - 1;
                downMotionX = event.getX(index);
                activePointerId = event.getPointerId(index);
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;

        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == activePointerId) {
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            downMotionX = ev.getX(newPointerIndex);
            activePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        final int pointerIndex = event.findPointerIndex(activePointerId);
        final float x = event.getX(pointerIndex);
        if (Thumb.MIN.equals(pressedThumb)) {
            setNormalizedMinValue(screenToNormalized(x));
        } else if (Thumb.MAX.equals(pressedThumb)) {
            setNormalizedMaxValue(screenToNormalized(x));
        }
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    void onStartTrackingTouch() {
        isDragging = true;
    }

    void onStopTrackingTouch() {
        isDragging = false;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (normalizedMaxValue >= 1 && normalizedMinValue <= 0.0) {
            defaultColor = getResources().getColor(R.color.yellow);
        } else {
            defaultColor = Color.GRAY;
        }
        paint.setStyle(Style.STROKE);
        paint.setColor(defaultColor);
        paint.setAntiAlias(true);

        rect.left = 0;
        rect.right = getWidth();
        rect.top = 0;
        rect.bottom = getHeight();
        canvas.drawRect(rect, paint);

        boolean selectedValuesAreDefault = (normalizedMinValue <= minDeltaForDefault && normalizedMaxValue >= 1 - minDeltaForDefault);

        int colorToUseForButtonsAndHighlightedLine = !alwaysActive && !activateOnDefaultValues && selectedValuesAreDefault ?
                defaultColor : // default values
                activeColor;   // non default, filter is active

        rect.left = normalizedToScreen(normalizedMaxValue);
        rect.right = getWidth();
        rect.top = 0;
        rect.bottom = getHeight();
        paint.setColor(defaultColor);
        canvas.drawRect(rect, paint);

        rect.left = normalizedToScreen(normalizedMinValue);
        rect.right = normalizedToScreen(normalizedMaxValue);
        rect.top = 0;
        rect.bottom = getHeight();
        paint.setColor(colorToUseForButtonsAndHighlightedLine);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawRect(rect, paint);

        rect.left = normalizedToScreen(normalizedMinValue);
        rect.right = normalizedToScreen(normalizedMinValue) + 20;
        rect.top = 0;
        rect.bottom = getHeight();
        paint.setColor(getResources().getColor(R.color.yellow));
        paint.setStyle(Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawRect(rect, paint);

        paint.setColor(getResources().getColor(R.color.white));
        paint.setTextSize(15);

        rect.left = normalizedToScreen(normalizedMaxValue) - 20;
        rect.right = normalizedToScreen(normalizedMaxValue) + 0;
        rect.top = 0;
        rect.bottom = getHeight();
        paint.setColor(getResources().getColor(R.color.yellow));
        paint.setStyle(Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawRect(rect, paint);

        paint.setColor(getResources().getColor(R.color.white));
        paint.setTextSize(15);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", normalizedMinValue);
        bundle.putDouble("MAX", normalizedMaxValue);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        final Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        normalizedMinValue = bundle.getDouble("MIN");
        normalizedMaxValue = bundle.getDouble("MAX");
    }

    private Thumb evalPressedThumb(float touchX) {
        Thumb result = null;
        boolean minThumbPressed = isInThumbRange(touchX, normalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue);
        if (minThumbPressed) {
            result = Thumb.MIN;
        } else if (maxThumbPressed) {
            result = Thumb.MAX;
        }
        return result;
    }

    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= 120;
    }

    public void setNormalizedMinValue(double value) {
        normalizedMinValue = Math.max(0d, Math.min(1d, Math.min(value, normalizedMaxValue)));
        defaultColor = Color.GRAY;
        if (pressedThumb == Thumb.MIN) {

            if (normalizedMaxValue - normalizedMinValue <= valuePerSecond) {
                normalizedMaxValue = normalizedMinValue + valuePerSecond;
                if (normalizedMaxValue >= 1d) {
                    normalizedMaxValue = 1d;
                }
            }
        }
        if (1d - normalizedMinValue >= valuePerSecond) {
            invalidate();
        } else {
            normalizedMinValue = 1d - valuePerSecond;
            invalidate();
        }


    }

    public void setNormalizedMaxValue(double value) {
        normalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, normalizedMinValue)));
        defaultColor = Color.GRAY;
        if (pressedThumb == Thumb.MAX) {
            if (normalizedMaxValue - normalizedMinValue <= valuePerSecond) {
                normalizedMinValue = normalizedMaxValue - valuePerSecond;
                if (normalizedMinValue <= 0d) {
                    normalizedMinValue = 0d;
                }
            }
        }
        if (normalizedMaxValue <= valuePerSecond) {
            normalizedMaxValue = valuePerSecond;
            invalidate();
        } else {
            invalidate();
        }
    }

    protected T normalizedToValue(double normalized) {
        double v = absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim);
        // TODO parameterize this rounding to allow variable decimal points
        return (T) numberType.toNumber(Math.round(v * 100) / 100d);
    }

    protected double valueToNormalized(T value) {
        if (0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
            return 0d;
        }
        return (value.doubleValue() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim);
    }

    private float normalizedToScreen(double normalizedCoord) {
        return (float) (normalizedCoord * getWidth());
    }

    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        if (width <= 2 * 0) {
            // prevent division by zero, simply return 0.
            return 0d;
        } else {
            double result = screenCoord / width;
            return Math.min(1d, Math.max(0d, result));
        }
    }

    public enum Thumb {
        MIN, MAX
    }

    protected enum NumberType {
        LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

        public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
            if (value instanceof Long) {
                return LONG;
            }
            if (value instanceof Double) {
                return DOUBLE;
            }
            if (value instanceof Integer) {
                return INTEGER;
            }
            if (value instanceof Float) {
                return FLOAT;
            }
            if (value instanceof Short) {
                return SHORT;
            }
            if (value instanceof Byte) {
                return BYTE;
            }
            if (value instanceof BigDecimal) {
                return BIG_DECIMAL;
            }
            throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
        }

        public Number toNumber(double value) {
            switch (this) {
                case LONG:
                    return (long) value;
                case DOUBLE:
                    return value;
                case INTEGER:
                    return (int) value;
                case FLOAT:
                    return (float) value;
                case SHORT:
                    return (short) value;
                case BYTE:
                    return (byte) value;
                case BIG_DECIMAL:
                    return BigDecimal.valueOf(value);
            }
            throw new InstantiationError("can't convert " + this + " to a Number object");
        }
    }

    public interface OnRangeSeekBarChangeListener<T extends Number> {
        void onRangeSeekBarValuesChanged(RangeSeekBar<T> bar, T minValue, T maxValue);

        void onRangeSeekBarRealtimeValuesChange(RangeSeekBar<T> bar, T minvalue, T maxValue);
    }

}
