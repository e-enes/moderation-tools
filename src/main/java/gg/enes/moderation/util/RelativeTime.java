package gg.enes.moderation.util;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for calculating relative time from a given timestamp.
 */
public abstract class RelativeTime {
    /**
     * Calculates the relative time in the past from the given timestamp.
     *
     * @param timestamp The timestamp to calculate relative time from
     * @return The relative time in the past as a string
     */
    public static String getPast(Timestamp timestamp) {
        long diffInMillis = System.currentTimeMillis() - timestamp.getTime();
        long diffInSeconds = TimeUnit.MINUTES.toSeconds(diffInMillis);

        if (diffInSeconds < 60) {
            return diffInSeconds + " second(s) ago";
        }

        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        if (diffInMinutes < 60) {
            return diffInMinutes + " minute(s) ago";
        }

        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        if (diffInHours < 24) {
            return diffInHours + " hour(s) ago";
        }

        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        if (diffInDays < 7) {
            return diffInDays + " day(s) ago";
        }

        long diffInWeeks = diffInDays / 7;
        if (diffInWeeks < 4) {
            return diffInWeeks + " week(s) ago";
        }

        long diffInMonths = diffInDays / 30;
        if (diffInMonths < 12) {
            return diffInMonths + " month(s) ago";
        }

        long diffInYears = diffInDays / 365;
        return diffInYears + " year(s) ago";
    }

    /**
     * Calculates the relative time in the future from the given timestamp.
     *
     * @param timestamp The timestamp to calculate relative time from
     * @return The relative time in the future as a string
     */
    public static String getFuture(Timestamp timestamp) {
        long diffInMillis = timestamp.getTime() - System.currentTimeMillis();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);

        if (diffInSeconds < 60) {
            return "in " + diffInSeconds + " second(s)";
        }

        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        if (diffInMinutes < 60) {
            return "in " + diffInMinutes + " minute(s)";
        }

        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        if (diffInHours < 24) {
            return "in " + diffInHours + " hour(s)";
        }

        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        if (diffInDays < 7) {
            return "in " + diffInDays + " day(s)";
        }

        long diffInWeeks = diffInDays / 7;
        if (diffInWeeks < 4) {
            return "in " + diffInWeeks + " week(s)";
        }

        long diffInMonths = diffInDays / 30;
        if (diffInMonths < 12) {
            return "in " + diffInMonths + " month(s)";
        }

        long diffInYears = diffInDays / 365;
        return "in " + diffInYears + " year(s)";
    }
}
