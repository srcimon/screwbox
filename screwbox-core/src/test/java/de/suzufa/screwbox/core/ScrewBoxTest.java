package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScrewBoxTest {

    @BeforeEach
    void ensureHeadlessModeInDevelopment() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void createEngine_nameIsNull_throwsException() {
        assertThatThrownBy(() -> ScrewBox.createEngine(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void createHeadlessEngine_createsEngineNamedHeadless() {
        Engine engine = ScrewBox.createHeadlessEngine();

        assertThat(engine.name()).isEqualTo("Headless");
    }

}
