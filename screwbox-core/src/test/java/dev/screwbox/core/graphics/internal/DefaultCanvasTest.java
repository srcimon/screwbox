package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.verify;

@MockitoSettings
class DefaultCanvasTest {

    private static final ScreenBounds CLIP = new ScreenBounds(0, 0, 100, 100);

    DefaultCanvas canvas;

    @Mock
    Renderer renderer;

    @BeforeEach
    void setUp() {
        canvas = new DefaultCanvas(renderer, CLIP);
    }

    @Test
    void fillWith_callsRendererFillWith() {
        canvas.fillWith(Color.BLUE);

        verify(renderer).fillWith(Color.BLUE, CLIP);
    }

    @Test
    void drawRectangle_callsRenderer() {
        canvas.drawRectangle(Offset.at(4, 10), Size.of(20, 20), RectangleDrawOptions.outline(Color.RED));

        verify(renderer).drawRectangle(Offset.at(4, 10), Size.of(20, 20), RectangleDrawOptions.outline(Color.RED), CLIP);
    }

    @Test
    void drawLine_callsRenderer() {
        canvas.drawLine(Offset.at(10, 3), Offset.at(21, 9), LineDrawOptions.color(Color.BLUE));

        verify(renderer).drawLine(Offset.at(10, 3), Offset.at(21, 9), LineDrawOptions.color(Color.BLUE), CLIP);
    }

    @Test
    void drawCircle_radiusPositive_callsRender() {
        canvas.drawCircle(Offset.at(10, 20), 4, OvalDrawOptions.fading(Color.RED));

        verify(renderer).drawOval(Offset.at(10, 20), 4, 4, OvalDrawOptions.fading(Color.RED), CLIP);
    }

    @Test
    void drawRectangle_usingBounds_callsRenderer() {
        canvas.drawRectangle(new ScreenBounds(10, 4, 13, 20), RectangleDrawOptions.filled(Color.RED));

        verify(renderer).drawRectangle(Offset.at(10, 4), Size.of(13, 20), RectangleDrawOptions.filled(Color.RED), CLIP);
    }
}
