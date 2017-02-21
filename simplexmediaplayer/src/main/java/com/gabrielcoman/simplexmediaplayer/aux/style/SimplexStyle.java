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

    // bitmaps
    private Bitmap playBitmap       = SimplexBitmap.createPlayButtonBitmap();
    private Bitmap replayBitmap     = SimplexBitmap.createReplayButtonBitmap();
    private Bitmap bottomMaskBitmap = SimplexBitmap.createVideoGradientBitmap();

    // different colors that can be changed
    private int     textColor       = Color.WHITE;
    private int     maskColor       = Color.BLACK;
    private int     lengthColor     = Color.LTGRAY;
    private int     bufferColor     = Color.WHITE;
    private int     playbackColor   = Color.RED;

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
        style.playbackColor = Color.RED;
        return style;
    }

    /**
     * Factory method that creates the blue style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle blueStyle () {
        SimplexStyle style = new SimplexStyle();
        style.playbackColor = 0xFF669EF9;
        return style;
    }

    /**
     * Factory method that creates the green style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle greenStyle () {
        SimplexStyle style = new SimplexStyle();
        style.playbackColor = 0xFF6FCE85;
        return style;
    }

    /**
     * Factory method that creates the gray style for the player
     *
     * @return a new instance of simplex style
     */
    public static SimplexStyle grayStyle () {
        SimplexStyle style = new SimplexStyle();
        style.playbackColor = 0xFFC6C6C6;
        return style;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int getLengthColor() {
        return lengthColor;
    }

    public int getBufferColor() {
        return bufferColor;
    }

    public int getPlaybackColor() {
        return playbackColor;
    }

    public int getMaskColor() {
        return maskColor;
    }

    public Bitmap getPlayBitmap() {
        return playBitmap;
    }

    public Bitmap getReplayBitmap() {
        return replayBitmap;
    }

    public Bitmap getBottomMaskBitmap() {
        return bottomMaskBitmap;
    }

    public int getTextColor() {
        return textColor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Public setters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setMaskColor (int color) {
        maskColor = color;
    }

    public void setLengthColor (int color) {
        lengthColor =  color;
    }

    public void setBufferColor (int color) {
        bufferColor = color;
    }

    public void setPlaybackColor (int color) {
        playbackColor = color;
    }

    public void setTextColor (int textColor) {
        this.textColor = textColor;
    }

    public void setPlayBitmap (Bitmap bitmap) {
        playBitmap = bitmap;
    }

    public void setReplayBitmap (Bitmap bitmap) {
        replayBitmap = bitmap;
    }



}
