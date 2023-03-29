package io.github.simonbas.screwbox.core.utils;

import io.github.simonbas.screwbox.core.Duration;
import org.junit.jupiter.api.Test;

import static io.github.simonbas.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeoutCacheTest {

    @Test
    void get_entryFresh_returnsValue() {
        TimeoutCache<Integer, String> cache = new TimeoutCache<>(ofSeconds(5));
        cache.put(19, "value");

        assertThat(cache.get(19)).hasValue("value");
    }

    @Test
    void get_entryOutdated_isEmpty() {
        TimeoutCache<Integer, String> cache = new TimeoutCache<>(Duration.ofNanos(1));

        cache.put(19, "value");

        assertThat(cache.get(19)).isEmpty();
    }

    @Test
    void newInstance_timeoutIsNull_throwsException() {
        assertThatThrownBy(() -> new TimeoutCache<>(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("lifetime must not be null");
    }
}
