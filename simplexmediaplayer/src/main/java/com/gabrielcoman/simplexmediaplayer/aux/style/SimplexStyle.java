package com.gabrielcoman.simplexmediaplayer.aux.style;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.gabrielcoman.simplexmediaplayer.aux.image.SimplexBitmap;

public class SimplexStyle {

    private boolean hasFullscreenMask = true;
    private int fullscreenMaskBgColor = Color.BLACK;
    private float fullscreenMaskAlpha = 0.25F;

    private Bitmap buttonPlaybackPlayBitmap = SimplexBitmap.createPlayButtonBitmap();
    private Bitmap buttonPlaybackReplayBitmap = SimplexBitmap.createReplayButtonBitmap();

    private Bitmap bottomGradientMaskBitmap = SimplexBitmap.createVideoGradientBitmap();

    private int indicatorBackgroundBgColor = Color.TRANSPARENT;

    private int indicatorCurrentTimeBgColor = Color.TRANSPARENT;
    private int indicatorCurrentTimeTxtColor = Color.WHITE;

    private int indicatorTotalTimeBgColor = Color.TRANSPARENT;
    private int indicatorTotalTimeTxtColor = Color.WHITE;

    private int indicatorLengthBgColor = Color.LTGRAY;
    private float indicatorLengthAlpha = 0.5F;
    private int indicatorBufferBgColor = Color.WHITE;
    private float indicatorBufferAlpha = 0.5F;
    private int indicatorPlaybackBgColor = Color.RED;

    public static SimplexStyle normalStyle() {
        return new SimplexStyle();
    }

    public static SimplexStyle testStyle() {
        SimplexStyle style = new SimplexStyle();
        style.indicatorBackgroundBgColor = Color.BLUE;
        style.indicatorCurrentTimeBgColor = Color.MAGENTA;
        style.indicatorTotalTimeBgColor = Color.MAGENTA;
        return style;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int getIndicatorBackgroundBgColor() {
        return indicatorBackgroundBgColor;
    }

    public int getIndicatorCurrentTimeBgColor() {
        return indicatorCurrentTimeBgColor;
    }

    public int getIndicatorCurrentTimeTxtColor() {
        return indicatorCurrentTimeTxtColor;
    }

    public int getIndicatorTotalTimeBgColor() {
        return indicatorTotalTimeBgColor;
    }

    public int getIndicatorTotalTimeTxtColor() {
        return indicatorTotalTimeTxtColor;
    }

    public int getIndicatorLengthBgColor() {
        return indicatorLengthBgColor;
    }

    public int getIndicatorBufferBgColor() {
        return indicatorBufferBgColor;
    }

    public int getIndicatorPlaybackBgColor() {
        return indicatorPlaybackBgColor;
    }

    public float getIndicatorLengthAlpha() {
        return indicatorLengthAlpha;
    }

    public float getIndicatorBufferAlpha() {
        return indicatorBufferAlpha;
    }

    public boolean isHasFullscreenMask() {
        return hasFullscreenMask;
    }

    public int getFullscreenMaskBgColor() {
        return fullscreenMaskBgColor;
    }

    public float getFullscreenMaskAlpha() {
        return fullscreenMaskAlpha;
    }

    public Bitmap getButtonPlaybackPlayBitmap() {
        return buttonPlaybackPlayBitmap;
    }

    public Bitmap getButtonPlaybackReplayBitmap() {
        return buttonPlaybackReplayBitmap;
    }

    public Bitmap getBottomGradientMaskBitmap() {
        return bottomGradientMaskBitmap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Public setters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setTotalLengthIndicatorBackground (int color) {
        indicatorLengthBgColor =  color;
    }

    public void setTotalLengthIndicatorAlpha (float alpha) {
        indicatorLengthAlpha = alpha;
    }

    public void setBufferLengthIndicatorBackground (int color) {
        indicatorBufferBgColor = color;
    }

    public void setBufferLengthIndicatorAlpha (float alpha) {
        indicatorBufferAlpha = alpha;
    }

    public void setPlaybackLengthIndicatorBackground (int color) {
        indicatorPlaybackBgColor = color;
    }

    public void setPlayButtonBitmap (Bitmap bitmap) {
        buttonPlaybackPlayBitmap = bitmap;
    }

    public void setReplayButtonBitmap (Bitmap bitmap) {
        buttonPlaybackReplayBitmap = bitmap;
    }


}
