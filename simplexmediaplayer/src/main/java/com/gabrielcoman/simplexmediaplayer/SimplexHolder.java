/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * This is an extension to FrameLayout that acts as support for the Simplex video player
 * and the Simplex controller.
 * It also adds a listener to check when the layout has really changed and inform the main
 * Simplex player class of the new width & height that it must adapt to.
 */
public class SimplexHolder extends FrameLayout {

    // the old width & height of the frame layout. If these don't change, then a layout
    // change did not actually occur
    private int      oldWidth  = 0;
    private int      oldHeight = 0;

    // an instance of the Listener to send callback messages back to
    // the Simplex player
    private Listener listener;

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    public SimplexHolder(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    public SimplexHolder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    public SimplexHolder(Context context, AttributeSet attrs, int defStyleAttr) {

        // call to super
        super(context, attrs, defStyleAttr);

        // create a new default instance of the listener so that it's always not null
        // and we don't have to null-check every time we want to use it
        listener = new Listener() {
            @Override public void didChangeLayout(int newWidth, int newHeight) {}};

        // add a new view tree observer to listen to layout changes triggered by Android
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // get the current measured width & height
                int newWidth = getMeasuredWidth();
                int newHeight = getMeasuredHeight();

                // find out if the listener should trigger
                boolean shouldTrigger = shouldTriggerLayoutChange(newWidth, newHeight);

                // if so:
                if (shouldTrigger) {

                    // update the old width & height with new values
                    oldWidth = newWidth;
                    oldHeight = newHeight;

                    // and trigger the listener
                    listener.didChangeLayout(newWidth, newHeight);
                }
            }
        });

    }

    /**
     * Method that finds out if the SimplexHolder class should trigger the listener informing
     * the parent (the SimplexPlayer) that a new layout change has occurred.
     *
     * @param newWidth  new measured width of the holder
     * @param newHeight new measured height of the holder
     * @return          true of the width or height have changed, false otherwise
     */
    public boolean shouldTriggerLayoutChange (int newWidth, int newHeight) {
        return newWidth != oldWidth || newHeight != oldHeight;
    }

    /**
     * Methods that sets a new value for the Listener listener instance
     *
     * @param listener a new instance of Listener
     */
    public void setListener (Listener listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    /**
     * Listener that any class that wants to receive callbacks from the SimplexHolder class
     * must implement.
     */
    public interface Listener {

        /**
         * Method to implement by the class. Called when an actual layout changes has happened.
         *
         * @param newWidth  the new width of the holder
         * @param newHeight the new height of the holder
         */
        void didChangeLayout(int newWidth, int newHeight);
    }
}
