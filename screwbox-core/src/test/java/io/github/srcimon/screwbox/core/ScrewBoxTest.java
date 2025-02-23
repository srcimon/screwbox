package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScrewBoxTest {

    @Test
    void createEngine_nameIsNull_throwsException() {
        assertThatThrownBy(() -> ScrewBox.createEngine(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }
}
