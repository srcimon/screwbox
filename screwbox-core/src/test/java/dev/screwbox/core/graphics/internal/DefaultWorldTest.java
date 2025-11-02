package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultWorldTest {

    @Mock
    DefaultCanvas canvas;

    @Mock
    RenderPipeline renderPipeline;

    @Mock
    DefaultCamera camera;

    DefaultWorld world;

    @BeforeEach
    void setUp() {
        DefaultViewport viewport = new DefaultViewport(canvas, camera);
        world = new DefaultWorld(new ViewportManager(viewport, renderPipeline));
    }

    @Test
    void drawRectangle_drawsRectangleOnCanvas() {
        RectangleDrawOptions options = RectangleDrawOptions.filled(Color.RED);
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(1.4);
        world.drawRectangle($$(10, 30, 30, 90), options);

        verify(canvas).drawRectangle(new ScreenBounds(-42, -84, 42, 126), options);
    }

    @Test
    void drawSprite_drawsScaledSprite() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);
        var sprite = SpriteBundle.DOT_YELLOW.get();

        world.drawSprite(sprite, $(20, 90), SpriteDrawOptions.scaled(2).opacity(0.2));

        verify(canvas).drawSprite(sprite, Offset.at(-40, 0), SpriteDrawOptions.scaled(4).opacity(0.2));
    }

    @Test
    void drawPolygon_drawsPolygon() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);

        world.drawPolygon(List.of($(10, 30), $(30, 20), $(15, 9)), PolygonDrawOptions.filled(Color.RED));

        verify(canvas).drawPolygon(List.of(Offset.at(-60, -120), Offset.at(-20, -140), Offset.at(-50, -162)), PolygonDrawOptions.filled(Color.RED));
    }
}
