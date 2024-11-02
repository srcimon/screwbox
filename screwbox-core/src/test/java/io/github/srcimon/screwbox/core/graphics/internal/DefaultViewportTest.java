package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultViewportTest {

    @Mock
    DefaultCanvas canvas;

    @Mock
    Camera camera;

    DefaultViewport viewport;

    @BeforeEach
    void setUp() {
        viewport = new DefaultViewport(canvas, camera);
    }

    @Test
    void toCanvas_boundsIntersectingCanvas_returnsBounds() {
        when(camera.focus()).thenReturn($(70, 180));
        when(camera.zoom()).thenReturn(1.5);
        when(canvas.width()).thenReturn(90);
        when(canvas.height()).thenReturn(50);

        assertThat(viewport.toCanvas($$(40, 100, 100, 400)))
                .isEqualTo(new ScreenBounds(0, -95, 150, 600));
    }

    @Test
    void toCanvas_usingZoom_returnsDistance() {
        when(camera.zoom()).thenReturn(1.5);

        assertThat(viewport.toCanvas(100.50))
                .isEqualTo(151);
    }

    @Test
    void toCanvas_usingParallax_returnsBounds() {
        when(camera.focus()).thenReturn($(100, 90));
        when(camera.zoom()).thenReturn(2.0);
        when(canvas.width()).thenReturn(640);
        when(canvas.height()).thenReturn(480);

        var result = viewport.toCanvas($$(40, 100, 100, 400), 1.5, 2.0);

        assertThat(result).isEqualTo(new ScreenBounds(100, 80, 200, 800));
    }

    @Test
    void visibleArea_usingZoom_returnsVisibleArea() {
        when(camera.focus()).thenReturn($(100, 90));
        when(camera.zoom()).thenReturn(2.0);
        when(canvas.width()).thenReturn(640);
        when(canvas.height()).thenReturn(480);

        var area = viewport.visibleArea();

        assertThat(area).isEqualTo($$(-60, -30, 320, 240));
    }

    @Test
    void toWorld_usingZoom_returnsPosition() {
        when(camera.focus()).thenReturn($(100, 90));
        when(camera.zoom()).thenReturn(2.0);
        when(canvas.width()).thenReturn(640);
        when(canvas.height()).thenReturn(480);

        var position = viewport.toWorld(Offset.at(100, 200));

        assertThat(position).isEqualTo($(260, 210));
    }
}
