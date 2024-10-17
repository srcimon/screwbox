package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OffsetRendererTest {

    private static final ScreenBounds CLIP = new ScreenBounds(0, 0, 100, 100);

    @Mock
    Renderer next;


    OffsetRenderer offsetRenderer;

    @BeforeEach
    void setUp() {
        offsetRenderer = new OffsetRenderer(Offset.at(10, 2), next);
    }

    @Test
    void drawCircle_drawsCircleWithTranslatedOffset() {
        var options = CircleDrawOptions.outline(Color.BLACK);

        offsetRenderer.drawCircle(Offset.at(10, 10), 4, options, CLIP);

        verify(next).drawCircle(Offset.at(20, 12), 4, options, CLIP);
    }
}
