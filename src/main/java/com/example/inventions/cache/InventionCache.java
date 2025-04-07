package com.example.inventions.cache;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

@Component
public class InventionCache {
    private static final Logger logger = Logger.getLogger(InventionCache.class.getName());

    private static final int MAX_CACHE_SIZE = 100;
    private final Map<String, Object> cache;

    public InventionCache() {
        this.cache = Collections.synchronizedMap(
                new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                        if (size() > MAX_CACHE_SIZE) {
                            logger.info(() -> String.format(
                                    "Cache is full (%d/%d), removing eldest entry: %s",
                                    size(), MAX_CACHE_SIZE, eldest.getKey()
                            ));
                            return true;
                        }
                        return false;
                    }
                }
        );
    }

    /**
     * Puts any value in cache
     * @param key Cache key
     * @param value Value to cache (must not be null)
     * @throws IllegalArgumentException if key or value is null
     */
    public void put(String key, Object value) {
        Objects.requireNonNull(key, "Cache key must not be null");
        Objects.requireNonNull(value, "Cache value must not be null");

        synchronized (cache) {
            logger.info(() -> String.format(
                    "Caching data for key: %s (cache usage: %d/%d)",
                    key, cache.size(), MAX_CACHE_SIZE
            ));
            cache.put(key, value);
        }
    }

    /**
     * Gets typed list from cache
     * @param key Cache key
     * @param elementType Expected list element type
     * @return List of elements or null if not found or type mismatch
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> elementType) {
        Objects.requireNonNull(key, "Cache key must not be null");
        Objects.requireNonNull(elementType, "Element type must not be null");

        Object value;
        synchronized (cache) {
            value = cache.get(key);
        }

        if (value == null) {
            logger.info(() -> String.format(
                    "Cache miss for list with key: %s (cache usage: %d/%d)",
                    key, cache.size(), MAX_CACHE_SIZE
            ));
            return null;
        }

        if (!(value instanceof List<?> list)) {
            logger.warning(() -> String.format(
                    "Cached value is not a List for key %s. Actual type: %s (cache usage: %d/%d)",
                    key, value.getClass().getName(), cache.size(), MAX_CACHE_SIZE
            ));
            return null;
        }

        if (!list.isEmpty() && !elementType.isInstance(list.get(0))) {
            logger.warning(() -> String.format(
                    "List element type mismatch for key %s. Expected %s but was %s (cache usage: %d/%d)",
                    key, elementType.getName(), list.get(0).getClass().getName(),
                    cache.size(), MAX_CACHE_SIZE
            ));
            return null;
        }

        logger.info(() -> String.format(
                "Cache hit for list with key: %s (cache usage: %d/%d)",
                key, cache.size(), MAX_CACHE_SIZE
        ));
        return (List<T>) value;
    }

    /**
     * @return Current number of entries in cache
     */
    public int size() {
        synchronized (cache) {
            return cache.size();
        }
    }

    public void evictByKeyPrefix(String prefix) {
        synchronized (cache) {
            cache.keySet().removeIf(key -> key.startsWith(prefix));
            logger.info(() -> String.format("Cache entries with prefix '%s' removed", prefix));
        }
    }

}