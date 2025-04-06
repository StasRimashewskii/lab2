package com.example.inventions.cache;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class InventionCache {
    private static final Logger logger = Logger.getLogger(InventionCache.class.getName());

    private static final int MAX_CACHE_SIZE = 100;
    private final Map<String, Object> cache;

    public InventionCache() {
        this.cache = new LinkedHashMap<String, Object>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                if (size() > MAX_CACHE_SIZE) {
                    logger.info("Cache is full, removing eldest entry: " + eldest.getKey());
                    return true;
                }
                return false;
            }
        };
    }

    public synchronized void put(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Cache key and value must not be null");
        }
        logger.info("Caching data for key: " + key);
        cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T get(String key, Class<T> type) {
        Object value = cache.get(key);
        if (value != null) {
            logger.info("Cache hit for key: " + key);
            try {
                return (T) value;
            } catch (ClassCastException e) {
                logger.warning("Type mismatch for key " + key +
                        ". Expected " + type + " but was " + value.getClass());
                return null;
            }
        }
        logger.info("Cache miss for key: " + key);
        return null;
    }

    public synchronized void clear() {
        cache.clear();
        logger.info("Cache cleared");
    }

    public synchronized int size() {
        return cache.size();
    }
}