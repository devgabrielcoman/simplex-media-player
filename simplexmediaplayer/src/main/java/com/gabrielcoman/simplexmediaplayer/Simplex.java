package com.gabrielcoman.simplexmediaplayer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;

public class Simplex extends Fragment implements SimplexHolder.SimplexHolderInterface, SurfaceHolder.Callback {

    private SimplexHolder videoHolder;
    private SimplexVideoView videoView;
    private MediaPlayer mediaPlayer;

    private int mCurrentSeekPos = 0;

    private SimplexInterface listener = null;

    public Simplex () {
        listener = new SimplexInterface() {
            @Override public void didReceiveEvent(SimplexEvent event) {}};
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

            listener.didReceiveEvent(SimplexEvent.Prepared);

        } else {
            container.removeView(videoHolder);
        }

        return videoHolder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mediaPlayer == null) return;

        mediaPlayer.setDisplay(holder);

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void play (String path) throws Throwable {

        Activity current = getActivity();
        if (current == null) {
            throw new Exception("Fragment not prepared yet! Await the 'Video_Prepared' event in order to play.");
        } else {
            File file = new File(current.getFilesDir(), path);
            if (file.exists()) {

                String videoURL = file.toString();

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(current, Uri.parse(videoURL));

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mp) {

                        mp.start();

                        if (mCurrentSeekPos != 0) {
                            mp.seekTo(mCurrentSeekPos);
                            mp.start();
                        }

                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        videoHolder.removeView(videoView);

                        mp.stop();
                        mp.setDisplay(null);
                        mp.release();
                        mediaPlayer = null;
                    }
                });

            } else {
                throw new Exception("File " + path + " does not exist on disk. Will not play!");
            }
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

    public void setListener (SimplexInterface listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    public interface SimplexInterface {
        void didReceiveEvent (SimplexEvent event);
    }
}
