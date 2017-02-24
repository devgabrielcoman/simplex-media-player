/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;
import com.gabrielcoman.simplexmediaplayer.aux.time.SimplexTime;
import com.gabrielcoman.simplexmediaplayer.aux.time.SimplexTimeAux;

import java.io.File;
import java.io.IOException;

/**
 * Class that represents the main Simplex Video Player
 */
public class Simplex extends Fragment implements
        SimplexHolder.Listener,
        SimplexVideoView.Listener,
        SimplexMediaPlayer.Listener,
        SimplexController.Listener
{

    // the main subviews of the simplex player:
    // - the video holder (parent to all other subviews)
    // - the special Simplex video view
    // - the special media player
    // - the special Simplex controller
    private SimplexHolder       videoHolder         = null;
    private SimplexVideoView    videoView           = null;
    private SimplexMediaPlayer  mediaPlayer         = null;
    private SimplexController   controller          = null;

    // two handlers to check on video progress and
    // whether the media should be played or not
    private Handler             progressHandler     = null;
    private Handler             mediaHandler        = null;

    // state vars keeping track of whether this is the
    // first time the player is being called
    private boolean             isFirstTime         = true;

    // local instance of a style that the users could interact with
    private SimplexStyle        style               = new SimplexStyle();

    // var holding whether the controller is visible
    private boolean             isControllerVisible = true;

    // private listener
    private Listener            listener;

    // private enum holding the internal simplified state of the player
    enum PlaybackState {
        NOTSTARTED,
        AUTOSTART,
        PAUSED,
        PLAYING,
        REWIND
    }
    // and a var of type PlaybackState to hold the current state
    private PlaybackState   state = PlaybackState.NOTSTARTED;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Fragment
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Basic default Fragment constructor, that just instantiates a new listener
     */
    public Simplex () {
        listener = new Listener() {
            @Override public void didStart() {}
            @Override public void didPause() {}
            @Override public void didComplete() {}
            @Override public void didError() {}
            @Override public void didClose () {}
            @Override public void didUpdateBuffer (float percent) {}
            @Override public void didUpdatePlayback (int hour, int minute, int second) {}};
    }

    /**
     * Overridden Fragment "onCreate" method that will only be called once.
     *
     * @param savedInstanceState the previous saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // call to super
        super.onCreate(savedInstanceState);

        // retain the instance between screen transitions
        setRetainInstance(true);

        // start a progress handler the runs every 250ms to ensure all the UI elements have
        // been properly updated
        progressHandler = new Handler(Looper.getMainLooper());
        progressHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (state == PlaybackState.REWIND) {
                    try {
                        controller.updatePlayback(1.0F);
                        controller.updateButtonPlaybackForState (state);
                    } catch (Exception e) {
                        // do nothing
                    }
                } else {

                    int currentTime = 0;

                    try {
                        currentTime = mediaPlayer.getCurrentPosition();
                    } catch (Exception e) {
                        // do nothing
                    }

                    int totalDuration = 0;

                    try {
                        totalDuration = mediaPlayer.getTotalDuration();
                    } catch (Exception e) {
                        // do nothing
                    }

                    // get a version of the current time - either from the media player
                    // or from a saved previous one
                    float playbackPercent = 0;

                    try {
                        playbackPercent = currentTime / (float) totalDuration;
                    } catch (Exception e) {
                        // do nothing
                    }

                    // and the buffer percent
                    float bufferPercent = 0;

                    try {
                        bufferPercent = mediaPlayer.getBufferPercent();
                    } catch (Exception e) {
                        // do nothing
                    }

                    // get time
                    SimplexTime time = SimplexTimeAux.getTime(currentTime);

                    // call listener
                    if (state == PlaybackState.PLAYING) {
                        listener.didUpdateBuffer(bufferPercent);
                        listener.didUpdatePlayback(time.getHour(), time.getMinute(), time.getSecond());
                    }

                    // and finally update all the UI
                    try {
                        controller.updatePlayback(playbackPercent);
                        controller.updateThumb(playbackPercent);
                        controller.updateCurrentTime(time);
                        controller.updateTotalTime(time);
                        controller.updateBuffer(bufferPercent);
                        controller.updateButtonPlaybackForState (state);
                    } catch (Exception e) {
                        // do nothing
                    }
                }

                if (progressHandler != null) {
                    progressHandler.postDelayed(this, 150);
                }
            }
        }, 150);
    }

    /**
     * Overridden Fragment "onCreateView" method that gets called everytime the screen re-orients
     * itself. This will create all the subviews of the video player
     *
     * @param inflater           current inflater
     * @param container          current container
     * @param savedInstanceState the previously saved instance state
     * @return                   the view to be displayed
     */
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
            videoView.setListener(this);
            videoHolder.addView(videoView);

            // create & parametrise the controller
            controller = new SimplexController(getActivity());
            controller.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            controller.setListener(this);
            controller.updateStyle(style);
            controller.setVisibility(isControllerVisible ? View.VISIBLE : View.GONE);
            videoHolder.addView(controller);

        } else {
            if (container != null) {
                container.removeView(videoHolder);
            } else {
                ViewGroup p = (ViewGroup) videoHolder.getParent();
                p.removeView(videoHolder);
            }
        }

        return videoHolder;
    }

    /**
     * Overridden Fragment method that gets called when the player gets destroyed
     *
     */
    @Override
    public void onDestroy() {

        // call to super
        super.onDestroy();

        // remove the progress handler
        if (progressHandler != null) {
            progressHandler = null;
        }

        // remove the media handler
        if (mediaHandler != null) {
            mediaHandler = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Surface & Video View
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Overridden Listener listener method that will get called
     * every time the SimplexVideoView surface gets re-created.
     *
     * @param holder current surface holder to feed into the media player
     */
    @Override
    public void videoViewCreated(SurfaceHolder holder) {

        // if the media player is valid and not null
        if (mediaPlayer != null && videoView != null) {

            // set it's display as the new holder
            mediaPlayer.setDisplay(holder);

            // and try to prepare it
            try {
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // also set the video size for the video view
            videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());

            // and resize the video view to a certain container
            videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());

        }
    }

    /**
     * Overridden Listener listener method that will get called
     * every time the SimplexVideoView surface gets destroyed.
     * This usually happens on an orientation change or when the activity gets put in the
     * background by another one.
     *
     * @param holder current surface holder
     */
    @Override
    public void videoViewDestroyed(SurfaceHolder holder) {

        // only if the media player is not null
        if (mediaPlayer != null) {

            // stop the media player
            mediaPlayer.stop();

        }
    }

    /**
     * Overridden SimplexHolder listener that will get called every time the screen re-orients
     * itself (and the video changes size) or the video simply changes size.
     *
     * @param newWidth  the new width of the holder
     * @param newHeight the new height of the holder
     */
    @Override
    public void didChangeLayout(int newWidth, int newHeight) {

        // if the media player is not null
        if (mediaPlayer != null && videoView != null) {
            // resize the video view to fit the new container
            videoView.resizeToContainer(newWidth, newHeight);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Media Player
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Overridden SimplexMediaPlayer.Listener onPrepared method
     * Here, depending on the state of the video player, multiple things can happen
     * - either the video continues (or starts)
     * - or it just seeks to a position
     */
    @Override
    public void onPrepared() {

        switch (state) {

            // when auto-starting or playing, just call the internal "play(toPos)" method
            case AUTOSTART:
            case PLAYING: {
                play(mediaPlayer.getLastPosition());
                break;
            }
            // when pausing, always seek to the current pos (which should not change)
            case PAUSED: {
                mediaPlayer.seekToLastPosition();
                break;
            }
            // when rewinding, always seek to the total duration
            case REWIND: {
                mediaPlayer.seekToEnd();
                break;
            }
            // when it's not started
            case NOTSTARTED: {
                // do nothing
                break;
            }
        }

    }

    /**
     * Overridden SimplexMediaPlayer.Listener onCompletion method
     * Here the media player pauses and the state goes to "rewind"
     */
    @Override
    public void onCompletion() {

        // call listener
        listener.didComplete();

        // pause & save last position
        mediaPlayer.pause();

        // rewind
        state = PlaybackState.REWIND;
    }

    /**
     * Overridden SimplexMediaPlayer.Listener onError method
     *
     */
    @Override
    public void onError() {
        // call listener
        listener.didError();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Click on different buttons in the Player
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Overridden OnClickListener method used to interact with the playback button.
     */
    @Override
    public void didClickOnPlaybackButton() {
        switch (state) {
            // if the video hasn't started or it's paused, start playing
            case NOTSTARTED:
            case PAUSED: {
                play(mediaPlayer.getLastPosition());
                break;
            }
            // if the video is playing, pause it
            case PLAYING: {
                pause();
                break;
            }
            // if the video is rewinding, pause from start
            case REWIND: {
                play(0);
                break;
            }
            // else do nothing
            case AUTOSTART: {
                // do nothing
                break;
            }
        }
    }

    /**
     * Overridden SimplexController method that gets called at the end of a user drag action
     * on the seek bar. This informs the video player that it should start playing from a
     * certain position.
     *
     * @param percent percent to advance to
     */
    @Override
    public void shouldAdvanceToPercent(float percent) {

        // only if we have a valid media player
        if (mediaPlayer != null) {

            // position
            int position = (int) (percent * mediaPlayer.getTotalDuration());

            // play from resulting position
            play(position);

        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Set data files
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Main public method through which either an URL or a file name are set as sources for
     * the player. From here on, if it's prepared and it's set to auto-play, the player will
     * begin playing.
     *
     * @param mediaName URL or file name
     */
    public void setMedia (final String mediaName) {

        // start a handler that will only fire the internal method if the
        // video view is prepared
        mediaHandler = new Handler(Looper.getMainLooper());
        mediaHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!videoView.isSurfaceCreated() && mediaHandler != null) {
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

    /**
     * Internal method that tries to start a new media player (or reset a new one), set it's
     * media player url and start preparing it
     *
     * @param mediaName     URL or file name
     * @throws Throwable    this throws an error
     */
    private void prepareMedia (String mediaName) throws Throwable {

        // get current activity or context
        Activity context = getActivity();

        if (context == null) {
            throw new Exception("Fragment not prepared yet!");
        }
        else {

            // handle a state at the begining of playing
            if (isFirstTime) {
                isFirstTime = false;
            } else {
                state = PlaybackState.PLAYING;
            }

            // create or reset the media player
            if (mediaPlayer == null) {
                mediaPlayer = new SimplexMediaPlayer();
                mediaPlayer.setDisplay(videoView.getHolder());
                mediaPlayer.setListener(this);
            } else {
                mediaPlayer.reset();
            }

            // determine what type of media Url we're talking about
            String mediaUrl;

            File file = new File(context.getFilesDir(), mediaName);
            if (file.exists()) {
                mediaUrl = file.toString();
                mediaPlayer.setOfflineBuffer();
            } else if (Patterns.WEB_URL.matcher(mediaName).matches()){
                mediaUrl = mediaName;
                mediaPlayer.setOnlineBuffer();
            } else {
                throw new Exception("Media inputted is neither a valid file on disk or a remote url!");
            }

            // try to set the data source
            try {
                mediaPlayer.setDataSource(mediaUrl);
            } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }

            // try to prepare the media
            try {
                mediaPlayer.prepare();
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }

            // and update the video size and container
            try {
                videoView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
                videoView.resizeToContainer(videoHolder.getMeasuredWidth(), videoHolder.getMeasuredHeight());
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Internal control Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Private "Play" method. It makes the media player seek to the specified position and updates
     * the current state
     *
     * @param position the position to play to.
     */
    private void play (int position) {
        if (mediaPlayer != null) {

            // call listener
            listener.didStart();

            // set state to "playing"
            state = PlaybackState.PLAYING;

            // seek to
            mediaPlayer.playFrom(position);
        }
    }

    /**
     * Private "Pause" method. It makes the media player pause and updates the current state
     */
    private void pause () {
        if (mediaPlayer != null) {

            // call listener
            listener.didPause();

            // set the state to "paused"
            state = PlaybackState.PAUSED;

            // pause
            mediaPlayer.pause();
        }
    }

    /**
     * Public "Close" method. It deletes everything
     */
    public void close () {

        // call listener
        listener.didClose();

        // remove the video view
        videoHolder.removeView(videoView);
        // and controller
        videoHolder.removeView(controller);

        // and if the media player is OK, remove that too
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

    /**
     * Public setter that sets whether the video should auto-start
     */
    public void shouldAutoStart () {
        state = PlaybackState.AUTOSTART;
    }

    /**
     * Hiden controller for a minimalist feel
     */
    public void hideController () {
        isControllerVisible = false;
        if (controller != null) {
            controller.setVisibility(View.GONE);
        }
    }

    /**
     * Setter for the style
     *
     * @param style new style
     */
    public void setStyle (SimplexStyle style) {
        this.style = style;
        if (controller != null) {
            controller.updateStyle(style);
        }
    }

    /**
     * Setter for the listener
     *
     * @param listener new listener instance
     */
    public void setListener (Listener listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface for the video player
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Public interface of the Simplex player
     */
    public interface Listener {

        /**
         * Called when a media file gets prepared and starts playing
         */
        void didStart ();

        /**
         * Called when a media file is paused
         */
        void didPause ();

        /**
         * Called when a media file is completed
         */
        void didComplete ();

        /**
         * Called when a media file errors
         */
        void didError ();

        /**
         * Called when the player closes alltogether
         */
        void didClose ();

        /**
         * Called when the buffer for a media is updaed
         *
         * @param percent of update, from 0 to 1
         */
        void didUpdateBuffer (float percent);

        /**
         * Called when the playback is updated to (hour / minute / second) of playing
         *
         * @param hour   hour of playback update
         * @param minute minute of playback update
         * @param second second of playback update
         */
        void didUpdatePlayback (int hour, int minute, int second);
    }
}
