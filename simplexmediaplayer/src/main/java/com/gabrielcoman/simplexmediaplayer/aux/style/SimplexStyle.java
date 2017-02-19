/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.aux.style;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.gabrielcoman.simplexmediaplayer.aux.image.SimplexBitmap;

/**
 * Class that holds player style parameters like buffer indicator color, alpha, bitmaps, etc
 */
public class SimplexStyle {

    // member vars that determine how the semi-transparent mask that covers the screen
    // when the video is paused will actually look like
    private int     fullscreenMaskBgColor        = Color.BLACK;
    private float   fullscreenMaskAlpha          = 0.25F;

    // member vars that hold bitmaps for the play & replay buttons
    private Bitmap  buttonPlaybackPlayBitmap     = SimplexBitmap.createPlayButtonBitmap();
    private Bitmap  buttonPlaybackReplayBitmap   = SimplexBitmap.createReplayButtonBitmap();

    // member var that holds a bitmap for the bottom semi-translucent gradient
    private Bitmap  bottomGradientMaskBitmap     = SimplexBitmap.createVideoGradientBitmap();

    // member var that holds the default background color for the indicator holder
    private int     indicatorBackgroundBgColor   = Color.TRANSPARENT;

    // member vars that determine how the current time indicator will look like
    private int     indicatorCurrentTimeBgColor  = Color.TRANSPARENT;
    private int     indicatorCurrentTimeTxtColor = Color.WHITE;

    // member vars that determine how the total time indicator will look like
    private int     indicatorTotalTimeBgColor    = Color.TRANSPARENT;
    private int     indicatorTotalTimeTxtColor   = Color.WHITE;

    // member vars that determine how the length, buffer and playback bars will look like
    private int     indicatorLengthBgColor       = Color.LTGRAY;
    private float   indicatorLengthAlpha         = 0.5F;
    private int     indicatorBufferBgColor       = Color.WHITE;
    private float   indicatorBufferAlpha         = 0.5F;
    private int     indicatorPlaybackBgColor     = Color.RED;

    /**
     * Factory method that creates the default (normal) style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle defaultStyle () {
        return redStyle();
    }

    /**
     * Factory method that creates the red style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle redStyle () {
        SimplexStyle style = new SimplexStyle();
        style.indicatorPlaybackBgColor = Color.RED;
        return style;
    }

    /**
     * Factory method that creates the blue style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle blueStyle () {
        SimplexStyle style = new SimplexStyle();
        style.indicatorPlaybackBgColor = 0xFF669EF9;
        return style;
    }

    /**
     * Factory method that creates the green style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle greenStyle () {
        SimplexStyle style = new SimplexStyle();
        style.indicatorPlaybackBgColor = 0xFF6FCE85;
        return style;
    }

    /**
     * Factory method that creates the gray style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle grayStyle () {
        SimplexStyle style = new SimplexStyle();
        style.indicatorPlaybackBgColor = 0xFFC6C6C6;
        return style;
    }

    /**
     * Factory method that creates a test style used for debugging
     *
     * @return a new instance of simplex style
     */
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
