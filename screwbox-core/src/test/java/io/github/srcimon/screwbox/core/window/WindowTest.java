package io.github.srcimon.screwbox.core.window;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class WindowTest {

    @Spy
    Window window;

    @Test
    void isClosed_isNotOpen_isTrue() {
        assertThat(window.isClosed()).isTrue();
    }

    @Test
    void isClosed_isOpen_isFalse() {
        when(window.isOpen()).thenReturn(true);

        assertThat(window.isClosed()).isFalse();
    }
}
