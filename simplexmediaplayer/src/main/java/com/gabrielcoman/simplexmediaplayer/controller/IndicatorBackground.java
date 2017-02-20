/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */

package com.gabrielcoman.simplexmediaplayer.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

/**
 * Subview of Relative Layout that represents a background for the controller playback part
 */
public class IndicatorBackground extends RelativeLayout {

    // subviews
    private ImageView    mask;
    private LinearLayout support;

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    public IndicatorBackground(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    public IndicatorBackground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    public IndicatorBackground(Context context, AttributeSet attrs, int defStyleAttr) {

        // call to super
        super(context, attrs, defStyleAttr);

        // create the bottom gradient mask
        mask = new ImageView(context);
        RelativeLayout.LayoutParams bottomGradientParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mask.setLayoutParams(bottomGradientParams);
        mask.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mask);

        // a linear layout child of the background, that will be used by time & playback indicators
        // as support
        support = new LinearLayout(context);
        LinearLayout.LayoutParams indicatorLinearParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        support.setOrientation(LinearLayout.HORIZONTAL);
        support.setLayoutParams(indicatorLinearParams);
        support.setBackgroundColor(Color.TRANSPARENT);
        addView(support);
    }

    /**
     * Method that updates the style of the component
     *
     * @param style new style
     */
    public void updateStyle (SimplexStyle style) {
        if (style != null) {
            mask.setImageBitmap(style.getBottomGradientMaskBitmap());
        }
    }

    /**
     * Getter for the linear indicator
     *
     * @return the support LinearLayout
     */
    public LinearLayout getSupport () {
        return support;
    }
}
