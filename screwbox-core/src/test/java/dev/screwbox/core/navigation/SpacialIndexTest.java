package dev.screwbox.core.navigation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpacialIndexTest {

    @Test
    void registry_noEntities_isNotPresent() {
        SpacialIndex spacialIndex = new SpacialIndex();

        assertThat(spacialIndex.registry()).isEmpty();
    }
}
