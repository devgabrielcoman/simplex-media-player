package com.gabrielcoman.simplexmediaplayer;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaDataSource;
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
import java.io.FileDescriptor;
import java.io.IOException;

public class Simplex extends Fragment implements
        SimplexHolder.SimplexHolderInterface,
        SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        View.OnClickListener,
        SimplexController.ProgressIndicatorInterface
{

    private SimplexHolder videoHolder;
    private SimplexVideoView videoView;
    private MediaPlayer mediaPlayer;
    private SimplexController controller;

    private Handler progressHandler;
    private Handler mediaHandler;

    private int mCurrentSeekPos = 0;
    private int mTotalDuration = 1;

    private boolean isPrepared = false;
    private String mMediaFileToPlay = null;

    public enum PlaybackState {
        NOTSTARTED,
        AUTOSTART,
        PAUSED,
        PLAYING,
        REWIND
    }
    private PlaybackState state = PlaybackState.NOTSTARTED;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Simplex () {
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setOnPreparedListener(this);
//        mediaPlayer.setOnErrorListener(this);
//        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        progressHandler = new Handler(Looper.getMainLooper());
        progressHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (state == PlaybackState.REWIND) {
                    if (controller != null) {
                        controller.setPlaybackIndicatorPercent(1.0F);
                    }
                } else {
                    float percent;

                    try {
                        percent = mediaPlayer.getCurrentPosition() / (float) mTotalDuration;
                    } catch (Exception e) {
                        percent = mCurrentSeekPos / (float) mTotalDuration;
                    }

                    try {
                        controller.setPlaybackIndicatorPercent(percent);
                    } catch (Exception e) {
                        // do nothing
                    }
                }

                if (progressHandler != null) {
                    progressHandler.postDelayed(this, 250);
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
            controller.hideStartButton(state == PlaybackState.AUTOSTART);
            controller.setPlaybackButtonForState(state);
            controller.setPlaybackButtonClickListener(Simplex.this);
            controller.setStartButtonClickListener(Simplex.this);
            controller.setListener(this);
            videoHolder.addView(controller);

        } else {
            container.removeView(videoHolder);
        }

        return videoHolder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressHandler != null) {
            progressHandler = null;
        }
        if (mediaHandler != null) {
            mediaHandler = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Surface & Video View
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // set is prepared to true
        isPrepared = true;

        if (mediaPlayer != null) {
////
////            Log.d("SIMPLEX", "Holder here is " + holder);
////
            mediaPlayer.setDisplay(holder);
//////
            try {
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
////
            videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
            videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());
//
        }
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
        if (mediaPlayer != null) {
            videoView.resizeToContainer(newWidth, newHeight);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Media Player
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.d("SIMPLEX", "Duration " + mp.getDuration() + " , " + mp.getVideoWidth() + " CPOS " + mCurrentSeekPos);

        switch (state) {

            case AUTOSTART:
            case PLAYING: {
                play(mCurrentSeekPos);
                break;
            }
            case PAUSED: {
                mp.seekTo(mCurrentSeekPos);
                break;
            }
            case REWIND: {
                mp.seekTo(mTotalDuration);
                break;
            }
            case NOTSTARTED: {
                // do nothing
                break;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        mp.pause();
        state = PlaybackState.REWIND;
        controller.setPlaybackButtonForState(state);
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
            switch (state) {
                case NOTSTARTED:
                case PAUSED: {
                    play(mCurrentSeekPos);
                    break;
                }
                case PLAYING: {
                    pause();
                    break;
                }
                case REWIND: {
                    play(0);
                    break;
                }
                case AUTOSTART: {
                    // do nothing
                    break;
                }
            }
        }
    }

    @Override
    public void shouldAdvanceToStep(float percent) {

        if (mediaPlayer != null) {
            int cPosition = (int) (percent * mTotalDuration);
            play(cPosition);
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Control Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setMediaFile(String path) {

        mMediaFileToPlay = path;

        mediaHandler = new Handler(Looper.getMainLooper());
        mediaHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isPrepared && mediaHandler != null) {
                    mediaHandler.postDelayed(this, 250);
                } else {

                    try {
                        prepareMedia(mMediaFileToPlay);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }, 250);
    }

    private void prepareMedia (String path) throws Throwable {

        if (path == null) {
            throw new Exception("No media file set to play. Please use 'setMediaFile' to set a valid file path");
        }
        else {
            Activity current = getActivity();
            if (current == null) {
                throw new Exception("Fragment not prepared yet!");
            }
            else {
                File file = new File(current.getFilesDir(), path);
                Log.d("SuperAwesome", "FILE IS: file://" + Uri.parse(file.toString()).toString());
                if (!file.exists()) {
                    throw new Exception("File " + path + " does not exist on disk. Will not set this media file!");
                }
                else {
                    String videoURL = file.toString();

                    if (mediaPlayer == null) {

                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDisplay(videoView.getHolder());
                        mediaPlayer.setOnPreparedListener(this);
                        mediaPlayer.setOnErrorListener(this);
                        mediaPlayer.setOnCompletionListener(this);
                    } else {
                        mediaPlayer.reset();
                    }

                    mCurrentSeekPos = 0;

                    try {
                        mediaPlayer.setDataSource(current, Uri.parse(videoURL));
                    } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        mediaPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mTotalDuration = mediaPlayer.getDuration();
                    videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
                    videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());

                    Log.d("SIMPLEX", "Duration " + mediaPlayer.getDuration() + " , " + mediaPlayer.getVideoWidth());
                }
            }
        }
    }

    private void play (int position) {
        if (mediaPlayer != null) {
            state = PlaybackState.PLAYING;
            controller.setPlaybackButtonForState(state);
            controller.hideStartButton(true);
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
        }
    }

    private void pause () {
        if (mediaPlayer != null) {
            state = PlaybackState.PAUSED;
            mCurrentSeekPos = mediaPlayer.getCurrentPosition();
            controller.setPlaybackButtonForState(state);
            mediaPlayer.pause();
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
        state = PlaybackState.AUTOSTART;
    }
}
