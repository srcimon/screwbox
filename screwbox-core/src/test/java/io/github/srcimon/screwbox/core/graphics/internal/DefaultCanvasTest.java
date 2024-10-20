package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
        canvas.drawCircle(Offset.at(10, 20), 4, CircleDrawOptions.fading(Color.RED));

        verify(renderer).drawCircle(Offset.at(10, 20), 4, CircleDrawOptions.fading(Color.RED), CLIP);
    }


    @Test
    void drawSpriteBatch_callsRenderer() {
        var spriteBatch = new SpriteBatch();

        canvas.drawSpriteBatch(spriteBatch);

        verify(renderer).drawSpriteBatch(spriteBatch, CLIP);
    }
}
