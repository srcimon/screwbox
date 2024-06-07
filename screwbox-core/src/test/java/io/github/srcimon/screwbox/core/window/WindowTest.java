package io.github.srcimon.screwbox.core.window;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void setCursor_multiImageSprite_exception() {
        Sprite multiImageSprite = new Sprite(List.of(Frame.invisible(), Frame.invisible()));

        assertThatThrownBy(() -> window.setCursor(multiImageSprite))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The sprite has more than one frame.");
    }
}
