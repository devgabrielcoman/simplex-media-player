/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.MalformedJsonException;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import android.widget.VideoView;

/**
 * This class subclasses the normal Android VideoView and adds two main traits:
 * - member variables to hold the width & height of the video that it's trying to display
 * - a method that re-sizes the video view so that the aspect ratio is preserved
 */
public class SimplexVideoView extends VideoView implements
    SurfaceHolder.Callback
{

    // class member variables that hold the width & height of the video that's being rendered
    private int     videoWidth  = 0;
    private int     videoHeight = 0;

    // internal state variable that keeps track of the video view being prepared for
    // the first time
    private boolean isSurfaceCreated = false;

    // listener of type SimplexVideoViewListener
    private SimplexVideoViewInterface listener;

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

        // call to super
        super(context, attrs, defStyleAttr);

        // set holder callback to this
        getHolder().addCallback(this);

        // instantiate this so it's never null
        listener = new SimplexVideoViewInterface() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {}
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}};
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Surface.Callback listener implementation
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Overridden SimplexVideoView SurfaceHolder.Callback listener method that will get called
     * every time the SimplexVideoView surface gets re-created.
     *
     * @param holder current surface holder to feed into the media player
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // set the state "isPrepared" value to "true"
        isSurfaceCreated = true;

        // call listener
        listener.surfaceCreated(holder);

    }

    /**
     * Overridden SimplexVideoView SurfaceHolder.Callback listener method that will get called
     * every time the SimplexVideoView surface gets changed.
     * Not implemented.
     *
     * @param holder current surface holder
     * @param format format
     * @param width  new width
     * @param height new height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    /**
     * Overridden SimplexVideoView SurfaceHolder.Callback listener method that will get called
     * every time the SimplexVideoView surface gets destroyed.
     * This usually happens on an orientation change or when the activity gets put in the
     * background by another one.
     *
     * @param holder current surface holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        // call to the listener
        listener.surfaceDestroyed(holder);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Setters & Getters for state vars
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter for the "isPrepared" state variable
     *
     * @return the current value of "isPrepared"
     */
    public boolean isSurfaceCreated () {
        return isSurfaceCreated;
    }

    /**
     * Setter for the SimplexVideoViewInterface listener
     *
     * @param listener a new, non-null (hopefully) instance
     */
    public void setListener (SimplexVideoViewInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    /**
     * Interface that needs to be implemented by the SimplexPlayer in order to get callbacks
     * from the Simplex Video View about when the surface is created or destroyed
     *
     */
    interface SimplexVideoViewInterface {

        /**
         * Called when the surface is created
         *
         * @param holder current surface holder
         */
        void surfaceCreated (SurfaceHolder holder);

        /**
         * Called when the surface is destroyed
         *
         * @param holder current surface holder
         */
        void surfaceDestroyed (SurfaceHolder holder);

    }
}
