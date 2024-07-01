package gg.enes.moderation.core.cache;

public interface CacheManager<K, V> {
    /**
     * Checks if the cache contains a key.
     *
     * @param key key to check.
     * @return True if the cache contains the key, false otherwise.
     */
    boolean contains(K key);

    /**
     * Sets a key in the cache.
     *
     * @param key   The key to set.
     * @param value The value to set.
     */
    void set(K key, V value);

    /**
     * Retrieves a key from the cache.
     *
     * @param key The key to retrieve.
     * @return The value associated with the key or null if not found.
     */
    V get(K key);

    /**
     * Removes a key from the cache.
     *
     * @param key The key to remove.
     */
    void del(K key);

    /**
     * Clears the cache.
     */
    void clear();
}