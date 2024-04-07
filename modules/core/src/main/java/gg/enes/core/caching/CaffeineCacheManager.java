package gg.enes.core.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheManager implements CacheManager {
    private final Cache<Object, Object> cache;

    public CaffeineCacheManager() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public <K, V> void set(K key, V value) {
        this.cache.put(key, value);
    }

    @Override
    public <K, V> @Nullable V get(K key, Class<V> type) {
        Object value = this.cache.getIfPresent(key);

        if (type.isInstance(value)) {
            return type.cast(value);
        }

        return null;
    }

    @Override
    public <K> void del(K key) {
        this.cache.invalidate(key);
    }
}
