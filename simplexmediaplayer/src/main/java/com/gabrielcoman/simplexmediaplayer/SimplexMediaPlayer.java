/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer;

import android.media.MediaPlayer;

/**
 * Custom media player
 */
public class SimplexMediaPlayer extends MediaPlayer implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener
{

    // private state vars
    private int      mLastPosition  = 0;
    private int      mTotalDuration = 0;
    private float    mBufferPercent = 0;

    // private Listener instance
    private Listener listener;

    /**
     * Simplex constructor that sets all traditional MediaPlayer listeners and
     * creates a new local instance of the SimplexMediaPlayer.Listener interface
     */
    SimplexMediaPlayer() {

        setOnPreparedListener(this);
        setOnCompletionListener(this);
        setOnErrorListener(this);
        setOnBufferingUpdateListener(this);

        listener = new Listener() {
            @Override public void onPrepared() {}
            @Override public void onCompletion() {}
            @Override public void onError() {}};
    }

    /**
     * Method that plays the video from a certain given position
     *
     * @param position a custom position
     */
    void playFrom(int position) {
        setLastPosition(position);
        playFromLastPosition();
    }

    /**
     * Method that plays the video from the last known position
     */
    private void playFromLastPosition() {
        seekToLastPosition();
        start();
    }

    /**
     * Method that seeks the video to the last known position
     */
    void seekToLastPosition() {
        seekTo(mLastPosition);
    }

    /**
     * Method that seeks the video to the end
     */
    void seekToEnd() {
        seekTo(mLastPosition);
    }

    /**
     * Saves the current position
     */
    private void saveCurrentPos() {
        setLastPosition(getCurrentPosition());
    }

    /**
     * Setter  the last know position
     *
     * @param position a new position
     */
    private void setLastPosition(int position) {
        mLastPosition = position;
    }

    /**
     * Getter for the last position
     *
     * @return the last known position
     */
    int getLastPosition () {
        return mLastPosition;
    }

    /**
     * Getter for the total duration
     *
     * @return total duration
     */
    int getTotalDuration() {
        return mTotalDuration;
    }

    /**
     * Getter for the buffer percent
     *
     * @return the percent the buffer is loaded at
     */
    float getBufferPercent() {
        return mBufferPercent;
    }

    /**
     * Sets the buffer at 100% (useful for offline files)
     */
    void setOfflineBuffer() {
        mBufferPercent = 100;
    }

    /**
     * Sets the buffer at 0% (for online media just starting to download)
     */
    void setOnlineBuffer() {
        mBufferPercent = 0;
    }

    /**
     * Overridden pause method that also saves the current position
     *
     * @throws IllegalStateException
     */
    @Override
    public void pause() throws IllegalStateException {
        saveCurrentPos();
        super.pause();
    }

    /**
     * Overridden stom method that also saves the current position
     *
     * @throws IllegalStateException
     */
    @Override
    public void stop() throws IllegalStateException {
        saveCurrentPos();
        super.stop();
    }

    /**
     * Overridden reset-all method
     */
    @Override
    public void reset() {
        mLastPosition = 0;
        mTotalDuration = 0;
        mBufferPercent = 0;
        super.reset();
    }

    /**
     * Setter for the local listener instance
     *
     * @param listener a new instance of the SimplexVideoView.Listener class
     */
    void setListener(Listener listener) {
        this.listener = listener != null ? listener : this.listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Implement methods from MediaPlayer interface
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when the media player online buffer is updated
     *
     * @param mp      current media player
     * @param percent percent it has gotten to
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mBufferPercent = percent / 100.0F;
    }

    /**
     * Called when the media player has completed
     *
     * @param mp current media player
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        listener.onCompletion();
    }

    /**
     * Called when the media player errors
     *
     * @param mp    current media player
     * @param what  what error
     * @param extra extra info
     * @return      whether to propagate
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        listener.onError();
        return false;
    }

    /**
     * Called when the media player is prepared
     *
     * @param mp current media player
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mTotalDuration = mp.getDuration();
        listener.onPrepared();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Interface to implement by the SimplexPlayer
     */
    interface Listener {

        /**
         * Called when the media player is ready
         */
        void onPrepared ();

        /**
         * Called when the media player finished playing
         */
        void onCompletion ();

        /**
         * Called on an error
         */
        void onError ();

    }
}
