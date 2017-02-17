package com.gabrielcoman.simplexmediaplayer.aux.time;

import java.util.Locale;

public class SimplexTime {

    public static String getTimeString(long millis) {
        StringBuilder buf = new StringBuilder();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

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
