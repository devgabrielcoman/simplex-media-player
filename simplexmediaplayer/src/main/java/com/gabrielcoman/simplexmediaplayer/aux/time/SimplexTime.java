/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.aux.time;

/**
 * Class that represents a time
 */
public class SimplexTime {

    private int hour;
    private int minute;
    private int second;

    /**
     * Constructor with three member vars
     *
     * @param hour
     * @param minute
     * @param second
     */
    public SimplexTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Getter for hour
     *
     * @return current stored hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Getter for minute
     *
     * @return current stored minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Getter for second
     *
     * @return current stored second
     */
    public int getSecond() {
        return second;
    }
}
