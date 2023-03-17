package io.github.simonbas.screwbox.core.graphics;

import io.github.simonbas.screwbox.core.window.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WindowTest {

    @Spy
    Window window;

    @Test
    void setCursor_predefinedCursor_setsFullscreenAndWindowCursor() {
        window.setCursor(MouseCursor.HIDDEN);

        verify(window).setWindowCursor(MouseCursor.HIDDEN);
        verify(window).setFullscreenCursor(MouseCursor.HIDDEN);
    }

    @Test
    void setCursor_multiImageSprite_exception() {
        Sprite multiImageSprite = new Sprite(List.of(Frame.invisible(), Frame.invisible()));

        assertThatThrownBy(() -> window.setCursor(multiImageSprite))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The sprite has more than one frame.");
    }

    @Test
    void setCursor_singleFrameSprite_exception() {
        Sprite sprite = Sprite.invisible();

        window.setCursor(sprite);

        verify(window).setFullscreenCursor(sprite.singleFrame());
        verify(window).setWindowCursor(sprite.singleFrame());
    }

}
