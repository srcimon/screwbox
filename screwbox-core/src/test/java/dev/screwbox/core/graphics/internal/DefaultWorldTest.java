package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.graphics.options.TextureLineDrawOptions;
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

        verify(canvas).drawPolygon(List.of(Offset.at(-60, -120), Offset.at(-20, -140), Offset.at(-50, -162)), PolygonDrawOptions.filled(Color.RED).strokeWidth(2));
    }

    @Test
    void drawLine_textureOptions_drawsTextureLine() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);

        world.drawLine($(10, 20), $(9, 3), SpriteBundle.BOX, TextureLineDrawOptions.scaled(3).opacity(Percent.half()));

        verify(canvas).drawLine(Offset.at(-60, -140), Offset.at(-62, -174), SpriteBundle.BOX.get(), TextureLineDrawOptions.scaled(6).opacity(Percent.half()));
    }

    @Test
    void drawLine_strokeOptions_drawsLine() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);

        world.drawLine($(10, 20), $(9, 3), LineDrawOptions.color(Color.RED).strokeWidth(10));

        verify(canvas).drawLine(Offset.at(-60, -140), Offset.at(-62, -174), LineDrawOptions.color(Color.RED).strokeWidth(20.0));
    }

    @Test
    void drawOval_validOptions_drawsOval() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);

        world.drawOval($(20, 10), 20, 10, OvalDrawOptions.outline(Color.RED));

        verify(canvas).drawOval(Offset.at(-40, -160), 40, 20, OvalDrawOptions.outline(Color.RED));
    }

    @Test
    void drawText_systemText_drawsSystemText() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);

        world.drawText($(20,10), "test", SystemTextDrawOptions.systemFont("Arial"));

        verify(canvas).drawText(Offset.at(-40,-160), "test", SystemTextDrawOptions.systemFont("Arial"));
    }

    @Test
    void drawText_pixelText_drawsPixelText() {
        when(camera.focus()).thenReturn($(40, 90));
        when(camera.zoom()).thenReturn(2.0);

        world.drawText($(20,10), "test", TextDrawOptions.font(FontBundle.SKINNY_SANS));

        verify(canvas).drawText(Offset.at(-40,-160), "test", TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(2.0));
    }
}
