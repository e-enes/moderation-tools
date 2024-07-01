package gg.enes.moderation.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import gg.enes.moderation.core.cache.config.CacheConfig;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

public final class CaffeineCacheManager<K, V> implements CacheManager<K, V> {
    /**
     * The cache instance.
     */
    private final Cache<K, V> cache;

    /**
     * Constructs a new Caffeine cache manager with the provided configuration.
     *
     * @param config The cache configuration.
     */
    public CaffeineCacheManager(@NonNull final CacheConfig config) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(config.getMaximumSize())
                .expireAfterWrite(config.getTimeToLive(), TimeUnit.MINUTES)
                .build();
    }

    @Override
    public boolean contains(final K key) {
        return this.cache.getIfPresent(key) != null;
    }

    @Override
    public void set(final K key, final V value) {
        this.cache.put(key, value);
    }

    @Override
    public @Nullable V get(final K key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void del(final K key) {
        this.cache.invalidate(key);
    }

    @Override
    public void clear() {
        this.cache.invalidateAll();
    }
}