package com.gabrielcoman.simplexmediaplayer.aux.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class SimplexDrawable extends Drawable {

    // instance members
    private final float mCornerRadius;
    private final RectF mRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mPaint;
    private final int mMargin;

    /**
     * Constructor that takes a bitmap, radius and margin
     *
     * @param bitmap        current bitmap to paint
     * @param cornerRadius  the corner radius to add
     * @param margin        the margin to add
     */
    SimplexDrawable(Bitmap bitmap, float cornerRadius, int margin) {
        mCornerRadius = cornerRadius;
        mMargin = margin;
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);
    }

    /**
     * Overridden Drawable method that repaints the image when the bounds change
     *
     * @param bounds a Rect of bounds
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);
    }

    /**
     * Overridden Drawable method that draws on a canvas
     *
     * @param canvas the current canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
    }

    /**
     * Overridden Drawable method that gets the opacity
     *
     * @return a type of pixel format
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * Overridden Drawable method that sets the transparency
     *
     * @param alpha the current alpha blending factor
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * Overidden Drawable method that sets the color filter
     *
     * @param cf the color filter
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
}
