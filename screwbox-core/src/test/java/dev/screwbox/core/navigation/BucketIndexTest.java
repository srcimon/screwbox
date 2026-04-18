package dev.screwbox.core.navigation;

import dev.screwbox.core.environment.Entity;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BucketIndexTest {

    @Test
    void newInstance_negativeCellSize_throwsException() {
        List<Entity> entities = Collections.emptyList();

        assertThatThrownBy(() -> new BucketIndex(-4, entities))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be positive (actual value: -4.0)");
    }

}
