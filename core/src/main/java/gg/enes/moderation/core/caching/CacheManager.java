package gg.enes.moderation.core.caching;

public interface CacheManager<K, V> {
    void set(K key, V value);

    V get(K key);

    void del(K key);
}