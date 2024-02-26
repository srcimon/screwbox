package io.github.srcimon.screwbox.core.window.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

class InitializeFontDrawingTaskTest {

    @Test
    void run_doesntCrash() {
        assertThatNoException().isThrownBy(() -> new InitializeFontDrawingTask().run());
    }
}
