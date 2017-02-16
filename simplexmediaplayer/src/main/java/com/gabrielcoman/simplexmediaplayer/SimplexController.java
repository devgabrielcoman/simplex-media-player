package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SimplexController extends RelativeLayout implements View.OnTouchListener {

    private Button startButton;
    private RelativeLayout controller;
    private Button playbackButton;
    private RelativeLayout progressHolder;
    private View progressIndicator;
    private View bufferIndicator;

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

        startButton = new Button(context);
        RelativeLayout.LayoutParams startButtonParams = new RelativeLayout.LayoutParams(100, 100);
        startButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        startButton.setLayoutParams(startButtonParams);
        startButton.setBackgroundColor(Color.GREEN);
        startButton.setText(">");
        addView(startButton);

        controller = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        controller.setLayoutParams(layoutParams);
        controller.setBackgroundColor(Color.BLUE);
        addView(controller);

        playbackButton = new Button(context);
        playbackButton.setBackgroundColor(Color.GREEN);
        playbackButton.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        controller.addView(playbackButton);

        progressHolder = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.setMargins(110, 10, 10, 10);
        progressHolder.setLayoutParams(layoutParams1);
        progressHolder.setBackgroundColor(Color.MAGENTA);
        progressHolder.setOnTouchListener(this);
        controller.addView(progressHolder);

        bufferIndicator = new View(context);
        bufferIndicator.setLayoutParams(new ViewGroup.LayoutParams(0, 80));
        bufferIndicator.setBackgroundColor(Color.CYAN);
        progressHolder.addView(bufferIndicator);

        progressIndicator = new View(context);
        progressIndicator.setLayoutParams(new ViewGroup.LayoutParams(0, 80));
        progressIndicator.setBackgroundColor(Color.YELLOW);
        progressHolder.addView(progressIndicator);

    }

    public void setPlaybackButtonClickListener(OnClickListener listener) {
        playbackButton.setOnClickListener(listener);
    }

    public void setStartButtonClickListener(OnClickListener listener) {
        startButton.setOnClickListener(listener);
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
                playbackButton.setText("||");
                break;
            }
            case REWIND: {
                playbackButton.setText("R");
                break;
            }
        }
    }

    public void hideStartButton (boolean hidden) {
        startButton.setVisibility(hidden ? GONE : VISIBLE);
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
