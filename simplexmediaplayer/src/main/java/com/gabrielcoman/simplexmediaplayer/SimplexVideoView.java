/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.MalformedJsonException;
import android.widget.FrameLayout;
import android.widget.VideoView;

/**
 * This class subclasses the normal Android VideoView and adds two main traits:
 * - member variables to hold the width & height of the video that it's trying to display
 * - a method that re-sizes the video view so that the aspect ratio is preserved
 */
public class SimplexVideoView extends VideoView {

    // class member variables that hold the width & height of the video that's being rendered
    private int     videoWidth  = 0;
    private int     videoHeight = 0;

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    public SimplexVideoView(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    public SimplexVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    public SimplexVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Setter method for the video width & height together
     *
     * @param videoWidth    new video width
     * @param videoHeight   new video height
     */
    public void setVideoSize (int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
    }

    /**
     * Method that does the math for the video view to update its layout parameters in order
     * for it to maintain a correct aspect ratio in regards to the container width & height
     *
     * @param containerWidth    the width of the container the video is played in
     * @param containerHeight   the height of the container the video is played in
     */
    public void resizeToContainer (int containerWidth, int containerHeight) {

        // calc the aspect ratio of the video
        float videoRatio = calcRatio(videoWidth, videoHeight);

        // calc the aspect ratio of the container
        float containerRatio = calcRatio(containerWidth, containerHeight);

        // these variables will hold the new width & height of the video view so as to respect
        // the video aspect ratio as well as the new X and Y margins
        float X, Y, W, H;

        // case 1
        if (videoRatio > containerRatio) {
            W = containerWidth;
            H = W / videoRatio;
            X = 0.0F;
            Y = (containerHeight - H) / 2.0F;
        }
        // case 2
        else {
            H = containerHeight;
            W = videoRatio * H;
            Y = 0.0F;
            X = (containerWidth - W) / 2.0F;
        }

        // now generate a new frame layout for this
        FrameLayout.LayoutParams newParams = new FrameLayout.LayoutParams((int)W, (int)H);
        newParams.setMargins((int)X, (int)Y, 0, 0);

        // and update
        setLayoutParams(newParams);
    }

    /**
     * Method that safely calculates a ration between a width and a height, making sure there
     * is no division by zero
     *
     * @param width  a width to calculate an aspect ratio with
     * @param height a height to calculate an aspect ratio with
     * @return       the aspect ratio between the given width and height
     */
    public float calcRatio (int width, int height) {
        return Math.abs(videoHeight != 0 ? width / (float) height : 1);
    }
}
