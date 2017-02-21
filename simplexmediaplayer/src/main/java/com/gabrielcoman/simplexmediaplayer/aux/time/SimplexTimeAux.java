/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.aux.time;

import java.util.Locale;

/**
 * Aux class that contains a method that will transform a milisecond time into its
 * "HH:MM:mm" equivalent.
 *
 */
public class SimplexTimeAux {

    /**
     * Method that takes a long parameter representing a time interval and translates it into
     * a new SimplexTime instance
     *
     * @param millis time interval
     * @return       a new populated SimplexTime instance
     */
    public static SimplexTime getTime (long millis) {

        // create a new string builder
        StringBuilder buf = new StringBuilder();

        // find out how many hours the millis parameter represents
        int hours = (int) (millis / (1000 * 60 * 60));
        // and how many minutes
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        // and how many seconds
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        return new SimplexTime(hours, minutes, seconds);
    }

    /**
     * Method that takes a long parameter representing a time interval and translates it into
     * a human readable string.
     *
     * @param millis time interval
     * @return       string with format "HH:MM:mm"
     */
    public static String getTimeString(long millis) {

        // create a new string builder
        StringBuilder buf = new StringBuilder();

        // find out how many hours the millis parameter represents
        int hours = (int) (millis / (1000 * 60 * 60));
        // and how many minutes
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        // and how many seconds
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        // format and return
        if (hours > 0) {
            return buf
                    .append(String.format(Locale.getDefault(), "%02d", hours))
                    .append(":")
                    .append(String.format(Locale.getDefault(), "%02d", minutes))
                    .append(":")
                    .append(String.format(Locale.getDefault(), "%02d", seconds)).toString();
        } else {
            return buf.append(String.format(Locale.getDefault(), "%02d", minutes))
                    .append(":")
                    .append(String.format(Locale.getDefault(), "%02d", seconds)).toString();
        }
    }
}
