package dev.screwbox.core.utils;

import dev.screwbox.core.environment.Entity;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpacialIndexTest {

    @Test
    void newInstance_negativeCellSize_throwsException() {
        List<Entity> entities = Collections.emptyList();

        assertThatThrownBy(() -> new SpacialIndex(-4, entities))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be positive (actual value: -4.0)");
    }
}
