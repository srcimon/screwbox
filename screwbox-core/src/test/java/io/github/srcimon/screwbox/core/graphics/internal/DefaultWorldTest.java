package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultWorldTest {

    @Mock
    Canvas canvas;

    @Mock
    Viewport viewport;

    @InjectMocks
    DefaultWorld world;

    @BeforeEach
    void setUp() {
        when(viewport.canvas()).thenReturn(canvas);
    }

    @Test
    void drawRectangle_drawsRectangleOnCanvas() {
        RectangleDrawOptions options = RectangleDrawOptions.filled(Color.RED);
        when(viewport.toCanvas($$(10, 30, 30, 90))).thenReturn(new ScreenBounds(40, 30, 50, 100));
        world.drawRectangle($$(10, 30, 30, 90), options);

        Mockito.verify(canvas).drawRectangle(new ScreenBounds(40, 30, 50, 100), options);
    }
}
