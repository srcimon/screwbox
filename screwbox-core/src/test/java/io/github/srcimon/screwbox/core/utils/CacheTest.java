package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CacheTest {

    Cache<Integer, String> cache;

    @BeforeEach
    void beforeEach() {
        cache = new Cache<>();
    }

    @Test
    void put_valueAlreadyPresent_replacesCurrentCacheValue() {
        cache.put(1, "one");
        cache.put(1, "two");

        assertThat(cache.get(1)).isEqualTo(Optional.of("two"));
    }

    @Test
    void getOrElse_valueNotInCache_returnsNewValueAndStoresInCache() {
        String result = cache.getOrElse(47, () -> "created value");

        assertThat(result).isEqualTo("created value");
        assertThat(cache.get(47)).isEqualTo(Optional.of("created value"));
    }

    @Test
    void getOrElse_valueInCache_returnsValueFromCache() {
        cache.put(47, "cache value");
        String result = cache.getOrElse(47, () -> "created value");

        assertThat(result).isEqualTo("cache value");
    }

    @Test
    void clear_keyPresent_removesEntry() {
        cache.put(19, "value");

        cache.clear(19);

        assertThat(cache.get(19)).isEmpty();
    }

    @Test
    void clear_removesAllEntries() {
        cache.put(1, "one");
        cache.put(2, "two");

        cache.clear();

        assertThat(cache.get(1)).isEmpty();
        assertThat(cache.get(2)).isEmpty();
    }
}
