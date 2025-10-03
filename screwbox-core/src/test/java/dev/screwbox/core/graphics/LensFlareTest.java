package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class LensFlareTest {

    @Mock
    DefaultCanvas canvas;

    Camera camera;
    Viewport viewport;

    @BeforeEach
    void setUp() {
        camera = new DefaultCamera(canvas);
        viewport = new DefaultViewport(canvas, camera);
    }

    @Test
    void newInstance_noRays_hasNoRays() {
        var lensFlare = LensFlare.noRays()
                .orb(2, 2, 0.5)
                .orb(1, 3, 0.5);


        assertThat(lensFlare.useRotation()).isFalse();
        assertThat(lensFlare.rayCount()).isZero();
        assertThat(lensFlare.orbs()).containsExactly(
                new LensFlare.Orb(2, 2, 0.5),
                new LensFlare.Orb(1, 3, 0.5));
    }

    @Test
    void newInstance_fullyConfigured_setsAllProperties() {
        var lensFlare = LensFlare.rayCount(4)
                .rayOpacity(0.5)
                .rayLength(4)
                .smoothing(Percent.of(0.4))
                .rayWidth(8)
                .rayRotationSpeed(-3)
                .orb(2, 2, 0.5)
                .enableRotation();

        assertThat(lensFlare.useRotation()).isTrue();
        assertThat(lensFlare.rayCount()).isEqualTo(4);
        assertThat(lensFlare.rayOpacity()).isEqualTo(0.5);
        assertThat(lensFlare.rayWidth()).isEqualTo(8);
        assertThat(lensFlare.rayRotationSpeed()).isEqualTo(-3);
        assertThat(lensFlare.smoothing()).isEqualTo(Percent.of(0.4));
        assertThat(lensFlare.orbs()).containsExactly(new LensFlare.Orb(2, 2, 0.5));
    }

    @Test
    void render_areaNoRaysWithoutRotation_rendersOnlyOrbs() {
        var lensFlare = LensFlare.noRays()
                .orb(2, 2, 0.5)
                .orb(1, 3, 0.5);

        lensFlare.render($$(-16, -24, 100, 100), 8, Color.WHITE, viewport);
        verify(canvas).drawRectangle(new ScreenBounds(-126, -110, 116, 116), RectangleDrawOptions.fading(Color.WHITE.opacity(0.5)).curveRadius(52));
        verify(canvas).drawRectangle(new ScreenBounds(-96, -88, 124, 124), RectangleDrawOptions.fading(Color.WHITE.opacity(0.5)).curveRadius(55));

        verify(canvas, never()).drawLine(any(), any(), any());
    }

    @Test
    void render_areaNoRaysUsingRotation_rendersOnlyOrbs() {
        var lensFlare = LensFlare.noRays()
                .orb(2, 2, 0.5)
                .orb(1, 3, 0.5)
                .enableRotation();

        lensFlare.render($$(-16, -24, 100, 100), 8, Color.WHITE, viewport);
        verify(canvas, times(2)).drawRectangle(any(), argThat(options -> Math.round(options.rotation().degrees()) == 127));

        verify(canvas, never()).drawLine(any(), any(), any());
    }

    @Test
    void render_noRays_rendersOrbs() {
        var lensFlare = LensFlare.noRays()
                .orb(2, 2, 0.5)
                .orb(1, 3, 0.5);

        lensFlare.render($(-16, -24), 8, Color.WHITE, viewport);

        verify(canvas).drawCircle(Offset.at(32, 48), 16, CircleDrawOptions.fading(Color.WHITE.opacity(0.5)));
        verify(canvas).drawCircle(Offset.at(16, 24), 24, CircleDrawOptions.fading(Color.WHITE.opacity(0.5)));
        verify(canvas, never()).drawLine(any(), any(), any());
    }

    @Test
    void render_noOrbs_rendersRays() {
        var lensFlare = LensFlare.rayCount(2)
                .rayOpacity(10);

        lensFlare.render($(-16, -24), 8, Color.WHITE, viewport);

        verify(canvas).drawLine(Offset.at(-16, -24), Offset.at(-16, -8), LineDrawOptions.color(Color.WHITE));
        verify(canvas).drawLine(Offset.at(-16, -24), Offset.at(-16, -40), LineDrawOptions.color(Color.WHITE));
        verify(canvas, never()).drawCircle(any(), anyInt(), any());
    }

    @Test
    void render_orbsAndRays_rendersBoth() {
        var lensFlare = LensFlare.rayCount(2)
                .rayOpacity(10)
                .orb(2, 2, 0.5)
                .orb(1, 3, 0.5);

        lensFlare.render($(-16, -24), 8, Color.WHITE, viewport);

        verify(canvas).drawCircle(Offset.at(32, 48), 16, CircleDrawOptions.fading(Color.WHITE.opacity(0.5)));
        verify(canvas).drawCircle(Offset.at(16, 24), 24, CircleDrawOptions.fading(Color.WHITE.opacity(0.5)));
        verify(canvas).drawLine(Offset.at(-16, -24), Offset.at(-16, -8), LineDrawOptions.color(Color.WHITE));
        verify(canvas).drawLine(Offset.at(-16, -24), Offset.at(-16, -40), LineDrawOptions.color(Color.WHITE));
    }

    @Test
    void newInstance_negativeRayCount_throwsException() {
        assertThatThrownBy(() -> LensFlare.rayCount(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ray count must be positive (actual value: -1)");
    }

    @Test
    void rayRotationSpeed_outOfRange_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.rayRotationSpeed(40))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ray rotation speed must be in range from -10 to 10 (actual value: 40.0)");
    }

    @Test
    void rayOpacity_negative_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.rayOpacity(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ray opacity must be in range from 0.01 to 10 (actual value: -1.0)");
    }

    @Test
    void rayWidth_negative_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.rayWidth(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ray width must be in range from 1 to 64 (actual value: -1)");
    }

    @Test
    void rayLength_negative_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.rayLength(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ray length must be in range from 1 to 10 (actual value: -1.0)");
    }

    @Test
    void orb_distanceTooFar_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.orb(-40, 4, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("orb distance must be in range -10 to 10 (actual value: -40.0)");
    }

    @Test
    void orb_sizeTooBig_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.orb(-4, 40, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("orb size must be in range 0.01 to 10 (actual value: 40.0)");
    }

    @Test
    void orb_opacityZero_throwsException() {
        final var lensFlare = LensFlare.noRays();

        assertThatThrownBy(() -> lensFlare.orb(-4, 8, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("orb opacity must be in range 0.01 to 10 (actual value: 0.0)");
    }
}
