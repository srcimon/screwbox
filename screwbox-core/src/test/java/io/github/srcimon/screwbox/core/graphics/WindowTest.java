package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.window.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class WindowTest {

    @Spy
    Window window;

    @Test
    void setCursor_multiImageSprite_exception() {
        Sprite multiImageSprite = new Sprite(List.of(Frame.invisible(), Frame.invisible()));

        assertThatThrownBy(() -> window.setCursor(multiImageSprite))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The sprite has more than one frame.");
    }

}
