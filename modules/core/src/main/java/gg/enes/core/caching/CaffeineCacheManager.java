package gg.enes.core.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheManager implements CacheManager {
    private final Cache<Object, Object> cache;

    /**
     * Constructs a CaffeineCacheManager with predefined maximum size and expiry policy.
     */
    public CaffeineCacheManager() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Inserts or updates an entry in the cache.
     *
     * @param key The key under which the value is stored.
     * @param value The value to be associated with the key.
     */
    @Override
    public <K, V> void set(K key, V value) {
        this.cache.put(key, value);
    }

    /**
     * Retrieves an entry from the cache, if present and of the specified type.
     *
     * @param key The key whose associated value is to be returned.
     * @param type The type of the value to return.
     * @return The value associated with the key or null if not found or type mismatch.
     */
    @Override
    public <K, V> @Nullable V get(K key, Class<V> type) {
        Object value = this.cache.getIfPresent(key);

        if (type.isInstance(value)) {
            return type.cast(value);
        }

        return null;
    }

    /**
     * Removes an entry from the cache, if it exists.
     *
     * @param key The key whose entry is to be removed.
     */
    @Override
    public <K> void del(K key) {
        this.cache.invalidate(key);
    }
}
