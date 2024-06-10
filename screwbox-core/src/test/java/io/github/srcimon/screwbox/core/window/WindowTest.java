package io.github.srcimon.screwbox.core.window;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
