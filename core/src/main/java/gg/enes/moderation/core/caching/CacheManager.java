package gg.enes.moderation.core.caching;

public interface CacheManager {
    <K, V> void set(K key, V value);

    <K, V> V get(K key, Class<V> type);

    <K> void del(K key);
}