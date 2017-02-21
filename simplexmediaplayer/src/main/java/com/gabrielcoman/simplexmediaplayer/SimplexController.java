/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;
import com.gabrielcoman.simplexmediaplayer.controller.IndicatorBackground;
import com.gabrielcoman.simplexmediaplayer.controller.PlayButton;
import com.gabrielcoman.simplexmediaplayer.controller.PlaybackIndicator;
import com.gabrielcoman.simplexmediaplayer.controller.TimeIndicator;

/**
 * Class that is a custom media controller built on top of a relative layout
 */
public class SimplexController extends RelativeLayout implements
        PlayButton.Listener,
        PlaybackIndicator.Listener
{

    // constants that define the size of the controller, the playback holder and the
    // playback lines themselves
    private static final int    CONTROLLER_HEIGHT = 80;

    // subviews
    private PlayButton          play;
    private IndicatorBackground background;
    private PlaybackIndicator   playback;
    private TimeIndicator       currentTime;
    private TimeIndicator       totalTime;

    // the style that's going to be applied to the controller
    private SimplexStyle        style;

    // instance of the Listener that allows the controller to send callbacks
    // to the main Simplex player
    private Listener            listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Controller constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Normal constructor with context
     *
     * @param context the current context (activity or fragment)
     */
    SimplexController(Context context) {
        // call this
        this(context, null, 0);

        // set a new default listener so I don't have to do a null check
        listener = new Listener() {
            @Override public void didClickOnPlaybackButton() {}
            @Override public void shouldAdvanceToPercent(float percent) {}};
    }

    /**
     * Constructor with context and attribute set
     *
     * @param context the current context (activity or fragment)
     * @param attrs   a new attribute set
     */
    SimplexController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with context, attribute set and default style attribute
     *
     * @param context       the current context (activity or fragment)
     * @param attrs         a new attribute set
     * @param defStyleAttr  the default style attribute
     */
    SimplexController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // set the default style
        style = SimplexStyle.defaultStyle();

        play = new PlayButton(context);
        RelativeLayout.LayoutParams buttonPlaybackParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        play.setLayoutParams(buttonPlaybackParams);
        play.updateStyle(style);
        play.setListener(this);
        addView(play);

        background = new IndicatorBackground(context);
        RelativeLayout.LayoutParams indicatorBackgroundParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        indicatorBackgroundParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        background.setLayoutParams(indicatorBackgroundParams);
        background.updateStyle(style);
        addView(background);

        // the current time text view indicator
        // it's the most left-bound and has a weight of 1 and wraps it's content depending on the
        // size of the text being displayed
        currentTime = new TimeIndicator(context);
        LinearLayout.LayoutParams indicatorCurrentTimeParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorCurrentTimeParams.weight = 1;
        currentTime.setLayoutParams(indicatorCurrentTimeParams);
        currentTime.updateStyle(style);
        background.getSupport().addView(currentTime);

        playback = new PlaybackIndicator(context);
        LinearLayout.LayoutParams indicatorPlaybackParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorPlaybackParams.weight = 50;
        playback.setLayoutParams(indicatorPlaybackParams);
        playback.updateStyle(style);
        playback.setListener(this);
        background.getSupport().addView(playback);

        // rightmost indicator; text view that displays the total video time
        totalTime = new TimeIndicator(context);
        LinearLayout.LayoutParams indicatorTotalTimeParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorTotalTimeParams.weight = 1;
        totalTime.setLayoutParams(indicatorTotalTimeParams);
        totalTime.updateStyle(style);
        background.getSupport().addView(totalTime);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Update UI methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method that updates the current controller style
     *
     * @param style a new style instance
     */
    void updateStyle (SimplexStyle style) {

        // get the style
        this.style = style;

        play.updateStyle(style);
        playback.updateStyle(style);
        currentTime.updateStyle(style);
        totalTime.updateStyle(style);
        background.updateStyle(style);
    }

    /**
     * Public method that updates the playback button depending on the current playback state
     *
     * @param state current playback state
     */
    void updateButtonPlaybackForState (Simplex.PlaybackState state) {

        switch (state) {
            case NOTSTARTED:
            case PAUSED: {
                play.setStatePaused();
                break;
            }
            case AUTOSTART:
            case PLAYING: {
                play.setStatePlaying();
                break;
            }
            case REWIND: {
                play.setStateRewind();
                break;
            }
        }
    }

    /**
     * Public method that will update the playback indicator to the current percentage of the
     * video that's been played.
     * It will only do so if the user's not controlling the movie playback with his finger
     *
     * @param percent current percent of the movie.
     */
    void updatePlayback (float percent) {
        if (!playback.isUserControlled()) {
            playback.updatePlayback(percent);
        }
    }

    /**
     * Public method that updates the seek-bar thumb to the current percentage of the video
     * that's been played.
     * It will only do so if the user's not controlling the movie playback with his finger.
     *
     * @param percent current percent of the movie.
     */
    void updateThumb (float percent) {
        if (!playback.isUserControlled()) {
            playback.updateThumb(percent);
        }
    }

    /**
     * Public method that updates the buffer indicator to the current percentage of the video
     * that's been played.
     *
     * @param percent current percent of the movie.
     */
    void updateBuffer (float percent) {
        playback.updateBuffer(percent);
    }

    /**
     * Public method that updates the text for the total time
     *
     * @param text a text containing the total time
     */
    void updateTotalTime (String text) {
        totalTime.setText(text);
    }

    /**
     * Public method that updates the current time text
     *
     * @param text a text containing the current time
     */
    void updateCurrentTime (String text) {
        currentTime.setText(text);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener implementations for the PlaybackButton & PlaybackIndicator interfaces
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        listener.didClickOnPlaybackButton();
    }

    @Override
    public void didStopTracking(float percent) {
        listener.shouldAdvanceToPercent(percent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Set different listeners methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Setter method for the controller's own listener
     *
     * @param listener a new instance of Listener
     */
    public void setListener (Listener listener) {
        this.listener = listener != null ? listener : this.listener;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Public interface that a Simplex player must implement to talk with the controllers
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Listener that must be implemented by the SimplexPlayer in order to get callbacks from
     * the controller.
     *
     */
    interface Listener {

        /**
         * Method implemented by the interface. Gets called when someone clicks on the
         * big play button
         *
         */
        void didClickOnPlaybackButton ();

        /**
         * Method implemented by the interface. It informs whoever's implemented the interface
         * that it should advance to a certain percent of the video playback length.
         *
         * @param percent percent to advance to
         */
        void shouldAdvanceToPercent (float percent);
    }
}
