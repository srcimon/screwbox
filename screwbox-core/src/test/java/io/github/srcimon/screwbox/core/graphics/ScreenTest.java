package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScreenTest {

    @Spy
    Screen screen;

    @Test
    void drawRectangle_usingBounds_drawsRectangleAtPositionWithSize() {
        screen.drawRectangle(new ScreenBounds(10, 4, 13, 20), RectangleDrawOptions.filled(Color.RED));

        verify(screen).drawRectangle(Offset.at(10, 4), Size.of(13, 20), RectangleDrawOptions.filled(Color.RED));
    }
}
