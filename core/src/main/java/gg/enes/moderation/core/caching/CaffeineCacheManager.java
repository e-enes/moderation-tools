package gg.enes.moderation.core.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheManager<K, V> implements CacheManager<K, V> {
    /**
     * The maximum number of entries that can be stored in the cache.
     */
    private static final int MAXIMUM_SIZE = 10_000;

    /**
     * The duration in minutes that an entry can be stored in the cache.
     */
    private static final int DURATION = 60;

    /**
     * The Caffeine cache instance.
     */
    private final Cache<K, V> cache;

    /**
     * Constructs a new Caffeine cache manager with default settings.
     */
    public CaffeineCacheManager() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .expireAfterWrite(DURATION, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Inserts or updates an entry in the cache.
     *
     * @param key The key under which the value is stored.
     * @param value The value to be associated with the key.
     */
    @Override
    public void set(final K key, final V value) {
        this.cache.put(key, value);
    }

    /**
     * Retrieves an entry from the cache, if present and of the specified type.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the key or null if not found.
     */
    @Override
    public V get(final K key) {
        return this.cache.getIfPresent(key);
    }

    /**
     * Removes an entry from the cache, if it exists.
     *
     * @param key The key whose entry is to be removed.
     */
    @Override
    public void del(final K key) {
        this.cache.invalidate(key);
    }
}
