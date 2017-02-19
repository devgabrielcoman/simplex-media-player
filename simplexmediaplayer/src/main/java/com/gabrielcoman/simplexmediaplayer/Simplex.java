package com.gabrielcoman.simplexmediaplayer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gabrielcoman.simplexmediaplayer.aux.time.SimplexTime;

import java.io.File;
import java.io.IOException;

public class Simplex extends Fragment implements
        SimplexHolder.SimplexHolderInterface,
        SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
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
    private int mBufferPercent = 0;

    private boolean isPrepared = false;
    private boolean isFirstTime = true;

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
                        controller.updateIndicatorPlaybackForProgress(1.0F);
                    }
                } else {
                    int currentTime;

                    try {
                        currentTime = mediaPlayer.getCurrentPosition();
                    } catch (Exception e) {
                        currentTime = mCurrentSeekPos;
                    }

                    float playbackPercent = currentTime / (float) mTotalDuration;
                    float bufferPercent = mBufferPercent / 100.0F;

                    try {
                        controller.updateIndicatorPlaybackForProgress(playbackPercent);
                        controller.updateIndicatorSeekBarForProgress(playbackPercent);
                        controller.updateInficatorCurrentTime(SimplexTime.getTimeString(currentTime));
                        controller.updateIndicatorBufferForProgress(bufferPercent);
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
            videoHolder.setBackgroundColor(Color.BLACK);
            videoHolder.setListener(this);

            // create the video view
            videoView = new SimplexVideoView(getActivity());
            videoView.getHolder().addCallback(this);
            videoHolder.addView(videoView);

            // create the controller
            controller = new SimplexController(getActivity());
            controller.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            controller.updateButtonPlaybackForState(state);
            controller.setButtonPlaybackClickListener(Simplex.this);
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

            mediaPlayer.setDisplay(holder);

            try {
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
            videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());

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
        controller.updateButtonPlaybackForState(state);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mBufferPercent = percent;
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

            // calc the new position
            int cPosition = (int) (percent * mTotalDuration);

            // play to the calculated position
            play(cPosition);

            // do a force update of the playback indicator
            try {
                controller.updateIndicatorPlaybackForProgress(percent);
            } catch (Exception e) {
                // do nothing
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Set data files
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setMedia (final String mediaName) {

        mediaHandler = new Handler(Looper.getMainLooper());
        mediaHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isPrepared && mediaHandler != null) {
                    mediaHandler.postDelayed(this, 250);
                } else {

                    try {
                        prepareMedia (mediaName);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }, 250);
    }

    private void prepareMedia (String mediaName) throws Throwable {

        Activity context = getActivity();

        if (context == null) {
            throw new Exception("Fragment not prepared yet!");
        }
        else {

            if (isFirstTime) {
                isFirstTime = false;
            } else {
                state = PlaybackState.PLAYING;
            }

            String mediaUrl;

            File file = new File(context.getFilesDir(), mediaName);
            if (file.exists()) {
                mediaUrl = file.toString();
                mBufferPercent = 100;
            } else if (Patterns.WEB_URL.matcher(mediaName).matches()){
                mediaUrl = mediaName;
                mBufferPercent = 0;
            } else {
                throw new Exception("Media inputted is neither a valid file on disk or a remote url!");
            }

            if (mediaPlayer == null) {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDisplay(videoView.getHolder());
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnBufferingUpdateListener(this);
            } else {
                mediaPlayer.reset();
            }

            mCurrentSeekPos = 0;

            try {
                mediaPlayer.setDataSource(mediaUrl);
            } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }

            try {
                mediaPlayer.prepare();
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }

            mTotalDuration = mediaPlayer.getDuration();
            controller.updateIndicatorTotalTime(SimplexTime.getTimeString(mTotalDuration));
            videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
            videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Internal control Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void play (int position) {
        if (mediaPlayer != null) {
            state = PlaybackState.PLAYING;
            controller.updateButtonPlaybackForState(state);
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
        }
    }

    private void pause () {
        if (mediaPlayer != null) {
            state = PlaybackState.PAUSED;
            mCurrentSeekPos = mediaPlayer.getCurrentPosition();
            controller.updateButtonPlaybackForState(state);
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
