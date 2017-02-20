/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

/**
 * This class implements a playback button consisting of:
 * - a dark semi-transparent underlay
 * - an actual clickable button
 */
public class PlayButton extends RelativeLayout {

    // a copy of the style
    private SimplexStyle style;

    // subviews
    private View         mask;
    private ImageButton  play;

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    public PlayButton(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    public PlayButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {

        // call to super
        super(context, attrs, defStyleAttr);

        // create the fullscreen mask
        mask = new View(context);
        ViewGroup.LayoutParams fullscreenMaskParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mask.setLayoutParams(fullscreenMaskParams);
        mask.setVisibility(GONE);
        addView(mask);

        // create the play button
        play = new ImageButton(context);
        RelativeLayout.LayoutParams startButtonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        play.setLayoutParams(startButtonParams);
        play.setBackgroundColor(Color.TRANSPARENT);
        play.setImageBitmap(null);
        addView(play);
    }


    /**
     * Method that updates the style of the component
     *
     * @param style new style
     */
    public void updateStyle (SimplexStyle style) {

        // assign a new style
        this.style = style;

        // if the style is OK - update the elements as well
        if (this.style != null) {
            mask.setBackgroundColor(style.getFullscreenMaskBgColor());
            mask.setAlpha(style.getFullscreenMaskAlpha());
        }
    }

    /**
     * Method that sets the button style to "playing"
     */
    public void setStatePlaying () {
        play.setImageBitmap(null);
        mask.setVisibility(GONE);
    }

    /**
     * Method that sets the button style to "paused"
     */
    public void setStatePaused () {
        play.setImageBitmap(style.getButtonPlaybackPlayBitmap());
        mask.setVisibility(VISIBLE);
    }

    /**
     * Method that sets the button style to "rewind"
     */
    public void setStateRewind () {
        play.setImageBitmap(style.getButtonPlaybackReplayBitmap());
        mask.setVisibility(VISIBLE);
    }

    /**
     * Setter for the listener
     *
     * @param listener a new instance of type Listener
     */
    public void setListener (Listener listener) {
        play.setOnClickListener(listener);
    }

    /**
     * Custom interface for the PlaybackButton class, derived from View.OnClickListener
     */
    public interface Listener extends View.OnClickListener {
        // do nothing
    }
}
