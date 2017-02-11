package com.gabrielcoman.simplexmediaplayer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;

public class Simplex extends Fragment implements
        SimplexHolder.SimplexHolderInterface,
        SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        View.OnClickListener
{

    private SimplexHolder videoHolder;
    private SimplexVideoView videoView;
    private MediaPlayer mediaPlayer;
    private SimplexController controller;

    private Handler timerHandler;

    private int mCurrentSeekPos = 0;
    private int mTotalDuration = 1;

    private SimplexInterface listener = null;

    private Boolean isStarted = false;
    private Boolean isPaused = false;
    private Boolean autostart = false;

    public Simplex () {
        listener = new SimplexInterface() {
            @Override public void didReceiveEvent(SimplexEvent event) {}};
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        timerHandler = new Handler(Looper.getMainLooper());
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                float percent;

                if (mediaPlayer != null) {
                    percent = mediaPlayer.getCurrentPosition() / (float) mTotalDuration;
                } else {
                    percent = mCurrentSeekPos / (float) mTotalDuration;
                }

                if (controller != null) {
                    controller.setPlaybackIndicatorPercent(percent);
                }

                if (timerHandler != null) {
                    timerHandler.postDelayed(this, 250);
                }
            }
        }, 250);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (videoHolder == null) {
            // create the video holder
            videoHolder = new SimplexHolder(getActivity());
            videoHolder.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            videoHolder.setBackgroundColor(Color.RED);
            videoHolder.setListener(this);

            // create the video view
            videoView = new SimplexVideoView(getActivity());
            videoView.getHolder().addCallback(this);
            videoHolder.addView(videoView);

            // create the controller
            controller = new SimplexController(getActivity());
            controller.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            controller.hideStartButton(autostart);
            controller.setPlaybackButtonPlay(autostart);
            controller.setPlaybackButtonClickListener(Simplex.this);
            controller.setStartButtonClickListener(Simplex.this);
            videoHolder.addView(controller);

            listener.didReceiveEvent(SimplexEvent.Prepared);

        } else {
            container.removeView(videoHolder);
        }

        return videoHolder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerHandler != null) {
            timerHandler = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Surface & Video View
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mediaPlayer == null) return;

        mediaPlayer.setDisplay(holder);

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTotalDuration = mediaPlayer.getDuration();
        videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
        videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mCurrentSeekPos = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
    }

    @Override
    public void didChangeLayout(int newWidth, int newHeight) {
        videoView.resizeToContainer(newWidth, newHeight);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Media Player
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onPrepared(MediaPlayer mp) {

        if (!isStarted && !autostart) return;

        mp.seekTo(mCurrentSeekPos);
        mp.start();
        if (isPaused) {
            mp.pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        videoHolder.removeView(videoView);

        mp.stop();
        mp.setDisplay(null);
        mp.release();
        mediaPlayer = null;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Click on different buttons in the Player
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                pause();
            }
            else {
                play();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Control Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setMediaFile(String path) throws Throwable {

        Activity current = getActivity();
        if (current == null) {
            throw new Exception("Fragment not prepared yet! Await the 'Video_Prepared' event in order to setMediaFile.");
        } else {
            File file = new File(current.getFilesDir(), path);
            if (file.exists()) {

                String videoURL = file.toString();

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(current, Uri.parse(videoURL));

                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnCompletionListener(this);

            } else {
                throw new Exception("File " + path + " does not exist on disk. Will not setMediaFile!");
            }
        }
    }

    private void play () {
        if (mediaPlayer != null) {
            controller.setPlaybackButtonPlay(true);
            controller.hideStartButton(true);
            mediaPlayer.start();
            isStarted = true;
            isPaused = false;
        }
    }

    private void pause () {
        if (mediaPlayer != null) {
            controller.setPlaybackButtonPlay(false);
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    public void close () {

        videoHolder.removeView(videoView);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.setDisplay(null);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Setters & Getters
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void shouldAutostart () {
        autostart = true;
        isPaused = false;
    }

    public void setListener (SimplexInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface listener
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface SimplexInterface {
        void didReceiveEvent (SimplexEvent event);
    }
}
