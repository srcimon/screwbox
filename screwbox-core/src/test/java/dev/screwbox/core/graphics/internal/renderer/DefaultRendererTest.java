package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class DefaultRendererTest {

    private static final ScreenBounds CLIP = new ScreenBounds(Offset.origin(), Size.of(640, 480));

    @Mock
    Graphics2D graphics;

    @InjectMocks
    DefaultRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer.updateContext(() -> graphics);
    }

    @Test
    void fillWith_newColor_changesColorAndFillsRect() {
        renderer.fillWith(Color.RED, CLIP);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics, times(1)).fillRect(0, 0, 640, 480);
    }

    @Test
    void fillWith_sameColor_changesColorOnlyOnce() {
        renderer.fillWith(Color.RED, CLIP);
        renderer.fillWith(Color.RED, CLIP);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics, times(2)).fillRect(0, 0, 640, 480);
    }

    @Test
    void drawRectangle_curveRadiusBiggerThanSize_noException() {
        var options = RectangleDrawOptions.outline(Color.RED).curveRadius(20);
        var offset = Offset.at(10, 20);
        var size = Size.of(1, 1);

        assertThatNoException().isThrownBy(() -> renderer.drawRectangle(offset, size, options, CLIP));
    }
}
