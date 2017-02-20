/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;
import com.gabrielcoman.simplexmediaplayer.aux.time.SimplexTime;

import java.io.File;
import java.io.IOException;

/**
 * Class that represents the main Simplex Video Player
 */
public class Simplex extends Fragment implements
        SimplexHolder.Listener,
        SimplexVideoView.Listener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        SimplexController.Listener
{

    // the main subviews of the simplex player:
    // - the video holder (parent to all other subviews)
    // - the special Simplex video view
    // - the special media player
    // - the special Simplex controller
    private SimplexHolder     videoHolder         = null;
    private SimplexVideoView  videoView           = null;
    private MediaPlayer       mediaPlayer         = null;
    private SimplexController controller          = null;

    // two handlers to check on video progress and
    // whether the media should be played or not
    private Handler           progressHandler     = null;
    private Handler           mediaHandler        = null;

    // state vars containing the current seek pos, duration and percent of the buffer
    // that's been loaded
    private int               currentSeekPos = 0;
    private int               totalDuration = 1;
    private int               bufferPercent = 0;

    // state vars keeping track of whether this is the
    // first time the player is being called
    private boolean           isFirstTime         = true;

    // local instance of a style that the users could interact with
    private SimplexStyle      style               = new SimplexStyle();

    // var holding whether the controller is visible
    private boolean           isControllerVisible = true;

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

                    // get a version of the current time - either from the media player
                    // or from a saved previous one
                    int currentTime;

                    try {
                        currentTime = mediaPlayer.getCurrentPosition();
                    } catch (Exception e) {
                        currentTime = currentSeekPos;
                    }

                    // get the playback percent
                    float playbackPercent = currentTime / (float) totalDuration;
                    // and the buffer percent
                    float bufferPercent = Simplex.this.bufferPercent / 100.0F;

                    // and finally update all the UI
                    try {
                        controller.updatePlayback(playbackPercent);
                        controller.updateThumb(playbackPercent);
                        controller.updateCurrentTime(SimplexTime.getTimeString(currentTime));
                        controller.updateTotalTime(SimplexTime.getTimeString(totalDuration));
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
            container.removeView(videoHolder);
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

            // update the current seek pos to know where to return to later
            currentSeekPos = mediaPlayer.getCurrentPosition();

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
     * Overridden MediaPlayer onPrepared method. This will get called every time the media player
     * will be properly prepared.
     *
     * @param mp current media player
     */
    @Override
    public void onPrepared(MediaPlayer mp) {

        switch (state) {

            // when auto-starting or playing, just call the internal "play(toPos)" method
            case AUTOSTART:
            case PLAYING: {
                play(currentSeekPos);
                break;
            }
            // when pausing, always seek to the current pos (which should not change)
            case PAUSED: {
                mp.seekTo(currentSeekPos);
                break;
            }
            // when rewinding, always seek to the total duration
            case REWIND: {
                mp.seekTo(totalDuration);
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
     * verridden MediaPlayer onCompletion method.
     *
     * @param mp current media player
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

        // pause the video
        mp.pause();

        // put the state in rewind mode
        state = PlaybackState.REWIND;
    }

    /**
     * verridden MediaPlayer onError method. Not implemented.
     *
     * @param mp    current media player
     * @param what  what the error's about
     * @param extra extra info
     * @return      whether to propagate the error or not
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    /**
     * Overridden MediaPlayer onBufferingUpdate method.
     * This will just update the state "bufferPercent" variable
     *
     * @param mp        current media player
     * @param percent   the percent of the media buffer that's full
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bufferPercent = percent;
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
                play(currentSeekPos);
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

            // calc the new position
            int cPosition = (int) (percent * totalDuration);

            // play to the calculated position
            play(cPosition);
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

            // determine what type of media Url we're talking about
            String mediaUrl;

            File file = new File(context.getFilesDir(), mediaName);
            if (file.exists()) {
                mediaUrl = file.toString();
                bufferPercent = 100;
            } else if (Patterns.WEB_URL.matcher(mediaName).matches()){
                mediaUrl = mediaName;
                bufferPercent = 0;
            } else {
                throw new Exception("Media inputted is neither a valid file on disk or a remote url!");
            }

            // create or reset the media player
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

            // make the current seek pos = 0
            currentSeekPos = 0;

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

            // and if all is OK, get the duration
            totalDuration = mediaPlayer.getDuration();

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

            // set state to "playing"
            state = PlaybackState.PLAYING;

            // seek to
            mediaPlayer.seekTo(position);

            // start
            mediaPlayer.start();
        }
    }

    /**
     * Private "Pause" method. It makes the media player pause and updates the current state
     */
    private void pause () {
        if (mediaPlayer != null) {

            // set the state to "paused"
            state = PlaybackState.PAUSED;

            // get the current seek pos
            currentSeekPos = mediaPlayer.getCurrentPosition();

            // pause
            mediaPlayer.pause();
        }
    }

    /**
     * Public "Close" method. It deletes everything
     */
    public void close () {

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
    public void shouldAutostart () {
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
}
