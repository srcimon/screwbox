package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.Vector.zero;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultWorldTest {

    DefaultWorld world;

    @Mock
    DefaultCanvas defaultCanvas;

    @BeforeEach
    void setUp() {
        when(defaultCanvas.width()).thenReturn(1024);
        when(defaultCanvas.height()).thenReturn(768);
        world = new DefaultWorld(defaultCanvas);
    }

    @Test
    void visibleArea_noCameraUpdateYet_returnsVisibleArea() {
        assertThat(world.visibleArea()).isEqualTo($$(-512, -384, 1024, 768));
    }

    @Test
    void updateCameraPosition_updatesVisibleArea() {
        world.updateCameraPosition(Vector.of(100, 20));

        assertThat(world.visibleArea())
                .isEqualTo(Bounds.atPosition(100, 20, 1024, 768));
    }

    @Test
    void drawCircle_callsScreen() {
        world.updateCameraPosition($(4, 2));
        world.updateZoom(2);

        world.drawCircle($(20, 19), 10, CircleDrawOptions.fading(RED));
        verify(defaultCanvas).drawCircle(Offset.at(544, 418), 20, CircleDrawOptions.fading(RED));
    }

    @Test
    void drawRectangle_callsScreen() {
        world.updateCameraPosition(zero());
        world.updateZoom(2.5);

        world.drawRectangle(Bounds.atPosition(0, 0, 100, 100), RectangleDrawOptions.filled(RED));

        verify(defaultCanvas).drawRectangle(Offset.at(387, 259), Size.of(250, 250), RectangleDrawOptions.filled(RED));
    }

    @Test
    void drawText_systemText_callsScreen() {
        world.drawText($(20, 19), "Hello World", SystemTextDrawOptions.systemFont("Arial").bold());

        verify(defaultCanvas).drawText(Offset.at(532, 403), "Hello World", SystemTextDrawOptions.systemFont("Arial").bold());
    }

    @Test
    void drawText_pixelfont_callsScreen() {
        world.updateZoom(2);
        world.drawText($(20, 19), "Hello World", TextDrawOptions.font(FontBundle.BOLDZILLA).scale(4));

        verify(defaultCanvas).drawText(Offset.at(552, 422), "Hello World", TextDrawOptions.font(FontBundle.BOLDZILLA).scale(8));
    }

    @Test
    void toScreen_usingParallax_returnsScreenBounds() {
        when(defaultCanvas.offset()).thenReturn(Offset.origin());
        world.updateZoom(2);

        var result = world.toScreen($$(10, 20, 40, 80), 1.4, 1.2);

        assertThat(result).isEqualTo(new ScreenBounds(532, 424, 80, 160));
    }
}