package com.gabrielcoman.simplexmediaplayer.aux.style;

import android.graphics.Color;

public class SimplexStyle {

    private int     indicatorBackgroundBgColor      = Color.TRANSPARENT;

    private int     indicatorCurrentTimeBgColor     = Color.TRANSPARENT;
    private int     indicatorCurrentTimeTxtColor    = Color.WHITE;

    private int     indicatorTotalTimeBgColor       = Color.TRANSPARENT;
    private int     indicatorTotalTimeTxtColor      = Color.WHITE;

    private int     indicatorLengthBgColor          = Color.LTGRAY;
    private float   indicatorLengthAlpha            = 0.5F;
    private int     indicatorBufferBgColor          = Color.WHITE;
    private float   indicatorBufferAlpha            = 0.5F;
    private int     indicatorPlaybackBgColor        = Color.RED;

    public static SimplexStyle normalStyle () {
        return new SimplexStyle();
    }

    public static SimplexStyle testStyle () {
        SimplexStyle style = new SimplexStyle();
        style.indicatorBackgroundBgColor = Color.BLUE;
        style.indicatorCurrentTimeBgColor = Color.MAGENTA;
        style.indicatorTotalTimeBgColor = Color.MAGENTA;
        return style;
    }

    public int getIndicatorBackgroundBgColor() {
        return indicatorBackgroundBgColor;
    }

    public void setIndicatorBackgroundBgColor(int indicatorBackgroundBgColor) {
        this.indicatorBackgroundBgColor = indicatorBackgroundBgColor;
    }

    public int getIndicatorCurrentTimeBgColor() {
        return indicatorCurrentTimeBgColor;
    }

    public void setIndicatorCurrentTimeBgColor(int indicatorCurrentTimeBgColor) {
        this.indicatorCurrentTimeBgColor = indicatorCurrentTimeBgColor;
    }

    public int getIndicatorCurrentTimeTxtColor() {
        return indicatorCurrentTimeTxtColor;
    }

    public void setIndicatorCurrentTimeTxtColor(int indicatorCurrentTimeTxtColor) {
        this.indicatorCurrentTimeTxtColor = indicatorCurrentTimeTxtColor;
    }

    public int getIndicatorTotalTimeBgColor() {
        return indicatorTotalTimeBgColor;
    }

    public void setIndicatorTotalTimeBgColor(int indicatorTotalTimeBgColor) {
        this.indicatorTotalTimeBgColor = indicatorTotalTimeBgColor;
    }

    public int getIndicatorTotalTimeTxtColor() {
        return indicatorTotalTimeTxtColor;
    }

    public void setIndicatorTotalTimeTxtColor(int indicatorTotalTimeTxtColor) {
        this.indicatorTotalTimeTxtColor = indicatorTotalTimeTxtColor;
    }

    public int getIndicatorLengthBgColor() {
        return indicatorLengthBgColor;
    }

    public void setIndicatorLengthBgColor(int indicatorLengthBgColor) {
        this.indicatorLengthBgColor = indicatorLengthBgColor;
    }

    public int getIndicatorBufferBgColor() {
        return indicatorBufferBgColor;
    }

    public void setIndicatorBufferBgColor(int indicatorBufferBgColor) {
        this.indicatorBufferBgColor = indicatorBufferBgColor;
    }

    public int getIndicatorPlaybackBgColor() {
        return indicatorPlaybackBgColor;
    }

    public void setIndicatorPlaybackBgColor(int indicatorPlaybackBgColor) {
        this.indicatorPlaybackBgColor = indicatorPlaybackBgColor;
    }

    public float getIndicatorLengthAlpha() {
        return indicatorLengthAlpha;
    }

    public void setIndicatorLengthAlpha(float indicatorLengthAlpha) {
        this.indicatorLengthAlpha = indicatorLengthAlpha;
    }

    public float getIndicatorBufferAlpha() {
        return indicatorBufferAlpha;
    }

    public void setIndicatorBufferAlpha(float indicatorBufferAlpha) {
        this.indicatorBufferAlpha = indicatorBufferAlpha;
    }
}
