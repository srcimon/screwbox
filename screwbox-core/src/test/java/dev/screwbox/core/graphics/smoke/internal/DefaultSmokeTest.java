package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MockitoSettings
class DefaultSmokeTest {

    DefaultSmoke smoke;

    ViewportManager viewportManager;

    @Mock
    ExecutorService executor;

    @Spy
    GraphicsConfiguration configuration = new GraphicsConfiguration(RenderingApi.OPEN_GL);

    @Mock
    Engine engine;

    @BeforeEach
    void setUp() {
        DefaultRenderer renderer = new DefaultRenderer();
        final var image = ImageOperations.createImage(128, 128);
        renderer.updateContext(image::createGraphics);
        final var canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 128, 128));
        viewportManager = new ViewportManager(new DefaultViewport(canvas, new DefaultCamera(canvas)), new RenderPipeline(executor, configuration, engine));
        smoke = new DefaultSmoke(viewportManager, configuration, executor);
    }

    @Test
    void emit_postionNegativeAmount_throwsException() {
        Vector position = $(40, 10);
        assertThatThrownBy(() -> smoke.emit(position, -0.1, Color.RED))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("amount must be positive (actual value: -0.1)");
    }

    @Test
    void emit_areaNegativeAmount_throwsException() {
        var area = $$(40, 10, 19, 30);
        assertThatThrownBy(() -> smoke.emit(area, -0.1, Color.RED))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("amount must be positive (actual value: -0.1)");
    }

    @Test
    void emit_autoTurnOnEnabled_activatesSmoke() {
        smoke.emit($(4, 1), 20, Color.WHITE);

        assertThat(configuration.isSmokeEnabled()).isTrue();
    }

    @Test
    void emit_atPostionAndAutoTurnOnDisabled_leavesSmokeDisabled() {
        configuration.setAutoEnableSmoke(false);

        smoke.emit($(4, 1), 20, Color.WHITE);

        assertThat(configuration.isSmokeEnabled()).isFalse();
    }

    @Test
    void emit_atAreaAndAutoTurnOnDisabled_leavesSmokeDisabled() {
        configuration.setAutoEnableSmoke(false);

        smoke.emit($$(4, 1, 10, 250), 20, Color.WHITE);

        assertThat(configuration.isSmokeEnabled()).isFalse();
    }

    @Test
    void setOptions_optionsNull_throwsException() {
        assertThatThrownBy(() -> smoke.setOptions(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("options must not be null");
    }

    @Test
    void densityAt_currentlyNoSmoke_isTransparent() {
        var density = smoke.densityAt($(40, 10));
        assertThat(density).isEqualTo(Color.TRANSPARENT);
    }

    @Test
    void push_areaPush_enablesSmoke() {
        smoke.push($$(20, 10, 64, 32), Vector.x(200));

        assertThat(configuration.isSmokeEnabled()).isTrue();
    }

    @Test
    void render_velocityAndDensityChangesPresent_updatesVelocityAndDensity() {
        smoke.push($$(20, 10, 64, 32), Vector.$(200, 0));
        smoke.emit($$(20, 10, 64, 32), 0.5, Color.RED);
        smoke.render(1.0);

        assertThat(smoke.velocityAt($(20, 10))).isEqualTo($(200, 0));
        assertThat(smoke.densityAt($(20, 10))).isEqualTo(Color.rgb(127, 0, 0, Percent.half()));
    }
}
