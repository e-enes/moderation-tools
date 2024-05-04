package gg.enes.moderation.core.caching;

public interface CacheManager<K, V> {
    /**
     * Inserts or updates an entry in the cache.
     *
     * @param key The key under which the value is stored.
     * @param value The value to be associated with the key.
     */
    void set(K key, V value);

    /**
     * Retrieves an entry from the cache, if present and of the specified type.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the key or null if not found.
     */
    V get(K key);

    /**
     * Removes an entry from the cache, if it exists.
     *
     * @param key The key whose entry is to be removed.
     */
    void del(K key);
}