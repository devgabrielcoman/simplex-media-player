package com.gabrielcoman.simplexmediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class SimplexHolder extends FrameLayout {

    private int oldWidth = 0;
    private int oldHeight = 0;

    private SimplexHolderInterface listener;

    public SimplexHolder(Context context) {
        this(context, null, 0);
    }

    public SimplexHolder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplexHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        listener = new SimplexHolderInterface() {
            @Override public void didChangeLayout(int newWidth, int newHeight) {}};

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int newWidth = getMeasuredWidth();
                int newHeight = getMeasuredHeight();
                boolean shouldTrigger = shouldTriggerLayoutChange(newWidth, newHeight);

                if (shouldTrigger) {
                    oldWidth = newWidth;
                    oldHeight = newHeight;

                    listener.didChangeLayout(newWidth, newHeight);
                }
            }
        });

    }

    public boolean shouldTriggerLayoutChange (int newWidth, int newHeight) {
        return newWidth != oldWidth || newHeight != oldHeight;
    }

    public void setListener (SimplexHolderInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    public interface SimplexHolderInterface {
        void didChangeLayout(int newWidth, int newHeight);
    }
}
