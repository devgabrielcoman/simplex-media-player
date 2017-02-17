package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gabrielcoman.simplexmediaplayer.aux.image.SimplexBitmap;

public class SimplexController extends RelativeLayout implements View.OnTouchListener {

    // some constants
    private static final int CONTROLLER_HEIGHT = 80;
    private static final int MASK_HEIGHT = (int) (1.25F * CONTROLLER_HEIGHT);

    private Button playbackButton;

    private RelativeLayout controller;
    private RelativeLayout progressHolder;
    private View progressIndicator;
    private View bufferIndicator;

    private ImageView mask;

    private TextView currentTime;
    private TextView totalTime;

    private ProgressIndicatorInterface listener;

    public SimplexController(Context context) {
        this(context, null, 0);
        listener = new ProgressIndicatorInterface() {
            @Override public void shouldAdvanceToStep(float percent) {}};
    }

    public SimplexController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplexController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        playbackButton = new Button(context);
        RelativeLayout.LayoutParams startButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        startButtonParams.setMargins(0, 0, 0, CONTROLLER_HEIGHT);
        playbackButton.setLayoutParams(startButtonParams);
        playbackButton.setBackgroundColor(Color.TRANSPARENT);
        playbackButton.setText(">");
        playbackButton.setTextColor(Color.WHITE);
        playbackButton.setTextSize(48);
        addView(playbackButton);

        mask = new ImageView(context);
        mask.setImageBitmap(SimplexBitmap.createVideoGradientBitmap());
        RelativeLayout.LayoutParams backgroundParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MASK_HEIGHT);
        backgroundParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mask.setLayoutParams(backgroundParams);
        mask.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mask);

        controller = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CONTROLLER_HEIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        controller.setLayoutParams(layoutParams);
        controller.setBackgroundColor(Color.TRANSPARENT);
        controller.setOnTouchListener(this);
        addView(controller);

        currentTime = new TextView(context);
        currentTime.setBackgroundColor(Color.TRANSPARENT);
        currentTime.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));
        currentTime.setText("00:00");
        currentTime.setTextColor(Color.WHITE);
        currentTime.setGravity(Gravity.CENTER);
        controller.addView(currentTime);

        progressHolder = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.setMargins(110, 10, 110, 10);
        progressHolder.setLayoutParams(layoutParams1);
        progressHolder.setBackgroundColor(Color.MAGENTA);
        progressHolder.setOnTouchListener(this);
        controller.addView(progressHolder);

        totalTime = new TextView(context);
        totalTime.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams totalTimeParams = new RelativeLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT);
        totalTimeParams.addRule(ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        totalTime.setLayoutParams(totalTimeParams);
        totalTime.setText("00:00");
        totalTime.setTextColor(Color.WHITE);
        totalTime.setGravity(Gravity.CENTER);
        controller.addView(totalTime);

        bufferIndicator = new View(context);
        bufferIndicator.setLayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
        bufferIndicator.setBackgroundColor(Color.CYAN);
        progressHolder.addView(bufferIndicator);

        progressIndicator = new View(context);
        progressIndicator.setLayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
        progressIndicator.setBackgroundColor(Color.YELLOW);
        progressHolder.addView(progressIndicator);

    }

    public void setPlaybackButtonClickListener(OnClickListener listener) {
        playbackButton.setOnClickListener(listener);
    }

    public void setPlaybackButtonForState (Simplex.PlaybackState state) {

        switch (state) {
            case NOTSTARTED:
            case PAUSED: {
                playbackButton.setText(">");
                break;
            }
            case AUTOSTART:
            case PLAYING: {
                playbackButton.setText(" ");
                break;
            }
            case REWIND: {
                playbackButton.setText("R");
                break;
            }
        }
    }

    public void setPlaybackIndicatorPercent (float percent) {

        int measuredWidth = progressHolder.getMeasuredWidth();
        int percentWidth = (int) (percent * measuredWidth);
        ViewGroup.LayoutParams params = progressIndicator.getLayoutParams();
        params.width = percentWidth;
        progressIndicator.setLayoutParams(params);

    }

    public void setBufferIndicatorPercent (float percent) {
        int measuredWidth = progressHolder.getMeasuredWidth();
        int percentWidth = (int) (percent * measuredWidth);
        ViewGroup.LayoutParams params = bufferIndicator.getLayoutParams();
        params.width = percentWidth;
        bufferIndicator.setLayoutParams(params);
    }

    public void setTotalTimeText (String text) {
        totalTime.setText(text);
    }

    public void setCurrentTime (String text) {
        currentTime.setText(text);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float current = event.getX();
        int totalSize = progressHolder.getMeasuredWidth();
        float percent = current / (float) totalSize;
        percent = percent > 1 ? 1 : percent;
        percent = percent < 0 ? 0 : percent;
        listener.shouldAdvanceToStep(percent);

        return false;
    }

    public void setListener (ProgressIndicatorInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    interface ProgressIndicatorInterface {
        void shouldAdvanceToStep (float percent);
    }
}
