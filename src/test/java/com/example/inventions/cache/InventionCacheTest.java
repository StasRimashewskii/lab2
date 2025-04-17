package com.example.inventions.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InventionCacheTest {

    private InventionCache cache;

    @BeforeEach
    void setUp() {
        cache = new InventionCache();
    }

    @Test
    void testPutAndGetListWithCorrectType() {
        List<String> data = Arrays.asList("инновация1", "инновация2");
        cache.put("ключ1", data);

        List<String> cachedData = cache.getList("ключ1", String.class);
        assertNotNull(cachedData, "Список не должен быть null");
        assertEquals(data, cachedData, "Список должен совпадать с сохранённым");
    }

    @Test
    void testGetListReturnsNullForWrongType() {
        cache.put("ключ2", Arrays.asList(1, 2, 3));

        List<String> result = cache.getList("ключ2", String.class);
        assertNull(result, "Ожидается null при несовпадении типов");
    }

    @Test
    void testGetListReturnsNullForNonListObject() {
        cache.put("ключ3", "просто строка");

        List<String> result = cache.getList("ключ3", String.class);
        assertNull(result, "Ожидается null при попытке получить не-список как список");
    }

    @Test
    void testEvictWhenCacheExceedsMaxSize() {
        for (int i = 0; i < 105; i++) {
            cache.put("ключ" + i, "значение" + i);
        }

        assertTrue(cache.size() <= 100, "Размер кэша не должен превышать 100");
    }

    @Test
    void testEvictByKeyPrefix() {
        cache.put("prefix_один", List.of("один"));
        cache.put("prefix_два", List.of("два"));
        cache.put("другой_ключ", List.of("три")); // здесь теперь список

        cache.evictByKeyPrefix("prefix_");

        assertNull(cache.getList("prefix_один", String.class));
        assertNull(cache.getList("prefix_два", String.class));
        assertNotNull(cache.getList("другой_ключ", String.class), "Ключ без префикса не должен быть удалён");
    }

    @Test
    void testPutWithNullKeyThrowsException() {
        assertThrows(NullPointerException.class, () -> cache.put(null, "значение"),
                "Ожидается исключение при передаче null в ключ");
    }

    @Test
    void testPutWithNullValueThrowsException() {
        assertThrows(NullPointerException.class, () -> cache.put("ключ", null),
                "Ожидается исключение при передаче null в значение");
    }

    @Test
    void testGetListWithNullKeyThrowsException() {
        assertThrows(NullPointerException.class, () -> cache.getList(null, String.class),
                "Ожидается исключение при передаче null в ключ при получении");
    }

    @Test
    void testGetListWithNullTypeThrowsException() {
        assertThrows(NullPointerException.class, () -> cache.getList("ключ", null),
                "Ожидается исключение при передаче null в тип");
    }
}
