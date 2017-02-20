/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

/**
 * Class that is a custom media controller built on top of a relative layout
 */
public class SimplexController extends RelativeLayout implements
        SeekBar.OnSeekBarChangeListener
{

    // constants that define the size of the controller, the playback holder and the
    // playback lines themselves
    private static final int    CONTROLLER_HEIGHT    = 80;
    private static final int    HOLDER_HEIGHT        = 30;
    private static final int    INDICATOR_HEIGHT     = 10;

    // subviews for the play button
    private View                fullscreenMask       = null;
    private ImageButton         buttonPlayback       = null;

    // bottom gradient mask subview
    private ImageView           bottomGradientMask   = null;

    // all indicators needed for the player
    private RelativeLayout      indicatorBackground  = null;
    private LinearLayout        indicatorLinear      = null;
    private RelativeLayout      indicatorHolder      = null;
    private SeekBar             indicatorSeekBar     = null;
    private ShapeDrawable       thumb                = null;
    private View                indicatorLength      = null;
    private View                indicatorPlayback    = null;
    private View                indicatorBuffer      = null;

    // text indicators to show the current & total time
    private TextView            indicatorCurrentTime = null;
    private TextView            indicatorTotalTime   = null;

    // the style that's going to be applied to the controller
    private SimplexStyle        style                = null;

    // variable that holds whether the user controls playback or not
    private boolean             userControlsPlayback = false;

    // instance of the ProgressIndicatorInterface that allows the controller to send callbacks
    // to the main Simplex player
    private ProgressIndicatorInterface listener      = null;

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
        listener = new ProgressIndicatorInterface() {
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

        // create the fullscreen mask
        fullscreenMask = new View(context);
        ViewGroup.LayoutParams fullscreenMaskParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fullscreenMask.setLayoutParams(fullscreenMaskParams);
        fullscreenMask.setVisibility(GONE);
        fullscreenMask.setBackgroundColor(style.getFullscreenMaskBgColor());
        fullscreenMask.setAlpha(style.getFullscreenMaskAlpha());
        addView(fullscreenMask);

        // create the playback button
        buttonPlayback = new ImageButton(context);
        RelativeLayout.LayoutParams startButtonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        startButtonParams.setMargins(0, 0, 0, CONTROLLER_HEIGHT);
        buttonPlayback.setLayoutParams(startButtonParams);
        buttonPlayback.setPadding(0, CONTROLLER_HEIGHT, 0, 0);
        buttonPlayback.setBackgroundColor(Color.TRANSPARENT);
        buttonPlayback.setImageBitmap(null);
        addView(buttonPlayback);

        // create the bottom gradient mask
        bottomGradientMask = new ImageView(context);
        bottomGradientMask.setImageBitmap(style.getBottomGradientMaskBitmap());
        RelativeLayout.LayoutParams bottomGradientParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        bottomGradientParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomGradientMask.setLayoutParams(bottomGradientParams);
        bottomGradientMask.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(bottomGradientMask);

        // create the full indicator background, that's as wide as the video and aligns to
        // the bottom of the screen
        indicatorBackground = new RelativeLayout(context);
        RelativeLayout.LayoutParams indicatorBackgroundParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        indicatorBackgroundParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorBackground.setLayoutParams(indicatorBackgroundParams);
        indicatorBackground.setBackgroundColor(style.getIndicatorBackgroundBgColor());
        addView(indicatorBackground);

        // create a subview of the indicator background that's a LinearLayout. This will hold
        // just three children:
        // - left: the current time indicator (text view with wrap content)
        // - center: the indicator holder; basically the view that contains all indicators
        // - right: the total time indicator (text with with wrap content equal to the left one)
        indicatorLinear = new LinearLayout(context);
        LinearLayout.LayoutParams indicatorLinearParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorLinear.setOrientation(LinearLayout.HORIZONTAL);
        indicatorLinear.setLayoutParams(indicatorLinearParams);
        indicatorLinear.setBackgroundColor(Color.TRANSPARENT);
        indicatorBackground.addView(indicatorLinear);

        // the current time text view indicator
        // it's the most left-bound and has a weigt of 1 and wraps it's content depending on the
        // size of the text being displayed
        indicatorCurrentTime = new TextView(context);
        LinearLayout.LayoutParams indicatorCurrentTimeParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorCurrentTimeParams.weight = 1;
        indicatorCurrentTime.setLayoutParams(indicatorCurrentTimeParams);
        indicatorCurrentTime.setGravity(Gravity.CENTER);
        indicatorCurrentTime.setText("00:00");
        indicatorCurrentTime.setPadding(15, 10, 15, 10);
        indicatorCurrentTime.setBackgroundColor(style.getIndicatorCurrentTimeBgColor());
        indicatorCurrentTime.setTextColor(style.getIndicatorCurrentTimeTxtColor());
        indicatorLinear.addView(indicatorCurrentTime);

        // a relative layout that acts as holding view for all the buffer, length, playback and
        // seeker indicators
        indicatorHolder = new RelativeLayout(context);
        LinearLayout.LayoutParams indicatorHolderParams =
                new LinearLayout.LayoutParams(0, HOLDER_HEIGHT);
        indicatorHolderParams.weight = 50;
        int holderVMargin = (int)((CONTROLLER_HEIGHT - HOLDER_HEIGHT) / 2.0F);
        indicatorHolderParams.setMargins(0, holderVMargin, 0, holderVMargin);
        indicatorHolder.setLayoutParams(indicatorHolderParams);
        indicatorHolder.setBackgroundColor(Color.TRANSPARENT);
        indicatorLinear.addView(indicatorHolder);

        // this indicator matches the holder in width and represents the whole video length
        indicatorLength = new View(context);
        RelativeLayout.LayoutParams indicatorLengthParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int lengthVMargin = (int)((HOLDER_HEIGHT - INDICATOR_HEIGHT) / 2.0F);
        int lengthHMargin = (int) (HOLDER_HEIGHT / 2.0F);
        indicatorLengthParams.setMargins(lengthHMargin, lengthVMargin, lengthHMargin, lengthVMargin);
        indicatorLength.setLayoutParams(indicatorLengthParams);
        indicatorLength.setBackgroundColor(style.getIndicatorLengthBgColor());
        indicatorLength.setAlpha(style.getIndicatorLengthAlpha());
        indicatorHolder.addView(indicatorLength);

        // this indicator represents how much of the video has been buffered so far
        indicatorBuffer = new View(context);
        RelativeLayout.LayoutParams indicatorBufferParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        int bufferVMargin = (int)((HOLDER_HEIGHT - INDICATOR_HEIGHT) / 2.0F);
        int bufferHMargin = (int) (HOLDER_HEIGHT / 2.0F);
        indicatorBufferParams.setMargins(bufferHMargin, bufferVMargin, bufferHMargin, bufferVMargin);
        indicatorBuffer.setLayoutParams(indicatorBufferParams);
        indicatorBuffer.setBackgroundColor(style.getIndicatorBufferBgColor());
        indicatorBuffer.setAlpha(style.getIndicatorBufferAlpha());
        indicatorHolder.addView(indicatorBuffer);

        // this indicator represents the current state of the playback
        // it is linked to the seek bar (and acts as a seek-bar bar of sorts)
        indicatorPlayback = new View(context);
        RelativeLayout.LayoutParams indicatorPlaybackParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        int playbackVMargin = (int)((HOLDER_HEIGHT - INDICATOR_HEIGHT) / 2.0F);
        int playbackHMargin = (int) (HOLDER_HEIGHT / 2.0F);
        indicatorPlaybackParams.setMargins(playbackHMargin, playbackVMargin, playbackHMargin, playbackVMargin);
        indicatorPlayback.setLayoutParams(indicatorPlaybackParams);
        indicatorPlayback.setBackgroundColor(style.getIndicatorPlaybackBgColor());
        indicatorHolder.addView(indicatorPlayback);

        // this indicator is a seek-bar that can control the movie playback
        // users can drag it to go to different parts of the video with it
        indicatorSeekBar = new SeekBar(context);
        indicatorSeekBar.setBackgroundColor(Color.TRANSPARENT);
        int seekbarHMargin = (int) (HOLDER_HEIGHT / 2.0F);
        indicatorSeekBar.setPadding(seekbarHMargin, 0, seekbarHMargin, 0);
        RelativeLayout.LayoutParams indicatorSeekBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorSeekBarParams.addRule(CENTER_VERTICAL, RelativeLayout.TRUE);
        indicatorSeekBarParams.setMargins(0, 0, 0, 0);
        indicatorSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN));
        indicatorSeekBar.setLayoutParams(indicatorSeekBarParams);
        indicatorSeekBar.setMinimumHeight(HOLDER_HEIGHT);
        indicatorSeekBar.setOnSeekBarChangeListener(this);
        indicatorHolder.addView(indicatorSeekBar);

        // it's thumb, that has the same color
        thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(HOLDER_HEIGHT);
        thumb.setIntrinsicWidth(HOLDER_HEIGHT);
        thumb.setColorFilter(style.getIndicatorPlaybackBgColor(), PorterDuff.Mode.SRC_IN);
        indicatorSeekBar.setThumb(thumb);

        // rightmost indicator; text view that displays the total video time
        indicatorTotalTime = new TextView(context);
        LinearLayout.LayoutParams indicatorTotalTimeParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorTotalTimeParams.weight = 1;
        indicatorTotalTime.setLayoutParams(indicatorTotalTimeParams);
        indicatorTotalTime.setGravity(Gravity.CENTER);
        indicatorTotalTime.setText("00:00");
        indicatorTotalTime.setPadding(15, 10, 15, 10);
        indicatorTotalTime.setBackgroundColor(style.getIndicatorTotalTimeBgColor());
        indicatorTotalTime.setTextColor(style.getIndicatorTotalTimeTxtColor());
        indicatorLinear.addView(indicatorTotalTime);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Update UI methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method that updates the current controller style
     *
     * @param style a new style instance
     */
    void setStyle (SimplexStyle style) {

        // get the style
        this.style = style;

        // update all the memebers that can be customised
        indicatorLength.setBackgroundColor(style.getIndicatorLengthBgColor());
        indicatorLength.setAlpha(style.getIndicatorLengthAlpha());
        indicatorBuffer.setBackgroundColor(style.getIndicatorBufferBgColor());
        indicatorBuffer.setAlpha(style.getIndicatorBufferAlpha());
        indicatorPlayback.setBackgroundColor(style.getIndicatorPlaybackBgColor());
        thumb.setColorFilter(style.getIndicatorPlaybackBgColor(), PorterDuff.Mode.SRC_IN);
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
                buttonPlayback.setImageBitmap(style.getButtonPlaybackPlayBitmap());
                fullscreenMask.setVisibility(VISIBLE);
                break;
            }
            case AUTOSTART:
            case PLAYING: {
                buttonPlayback.setImageBitmap(null);
                fullscreenMask.setVisibility(GONE);
                break;
            }
            case REWIND: {
                buttonPlayback.setImageBitmap(style.getButtonPlaybackReplayBitmap());
                fullscreenMask.setVisibility(VISIBLE);
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
    void updateIndicatorPlaybackForProgress (float percent) {
        if (!userControlsPlayback) {
            updateIndicatorPlaybackForProgressInternal (percent);
        }
    }

    /**
     * Private method used by the seek-bar listener in order to update the playback indicator
     *
     * @param percent current percent of the movie.
     */
    private void updateIndicatorPlaybackForProgressInternal (float percent) {
        int percentWidth = getIndicatorWidthFromTotalForPercent(percent);
        updateIndicatorLayoutParams(indicatorPlayback, percentWidth);
    }

    /**
     * Public method that updates the seek-bar thumb to the current percentage of the video
     * that's been played.
     * It will only do so if the user's not controlling the movie playback with his finger.
     *
     * @param percent current percent of the movie.
     */
    void updateIndicatorSeekBarForProgress (float percent) {
        if (!userControlsPlayback) {
            indicatorSeekBar.setProgress((int) (percent * 100));
        }
    }

    /**
     * Public method that updates the buffer indicator to the current percentage of the video
     * that's been played.
     *
     * @param percent current percent of the movie.
     */
    void updateIndicatorBufferForProgress (float percent) {
        int percentWidth = getIndicatorWidthFromTotalForPercent(percent);
        updateIndicatorLayoutParams(indicatorBuffer, percentWidth);
    }

    /**
     * Public method that updates the text for the total time
     *
     * @param text a text containing the total time
     */
    void updateIndicatorTotalTime (String text) {
        indicatorTotalTime.setText(text);
    }

    /**
     * Public method that updates the current time text
     *
     * @param text a text containing the current time
     */
    void updateIndicatorCurrentTime (String text) {
        indicatorCurrentTime.setText(text);
    }

    /**
     * Method that returns the lenght an indicator should be set to in order for it's
     * width to represent X% of the total length width
     *
     * @param percent percent to set
     * @return        a width proportional to the percent given from the total length
     */
    public int getIndicatorWidthFromTotalForPercent (float percent) {
        int totalLength = indicatorLength.getMeasuredWidth();
        return (int) (percent * totalLength);
    }

    /**
     * Method that updates a generic indicator to a specified width
     *
     * @param indicator indicator to update
     * @param width     width to update layout params width
     */
    public void updateIndicatorLayoutParams (View indicator, int width) {
        ViewGroup.LayoutParams params = indicator.getLayoutParams();
        params.width = width;
        indicator.setLayoutParams(params);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SeekBar listener methods
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

        // then update the playback indicator bar
        updateIndicatorPlaybackForProgressInternal(percent);
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
        userControlsPlayback = true;
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
        userControlsPlayback = false;

        // calc the current progress as a float
        float percent = seekBar.getProgress() / 100.0F;

        // send out a listener message toward the video player so that it know how to update
        // the player to a new position
        listener.shouldAdvanceToPercent(percent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Set different listeners methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Setter method for the click listener used on the playback button
     *
     * @param listener a new instance of the OnClickListener
     */
    public void setButtonPlaybackClickListener (OnClickListener listener) {
        buttonPlayback.setOnClickListener(listener);
    }

    /**
     * Setter method for the controller's own listener
     *
     * @param listener a new instance of ProgressIndicatorInterface
     */
    public void setListener (ProgressIndicatorInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Public interface that a Simplex player must implement to talk with the controllers
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Interface that must be implemented by the SimplexPlayer in order to get callbacks from
     * the controller.
     *
     */
    interface ProgressIndicatorInterface {

        /**
         * Method implemented by the interface. It informs whoever's implemented the interface
         * that it should advance to a certain percent of the video playback length.
         *
         * @param percent percent to advance to
         */
        void shouldAdvanceToPercent (float percent);
    }
}
