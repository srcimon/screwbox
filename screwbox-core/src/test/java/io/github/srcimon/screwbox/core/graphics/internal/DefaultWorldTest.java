package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultWorldTest {

    @Mock
    DefaultCanvas canvas;

    @Mock
    Renderer renderer;

    @Mock
    Camera camera;

    DefaultWorld world;

    @BeforeEach
    void setUp() {
        DefaultViewport viewport = new DefaultViewport(canvas, camera);
        world = new DefaultWorld(new ViewportManager(viewport, renderer));
    }

    @Test
    void drawRectangle_drawsRectangleOnCanvas() {
        RectangleDrawOptions options = RectangleDrawOptions.filled(Color.RED);
        when(camera.focus()).thenReturn(Vector.$(40, 90));
        when(camera.zoom()).thenReturn(1.4);
        world.drawRectangle($$(10, 30, 30, 90), options);

        verify(canvas).drawRectangle(new ScreenBounds(-42, -84, 42, 126), options);
    }

    @Test
    void drawSprite_drawsScaledSprite() {
        when(camera.focus()).thenReturn(Vector.$(40, 90));
        when(camera.zoom()).thenReturn(2.0);
        var sprite = SpriteBundle.DOT_YELLOW.get();

        world.drawSprite(sprite, Vector.$(20, 90), SpriteDrawOptions.scaled(2).opacity(0.2));

        verify(canvas).drawSprite(sprite, Offset.at(-40, 0), SpriteDrawOptions.scaled(4).opacity(0.2));
    }
}
