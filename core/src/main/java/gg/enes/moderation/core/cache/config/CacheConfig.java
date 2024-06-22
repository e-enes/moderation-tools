package gg.enes.moderation.core.cache.config;

public class CacheConfig {
    /**
     * The type of cache to use.
     */
    private CacheType cacheType;

    /**
     * The host of the Redis cache.
     */
    private String redisHost;

    /**
     * The port of the Redis cache.
     */
    private int redisPort;

    /**
     * The timeout for Redis connections.
     */
    private int redisTimeout;

    /**
     * The maximum size of the Caffeine cache.
     */
    private long caffeineMaxSize;

    /**
     * The expiration time for entries in the Caffeine cache.
     */
    private long caffeineExpireAfterWrite;

    /**
     * Sets the type of cache to use.
     *
     * @param newCacheType The cache type as a CacheType enum.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setCacheType(final CacheType newCacheType) {
        this.cacheType = newCacheType;
        return this;
    }

    /**
     * Retrieves the type of cache to use.
     *
     * @return The cache type as a CacheType enum.
     */
    public CacheType getCacheType() {
        return this.cacheType;
    }

    /**
     * Sets the host of the Redis cache.
     *
     * @param newRedisHost The host of the Redis cache.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setRedisHost(final String newRedisHost) {
        this.redisHost = newRedisHost;
        return this;
    }

    /**
     * Retrieves the host of the Redis cache.
     *
     * @return The host of the Redis cache.
     */
    public String getRedisHost() {
        return this.redisHost;
    }

    /**
     * Sets the port of the Redis cache.
     *
     * @param newRedisPort The port of the Redis cache.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setRedisPort(final int newRedisPort) {
        this.redisPort = newRedisPort;
        return this;
    }

    /**
     * Retrieves the port of the Redis cache.
     *
     * @return The port of the Redis cache.
     */
    public int getRedisPort() {
        return this.redisPort;
    }

    /**
     * Sets the timeout for Redis connections.
     *
     * @param newRedisTimeout The timeout for Redis connections.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setRedisTimeout(final int newRedisTimeout) {
        this.redisTimeout = newRedisTimeout;
        return this;
    }

    /**
     * Retrieves the timeout for Redis connections.
     *
     * @return The timeout for Redis connections.
     */
    public int getRedisTimeout() {
        return this.redisTimeout;
    }

    /**
     * Sets the maximum size of the Caffeine cache.
     *
     * @param newCaffeineMaxSize The maximum size of the Caffeine cache.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setCaffeineMaxSize(final long newCaffeineMaxSize) {
        this.caffeineMaxSize = newCaffeineMaxSize;
        return this;
    }

    /**
     * Retrieves the maximum size of the Caffeine cache.
     *
     * @return The maximum size of the Caffeine cache.
     */
    public long getCaffeineMaxSize() {
        return this.caffeineMaxSize;
    }

    /**
     * Sets the expiration time for entries in the Caffeine cache.
     *
     * @param newCaffeineExpireAfterWrite The expiration time for entries in the Caffeine cache.
     * @return The current CacheConfig instance.
     */
    public CacheConfig setCaffeineExpireAfterWrite(final long newCaffeineExpireAfterWrite) {
        this.caffeineExpireAfterWrite = newCaffeineExpireAfterWrite;
        return this;
    }

    /**
     * Retrieves the expiration time for entries in the Caffeine cache.
     *
     * @return The expiration time for entries in the Caffeine cache.
     */
    public long getCaffeineExpireAfterWrite() {
        return this.caffeineExpireAfterWrite;
    }
}
