package gg.enes.moderation.core.cache.config;

public final class CacheConfig {
    /**
     * The maximum size of the cache.
     */
    private int maximumSize = 512;

    /**
     * The time to live of the cache.
     */
    private long timeToLive = 60;

    /**
     * The singleton instance of the CacheConfig.
     */
    private static CacheConfig instance;

    private CacheConfig() {
    }

    /**
     * Retrieves the singleton instance of the CacheConfig.
     *
     * @return The singleton instance of the CacheConfig.
     */
    public static CacheConfig build() {
        if (instance == null) {
            instance = new CacheConfig();
        }

        return instance;
    }

    /**
     * Sets the maximum size of the cache.
     *
     * @param newMaximumSize The maximum size of the cache.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setMaximumSize(final int newMaximumSize) {
        this.maximumSize = newMaximumSize;
        return this;
    }

    /**
     * Retrieves the maximum size of the cache.
     *
     * @return The maximum size of the cache.
     */
    public int getMaximumSize() {
        return this.maximumSize;
    }

    /**
     * Sets the time to live of the cache.
     *
     * @param newTimeToLive The time to live of the cache.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setTimeToLive(final long newTimeToLive) {
        this.timeToLive = newTimeToLive;
        return this;
    }

    /**
     * Retrieves the time to live of the cache.
     *
     * @return The time to live of the cache.
     */
    public long getTimeToLive() {
        return this.timeToLive;
    }
}
