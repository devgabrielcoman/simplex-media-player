/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;
import com.gabrielcoman.simplexmediaplayer.aux.time.SimplexTime;

import java.util.Locale;

/**
 * This class represents a custom TextView used to display video player time
 */
public class TimeIndicator extends TextView {

    // constants
    private final static int PADDING_HORZ = 15;
    private final static int PADDING_VERT = 10;

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    public TimeIndicator(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    public TimeIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    public TimeIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);
        updateText(new SimplexTime(0, 0, 0));
        setBackgroundColor(Color.TRANSPARENT);
        setPadding(PADDING_HORZ, PADDING_VERT, PADDING_HORZ, PADDING_VERT);
    }

    /**
     * Method that updates the style of the component
     *
     * @param style new style
     */
    public void updateStyle (SimplexStyle style) {
        if (style != null) {
            setTextColor(style.getTextColor());
        }
    }

    /**
     * Method that updates the text of a time indicator
     *
     * @param time current simplex time
     */
    public void updateText (SimplexTime time) {

        // create a new string builder
        StringBuilder buffer = new StringBuilder();

        // format and return
        if (time.getHour() > 0) {
            buffer.append(String.format(Locale.getDefault(), "%02d", time.getHour()))
                    .append(":")
                    .append(String.format(Locale.getDefault(), "%02d", time.getMinute()))
                    .append(":")
                    .append(String.format(Locale.getDefault(), "%02d", time.getSecond()));
        } else {
            buffer.append(String.format(Locale.getDefault(), "%02d", time.getMinute()))
                    .append(":")
                    .append(String.format(Locale.getDefault(), "%02d", time.getSecond()));
        }

        // update text
        setText(buffer.toString());
    }
}
