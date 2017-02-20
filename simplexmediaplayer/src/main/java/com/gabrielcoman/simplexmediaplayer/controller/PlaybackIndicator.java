/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

/**
 * This class holds all the indicators needed to display the playback, buffer, length and
 * seekbar for the video player
 */
public class PlaybackIndicator extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    // constants
    private static final int SEEKBAR_SIZE   = 30;
    private static final int INDICATOR_SIZE = 10;

    // subviews
    private View             length;
    private View             buffer;
    private View             playback;
    private SeekBar          seekbar;
    private ShapeDrawable    thumb;

    // private state var
    private boolean          userControlled = false;

    // interface
    private Listener         listener;

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    public PlaybackIndicator(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    public PlaybackIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    public PlaybackIndicator(Context context, AttributeSet attrs, int defStyleAttr) {

        // call to super
        super(context, attrs, defStyleAttr);

        // instantiate local listener interface so it's never null
        listener = new Listener() {
            @Override public void didStopTracking(float percent) {}};

        // this indicator matches the holder in width and represents the whole video length
        length = new View(context);
        RelativeLayout.LayoutParams indicatorLengthParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, INDICATOR_SIZE);
        int lengthHMargin = (int) (SEEKBAR_SIZE / 2.0f);
        indicatorLengthParams.addRule(CENTER_VERTICAL, RelativeLayout.TRUE);
        indicatorLengthParams.setMargins(lengthHMargin, 0, lengthHMargin, 0);
        length.setLayoutParams(indicatorLengthParams);
        addView(length);

        // this indicator represents how much of the video has been buffered so far
        buffer = new View(context);
        RelativeLayout.LayoutParams indicatorBufferParams
                = new RelativeLayout.LayoutParams(0, INDICATOR_SIZE);
        indicatorBufferParams.addRule(CENTER_VERTICAL, RelativeLayout.TRUE);
        int bufferHMargin = (int) (SEEKBAR_SIZE / 2.0F);
        indicatorBufferParams.setMargins(bufferHMargin, 0, bufferHMargin, 0);
        buffer.setLayoutParams(indicatorBufferParams);
        addView(buffer);

        // this indicator represents the current state of the playback
        // it is linked to the seek bar (and acts as a seek-bar bar of sorts)
        playback = new View(context);
        RelativeLayout.LayoutParams indicatorPlaybackParams
                = new RelativeLayout.LayoutParams(0, INDICATOR_SIZE);
        indicatorPlaybackParams.addRule(CENTER_VERTICAL, RelativeLayout.TRUE);
        int playbackHMargin = (int) (SEEKBAR_SIZE / 2.0F);
        indicatorPlaybackParams.setMargins(playbackHMargin, 0, playbackHMargin, 0);
        playback.setLayoutParams(indicatorPlaybackParams);
        addView(playback);

        // this indicator is a seek-bar that can control the movie playback
        // users can drag it to go to different parts of the video with it
        seekbar = new SeekBar(context);
        seekbar.setBackgroundColor(Color.TRANSPARENT);
        int seekbarHMargin = (int) (SEEKBAR_SIZE / 2.0F);
        seekbar.setPadding(seekbarHMargin, 0, seekbarHMargin, 0);
        RelativeLayout.LayoutParams indicatorSeekBarParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SEEKBAR_SIZE);
        indicatorSeekBarParams.addRule(CENTER_VERTICAL, RelativeLayout.TRUE);
        indicatorSeekBarParams.setMargins(0, 0, 0, 0);
        seekbar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN));
        seekbar.setLayoutParams(indicatorSeekBarParams);
        seekbar.setMinimumHeight(SEEKBAR_SIZE);
        seekbar.setOnSeekBarChangeListener(this);
        addView(seekbar);

        // it's thumb, that has the same color
        thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(SEEKBAR_SIZE);
        thumb.setIntrinsicWidth(SEEKBAR_SIZE);
        seekbar.setThumb(thumb);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Update UI methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method that updates the style of the component
     *
     * @param style new style
     */
    public void updateStyle (SimplexStyle style) {
        if (style != null) {
            length.setBackgroundColor(style.getIndicatorLengthBgColor());
            length.setAlpha(style.getIndicatorLengthAlpha());
            buffer.setBackgroundColor(style.getIndicatorBufferBgColor());
            buffer.setAlpha(style.getIndicatorBufferAlpha());
            playback.setBackgroundColor(style.getIndicatorPlaybackBgColor());
            thumb.setColorFilter(style.getIndicatorPlaybackBgColor(), PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * Public method that updates the seek-bar thumb to the current percentage of the video
     * that's been played.
     *
     * @param percent current percent of the movie.
     */
    public void updateThumb (float percent) {
        seekbar.setProgress((int) (100 * percent));
    }

    /**
     * Public method that updates the buffer indicator to the current percentage of the video
     * that's been played.
     *
     * @param percent current percent of the movie.
     */
    public void updateBuffer (float percent) {
        int length = this.length.getMeasuredWidth();
        int width = (int) (percent * length);
        ViewGroup.LayoutParams params = buffer.getLayoutParams();
        params.width = width;
        buffer.setLayoutParams(params);
    }

    /**
     * Public method that will update the playback indicator to the current percentage of the
     * video that's been played.
     *
     * @param percent current percent of the movie.
     */
    public void updatePlayback (float percent) {
        int length = this.length.getMeasuredWidth();
        int width = (int) (percent * length);
        ViewGroup.LayoutParams params = playback.getLayoutParams();
        params.width = width;
        playback.setLayoutParams(params);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation of SeekBar.OnSeekBarChangeListener
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Seek-bar listener method that informs the controller when the seek-bar is being
     * moved around by the user
     *
     * @param seekBar   current seek bar
     * @param progress  progress the bar is at
     * @param fromUser  if the update comes from the user
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // calc the percent as a float
        float percent = progress / 100.0F;

        // update internally
        updatePlayback(percent);
    }

    /**
     * Seek-bar listener method that informs the controller of when the seek-bar has begun being
     * touched. The state boolean "userControlsPlayback" becomes true and the playback
     * indicator will be controller only by the user, not by the video player.
     *
     * @param seekBar current seek bar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        userControlled = true;
    }

    /**
     * Seek-bar listener method that informs the controller of when the seek-bar has stopped
     * being touched. The state boolean "userControlsPlayback" becomes false and the playback
     * indicator will begin being updated by the video player.
     *
     * @param seekBar current seek bar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        // re-set this state var to false
        userControlled = false;

        // calc the current progress as a float
        float percent = seekBar.getProgress() / 100.0F;

        listener.didStopTracking(percent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Setters and getters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter for the user-control-playback state var
     *
     * @return true or false
     */
    public boolean isUserControlled() {
        return userControlled;
    }

    /**
     * Setter for the listener
     *
     * @param listener a new instance of type Listener
     */
    public void setListener (Listener listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    /**
     * Listener for the PlaybackIndicator
     *
     */
    public interface Listener {

        /**
         * Only method that matters externally is when the user has stopped tracking
         *
         * @param percent percent at which user has stopped tracking
         */
        void didStopTracking (float percent);

    }
}
