package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Bounds.$$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultWorldTest {

    @Mock
    DefaultCanvas canvas;

    @Mock
    RenderPipeline renderPipeline;

    @Mock
    Camera camera;

    DefaultWorld world;

    @BeforeEach
    void setUp() {
        DefaultViewport viewport = new DefaultViewport(canvas, camera);
        world = new DefaultWorld(new ViewportManager(viewport, renderPipeline));
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
