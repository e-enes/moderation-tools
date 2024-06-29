package gg.enes.moderation.core;

import gg.enes.moderation.core.utils.EnvironmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ModerationLogger {
    private ModerationLogger() {
    }

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("ModerationCore");

    /**
     * Logs a debug message.
     *
     * @param message The message to log.
     */
    public static void debug(final String message) {
        if (EnvironmentUtil.isDevelopmentEnvironment()) {
            LOGGER.debug(message);
        }
    }

    /**
     * Logs an info message.
     *
     * @param message The message to log.
     */
    public static void info(final String message) {
        LOGGER.info(message);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log.
     */
    public static void warn(final String message) {
        LOGGER.warn(message);
    }

    /**
     * Logs an error message.
     *
     * @param message The message to log.
     */
    public static void error(final String message) {
        LOGGER.error(message);
    }

    /**
     * Logs an error message with a throwable.
     *
     * @param message   The message to log.
     * @param throwable The throwable to log.
     */
    public static void error(final String message, final Throwable throwable) {
        LOGGER.error(message, throwable);
    }
}
