package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gabrielcoman.simplexmediaplayer.aux.image.SimplexBitmap;
import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

public class SimplexController extends RelativeLayout implements
        SeekBar.OnSeekBarChangeListener
{

    // some constants
    private static final int    CONTROLLER_HEIGHT = 80;
    private static final int    INDICATOR_HEIGHT = 30;

    private ImageButton         buttonPlayback;
    private View                fullscreenMask;

    private ImageView           mask;

    private RelativeLayout      indicatorBackground;
    private RelativeLayout      indicatorHolder;
    private SeekBar             indicatorSeekBar;
    private View                indicatorLength;
    private View                indicatorPlayback;
    private View                indicatorBuffer;

    private TextView            indicatorCurrentTime;
    private TextView            indicatorTotalTime;

    private SimplexStyle        controllerStyle;

    private boolean             userControlsPlayback = false;

    private ProgressIndicatorInterface listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Controller constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public SimplexController(Context context) {
        // call this
        this(context, null, 0);

        // set a new default listener so I don't have to do a null check
        listener = new ProgressIndicatorInterface() {
            @Override public void shouldAdvanceToStep(float percent) {}};
    }

    public SimplexController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplexController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // set a new style
        controllerStyle = SimplexStyle.normalStyle();

        fullscreenMask = new View(context);
        fullscreenMask.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullscreenMask.setVisibility(GONE);
        fullscreenMask.setBackgroundColor(Color.BLACK);
        fullscreenMask.setAlpha(0.25F);
        addView(fullscreenMask);

        buttonPlayback = new ImageButton(context);
        RelativeLayout.LayoutParams startButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        startButtonParams.setMargins(0, 0, 0, CONTROLLER_HEIGHT);
        buttonPlayback.setLayoutParams(startButtonParams);
        buttonPlayback.setBackgroundColor(Color.TRANSPARENT);
        buttonPlayback.setImageBitmap(SimplexBitmap.createPlayButtonBitmap());
        addView(buttonPlayback);

        mask = new ImageView(context);
        mask.setImageBitmap(SimplexBitmap.createVideoGradientBitmap());
        RelativeLayout.LayoutParams backgroundParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        backgroundParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mask.setLayoutParams(backgroundParams);
        mask.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mask);

        indicatorBackground = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorBackground.setLayoutParams(layoutParams);
        indicatorBackground.setBackgroundColor(controllerStyle.getIndicatorBackgroundBgColor());
        addView(indicatorBackground);

        indicatorCurrentTime = new TextView(context);
        RelativeLayout.LayoutParams indicatorCurrentTimeParams = new RelativeLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorCurrentTime.setLayoutParams(indicatorCurrentTimeParams);
        indicatorCurrentTime.setGravity(Gravity.CENTER);
        indicatorCurrentTime.setText("00:00");
        indicatorCurrentTime.setBackgroundColor(controllerStyle.getIndicatorCurrentTimeBgColor());
        indicatorCurrentTime.setTextColor(controllerStyle.getIndicatorCurrentTimeTxtColor());
        indicatorBackground.addView(indicatorCurrentTime);

        indicatorHolder = new RelativeLayout(context);
        RelativeLayout.LayoutParams indicatorHolderParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, INDICATOR_HEIGHT);
        int verticalMargin = (int)((CONTROLLER_HEIGHT - INDICATOR_HEIGHT) / 2.0F);
        indicatorHolderParams.setMargins(110, verticalMargin, 110, verticalMargin);
        indicatorHolder.setLayoutParams(indicatorHolderParams);
        indicatorHolder.setBackgroundColor(Color.TRANSPARENT);
        indicatorBackground.addView(indicatorHolder);

        indicatorLength = new View(context);
        RelativeLayout.LayoutParams indicatorLengthParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorLengthParams.setMargins(15, 10, 15, 10);
        indicatorLength.setLayoutParams(indicatorLengthParams);
        indicatorLength.setBackgroundColor(controllerStyle.getIndicatorLengthBgColor());
        indicatorLength.setAlpha(controllerStyle.getIndicatorLengthAlpha());
        indicatorHolder.addView(indicatorLength);

        indicatorBuffer = new View(context);
        RelativeLayout.LayoutParams indicatorBufferParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorBufferParams.setMargins(15, 10, 15, 10);
        indicatorBuffer.setLayoutParams(indicatorBufferParams);
        indicatorBuffer.setBackgroundColor(controllerStyle.getIndicatorBufferBgColor());
        indicatorBuffer.setAlpha(controllerStyle.getIndicatorBufferAlpha());
        indicatorHolder.addView(indicatorBuffer);

        indicatorSeekBar = new SeekBar(context);
        indicatorSeekBar.setBackgroundColor(Color.TRANSPARENT);
        indicatorSeekBar.setPadding(15, 0, 15, 0);
        RelativeLayout.LayoutParams indicatorSeekBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorSeekBarParams.addRule(CENTER_VERTICAL, RelativeLayout.TRUE);
        indicatorSeekBarParams.setMargins(0, 0, 0, 0);
        indicatorSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN));
        indicatorSeekBar.setLayoutParams(indicatorSeekBarParams);
        indicatorSeekBar.setMinimumHeight(INDICATOR_HEIGHT);
        indicatorSeekBar.setOnSeekBarChangeListener(this);
        indicatorHolder.addView(indicatorSeekBar);

        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

        thumb.setIntrinsicHeight(30);
        thumb.setIntrinsicWidth(30);
        thumb.setColorFilter(Color.RED, PorterDuff.Mode.ADD);
        indicatorSeekBar.setThumb(thumb);

        indicatorPlayback = new View(context);
        RelativeLayout.LayoutParams indicatorPlaybackParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorPlaybackParams.setMargins(15, 10, 15, 10);
        indicatorPlayback.setLayoutParams(indicatorPlaybackParams);
        indicatorPlayback.setBackgroundColor(controllerStyle.getIndicatorPlaybackBgColor());
        indicatorHolder.addView(indicatorPlayback);

        indicatorTotalTime = new TextView(context);
        RelativeLayout.LayoutParams indicatorTotalTimeParams = new RelativeLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorTotalTimeParams.addRule(ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        indicatorTotalTime.setLayoutParams(indicatorTotalTimeParams);
        indicatorTotalTime.setGravity(Gravity.CENTER);
        indicatorTotalTime.setText("00:00");
        indicatorTotalTime.setBackgroundColor(controllerStyle.getIndicatorTotalTimeBgColor());
        indicatorTotalTime.setTextColor(controllerStyle.getIndicatorTotalTimeTxtColor());
        indicatorBackground.addView(indicatorTotalTime);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Update UI methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateButtonPlaybackForState (Simplex.PlaybackState state) {

        switch (state) {
            case NOTSTARTED:
            case PAUSED: {
                buttonPlayback.setImageBitmap(SimplexBitmap.createPlayButtonBitmap());
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
                buttonPlayback.setImageBitmap(SimplexBitmap.createReplayButtonBitmap());
                fullscreenMask.setVisibility(VISIBLE);
                break;
            }
        }
    }

    public void updateIndicatorPlaybackForProgress (float percent) {
        if (!userControlsPlayback) {
            updateIndicatorPlaybackForProgressInternal (percent);
        }
    }

    private void updateIndicatorPlaybackForProgressInternal (float percent) {
        int measuredWidth = indicatorLength.getMeasuredWidth();
        int percentWidth = (int) (percent * measuredWidth);
        ViewGroup.LayoutParams params = indicatorPlayback.getLayoutParams();
        params.width = percentWidth;
        indicatorPlayback.setLayoutParams(params);
    }

    public void updateIndicatorSeekBarForProgress (float percent) {
        if (!userControlsPlayback) {
            indicatorSeekBar.setProgress((int) (percent * 100));
        }
    }

    public void updateIndicatorBufferForProgress (float percent) {
        int measuredWidth = indicatorLength.getMeasuredWidth();
        int percentWidth = (int) (percent * measuredWidth);
        ViewGroup.LayoutParams params = indicatorBuffer.getLayoutParams();
        params.width = percentWidth;
        indicatorBuffer.setLayoutParams(params);
    }

    public void updateIndicatorTotalTime (String text) {
        indicatorTotalTime.setText(text);
    }

    public void updateInficatorCurrentTime (String text) {
        indicatorCurrentTime.setText(text);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SeekBar delegatre methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float percent = progress / 100.0F;
        updateIndicatorPlaybackForProgressInternal(percent);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        userControlsPlayback = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        userControlsPlayback = false;
        float percent = seekBar.getProgress() / 100.0F;
        listener.shouldAdvanceToStep(percent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Set different Listeners methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setButtonPlaybackClickListener (OnClickListener listener) {
        buttonPlayback.setOnClickListener(listener);
    }

    public void setListener (ProgressIndicatorInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Public interface that a Simplex player must implement to talk with the controllers
    ////////////////////////////////////////////////////////////////////////////////////////////////

    interface ProgressIndicatorInterface {
        void shouldAdvanceToStep (float percent);
    }
}
