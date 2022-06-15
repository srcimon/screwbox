package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Percentage;

@ExtendWith(MockitoExtension.class)
class WindowTest {

    @Spy
    Window window;

    @Test
    void fillWith_sprite_callsRealMethod() {
        Sprite sprite = sprite();

        window.fillWith(sprite);

        verify(window).fillWith(origin(), sprite, 1, Percentage.max());
    }

    @Test
    void fillWith_OffsetAndsprite_callsRealMethod() {
        Sprite sprite = sprite();

        window.fillWith(Offset.at(2, 3), sprite);

        verify(window).fillWith(Offset.at(2, 3), sprite, 1, Percentage.max());
    }

    private Sprite sprite() {
        return Sprite.fromFile("tile.bmp");
    }
}
