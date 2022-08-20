package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
