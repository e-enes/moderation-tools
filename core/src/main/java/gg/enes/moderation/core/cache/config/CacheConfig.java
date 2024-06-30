package gg.enes.moderation.core.cache.config;

public final class CacheConfig {
    /**
     * The maximum size of the cache.
     */
    private static int maximumSize = 512;

    /**
     * The time to live of the cache.
     */
    private static long timeToLive = 60;

    /**
     * The singleton instance of CacheConfig.
     */
    private static volatile CacheConfig instance;

    private CacheConfig() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the singleton instance of CacheConfig.
     *
     * @return The singleton instance of CacheConfig.
     */
    public static CacheConfig getInstance() {
        if (instance == null) {
            synchronized (CacheConfig.class) {
                if (instance == null) {
                    instance = new CacheConfig();
                }
            }
        }

        return instance;
    }

    /**
     * Retrieves the maximum size of the cache.
     *
     * @return The maximum size of the cache.
     */
    public int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Retrieves the time to live of the cache.
     *
     * @return The time to live of the cache.
     */
    public long getTimeToLive() {
        return timeToLive;
    }

    /**
     * Sets the maximum size of the cache globally.
     *
     * @param newMaximumSize The new maximum size of the cache.
     */
    public static void setMaximumSize(final int newMaximumSize) {
        maximumSize = newMaximumSize;
    }

    /**
     * Sets the time to live of the cache globally.
     *
     * @param newTimeToLive The new time to live of the cache.
     */
    public static void setTimeToLive(final long newTimeToLive) {
        timeToLive = newTimeToLive;
    }

}
