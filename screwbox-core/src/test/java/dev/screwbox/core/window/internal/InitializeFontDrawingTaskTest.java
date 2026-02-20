package dev.screwbox.core.window.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.assertj.core.api.Assertions.assertThatNoException;

class InitializeFontDrawingTaskTest {

    @Test
    @DisabledOnOs(OS.LINUX)
    void run_doesntCrash() {
        assertThatNoException().isThrownBy(() -> new InitializeFontDrawingTask().run());
    }
}
