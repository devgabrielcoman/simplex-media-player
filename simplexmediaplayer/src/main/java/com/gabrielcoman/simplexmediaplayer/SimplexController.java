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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

public class SimplexController extends RelativeLayout implements
        SeekBar.OnSeekBarChangeListener
{

    // some constants
    private static final int    CONTROLLER_HEIGHT = 80;
    private static final int    HOLDER_HEIGHT = 30;
    private static final int    INDICATOR_HEIGHT = 10;

    private View                fullscreenMask;
    private ImageButton         buttonPlayback;

    private ImageView           bottomGradientMask;

    private RelativeLayout      indicatorBackground;
    private LinearLayout        indicatorLinear;
    private RelativeLayout      indicatorHolder;
    private SeekBar             indicatorSeekBar;
    private ShapeDrawable       thumb;
    private View                indicatorLength;
    private View                indicatorPlayback;
    private View                indicatorBuffer;

    private TextView            indicatorCurrentTime;
    private TextView            indicatorTotalTime;

    private SimplexStyle        style;

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
        style = SimplexStyle.normalStyle();

        fullscreenMask = new View(context);
        ViewGroup.LayoutParams fullscreenMaskParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fullscreenMask.setLayoutParams(fullscreenMaskParams);
        fullscreenMask.setVisibility(GONE);
        fullscreenMask.setBackgroundColor(style.getFullscreenMaskBgColor());
        fullscreenMask.setAlpha(style.getFullscreenMaskAlpha());
        if (style.isHasFullscreenMask()) {
            addView(fullscreenMask);
        }

        buttonPlayback = new ImageButton(context);
        RelativeLayout.LayoutParams startButtonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        startButtonParams.setMargins(0, 0, 0, CONTROLLER_HEIGHT);
        buttonPlayback.setLayoutParams(startButtonParams);
        buttonPlayback.setPadding(0, CONTROLLER_HEIGHT, 0, 0);
        buttonPlayback.setBackgroundColor(Color.TRANSPARENT);
        buttonPlayback.setImageBitmap(style.getButtonPlaybackPlayBitmap());
        addView(buttonPlayback);

        bottomGradientMask = new ImageView(context);
        bottomGradientMask.setImageBitmap(style.getBottomGradientMaskBitmap());
        RelativeLayout.LayoutParams bottomGradientParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        bottomGradientParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomGradientMask.setLayoutParams(bottomGradientParams);
        bottomGradientMask.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(bottomGradientMask);

        indicatorBackground = new RelativeLayout(context);
        RelativeLayout.LayoutParams indicatorBackgroundParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        indicatorBackgroundParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorBackground.setLayoutParams(indicatorBackgroundParams);
        indicatorBackground.setBackgroundColor(style.getIndicatorBackgroundBgColor());
        addView(indicatorBackground);

        indicatorLinear = new LinearLayout(context);
        LinearLayout.LayoutParams indicatorLinearParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorLinear.setOrientation(LinearLayout.HORIZONTAL);
        indicatorLinear.setLayoutParams(indicatorLinearParams);
        indicatorLinear.setBackgroundColor(Color.TRANSPARENT);
        indicatorBackground.addView(indicatorLinear);

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

        indicatorHolder = new RelativeLayout(context);
        LinearLayout.LayoutParams indicatorHolderParams =
                new LinearLayout.LayoutParams(0, HOLDER_HEIGHT);
        indicatorHolderParams.weight = 50;
        int holderVMargin = (int)((CONTROLLER_HEIGHT - HOLDER_HEIGHT) / 2.0F);
        indicatorHolderParams.setMargins(0, holderVMargin, 0, holderVMargin);
        indicatorHolder.setLayoutParams(indicatorHolderParams);
        indicatorHolder.setBackgroundColor(Color.TRANSPARENT);
        indicatorLinear.addView(indicatorHolder);

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

        indicatorBuffer = new View(context);
        RelativeLayout.LayoutParams indicatorBufferParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        int bufferVMargin = (int)((HOLDER_HEIGHT - INDICATOR_HEIGHT) / 2.0F);
        int bufferHMargin = (int) (HOLDER_HEIGHT / 2.0F);
        indicatorBufferParams.setMargins(bufferHMargin, bufferVMargin, bufferHMargin, bufferVMargin);
        indicatorBuffer.setLayoutParams(indicatorBufferParams);
        indicatorBuffer.setBackgroundColor(style.getIndicatorBufferBgColor());
        indicatorBuffer.setAlpha(style.getIndicatorBufferAlpha());
        indicatorHolder.addView(indicatorBuffer);

        indicatorPlayback = new View(context);
        RelativeLayout.LayoutParams indicatorPlaybackParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        int playbackVMargin = (int)((HOLDER_HEIGHT - INDICATOR_HEIGHT) / 2.0F);
        int playbackHMargin = (int) (HOLDER_HEIGHT / 2.0F);
        indicatorPlaybackParams.setMargins(playbackHMargin, playbackVMargin, playbackHMargin, playbackVMargin);
        indicatorPlayback.setLayoutParams(indicatorPlaybackParams);
        indicatorPlayback.setBackgroundColor(style.getIndicatorPlaybackBgColor());
        indicatorHolder.addView(indicatorPlayback);

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

        thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(HOLDER_HEIGHT);
        thumb.setIntrinsicWidth(HOLDER_HEIGHT);
        thumb.setColorFilter(style.getIndicatorPlaybackBgColor(), PorterDuff.Mode.SRC_IN);
        indicatorSeekBar.setThumb(thumb);

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

    public void setStyle (SimplexStyle style) {
        this.style = style;
        indicatorLength.setBackgroundColor(style.getIndicatorLengthBgColor());
        indicatorLength.setAlpha(style.getIndicatorLengthAlpha());
        indicatorBuffer.setBackgroundColor(style.getIndicatorBufferBgColor());
        indicatorBuffer.setAlpha(style.getIndicatorBufferAlpha());
        indicatorPlayback.setBackgroundColor(style.getIndicatorPlaybackBgColor());
        thumb.setColorFilter(style.getIndicatorPlaybackBgColor(), PorterDuff.Mode.SRC_IN);
    }

    public void updateButtonPlaybackForState (Simplex.PlaybackState state) {

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
    // SeekBar listener methods
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
    // Set different listeners methods
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
