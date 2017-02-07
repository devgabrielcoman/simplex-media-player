package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SimplexController extends RelativeLayout {

    private RelativeLayout controller;
    private Button playbackButton;

    public SimplexController(Context context) {
        this(context, null, 0);
    }

    public SimplexController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplexController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
        setPlaybackButtonPause();
    }

    public void setPlaybackButtonClickListener(OnClickListener listener) {
        playbackButton.setOnClickListener(listener);
    }

    public void setPlaybackButtonPlay () {
        playbackButton.setText(">");
    }

    public void setPlaybackButtonPause () {
        playbackButton.setText("||");
    }

}
