package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.VideoView;

public class SimplexVideoView extends VideoView {

    private int videoWidth = 0;
    private int videoHeight = 0;

    public SimplexVideoView(Context context) {
        this(context, null, 0);
    }

    public SimplexVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplexVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize (int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
    }

    public void resizeToContainer (int containerWidth, int containerHeight) {
        float videoRatio = videoWidth / (float) videoHeight;
        float containerRatio = containerWidth / (float) containerHeight;
        float X, Y, W, H;
        if (videoRatio > containerRatio) {
            W = containerWidth;
            H = W / videoRatio;
            X = 0.0F;
            Y = (containerHeight - H) / 2.0f;
        } else {
            H = containerHeight;
            W = videoRatio * H;
            Y = 0.0f;
            X = (containerWidth - W) / 2.0f;
        }

        FrameLayout.LayoutParams newParams = new FrameLayout.LayoutParams((int)W, (int)H);
        newParams.setMargins((int)X, (int)Y, 0, 0);

        setLayoutParams(newParams);
    }
}
