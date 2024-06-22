package gg.enes.moderation.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.cache.config.CacheType;
import org.checkerframework.checker.nullness.qual.NonNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class CacheManager {
    /**
     * The singleton instance of the CacheManager.
     */
    private static CacheManager instance;

    /**
     * The Caffeine cache instance.
     */
    private Cache<String, Object> caffeineCache;

    /**
     * The Jedis pool for Redis connections.
     */
    private JedisPool jedisPool;

    /**
     * The type of cache to use.
     */
    private CacheType cacheType;

    private CacheManager() {
    }

    /**
     * Retrieves the singleton instance of the CacheManager.
     *
     * @return The singleton instance of the CacheManager.
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * Initializes the cache manager with the provided configuration.
     *
     * @param config The cache configuration.
     */
    public synchronized void initialize(@NonNull final CacheConfig config) {
        cacheType = config.getCacheType();

        if (cacheType == CacheType.REDIS) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            poolConfig.setMinEvictableIdleDuration(Duration.ofMinutes(1));
            poolConfig.setBlockWhenExhausted(true);

            jedisPool = new JedisPool(poolConfig, config.getRedisHost(), config.getRedisPort(), config.getRedisTimeout());
        } else if (cacheType == CacheType.CAFFEINE) {
            caffeineCache = Caffeine.newBuilder()
                    .maximumSize(config.getCaffeineMaxSize())
                    .expireAfterWrite(config.getCaffeineExpireAfterWrite(), TimeUnit.MILLISECONDS)
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported cache type: " + config.getCacheType());
        }
    }

    /**
     * Retrieves the type of cache currently in use.
     *
     * @return The cache type as a CacheType enum.
     */
    public CacheType getCacheType() {
        return cacheType;
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the key or null if not found.
     */
    public Object get(final String key) {
        if (cacheType == CacheType.REDIS) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            }
        } else if (cacheType == CacheType.CAFFEINE) {
            return caffeineCache.getIfPresent(key);
        } else {
            throw new IllegalStateException("CacheManager is not properly initialized.");
        }
    }

    /**
     * Inserts or updates an entry in the cache.
     *
     * @param key The key under which the value is stored.
     * @param value The value to be associated with the key.
     */
    public void put(final String key, final Object value) {
        if (cacheType == CacheType.REDIS) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set(key, value.toString());
            }
        } else if (cacheType == CacheType.CAFFEINE) {
            caffeineCache.put(key, value);
        } else {
            throw new IllegalStateException("CacheManager is not initialized.");
        }
    }

    /**
     * Removes an entry from the cache, if it exists.
     *
     * @param key The key whose entry is to be removed.
     */
    public void del(final String key) {
        if (cacheType == CacheType.REDIS) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.del(key);
            }
        } else if (cacheType == CacheType.CAFFEINE) {
            caffeineCache.invalidate(key);
        } else {
            throw new IllegalStateException("CacheManager is not initialized.");
        }
    }

    /**
     * Removes an entry from the cache, if it exists.
     */
    public void close() {
        if (cacheType == CacheType.REDIS) {
            jedisPool.close();
        } else if (cacheType == CacheType.CAFFEINE) {
            caffeineCache.invalidateAll();
        } else {
            throw new IllegalStateException("CacheManager is not initialized.");
        }
    }
}
