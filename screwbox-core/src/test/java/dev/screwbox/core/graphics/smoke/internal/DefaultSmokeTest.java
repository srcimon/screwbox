package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.test.TestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class DefaultSmokeTest {

    DefaultSmoke smoke;
    ExecutorService executor;
    GraphicsConfiguration configuration;
    Frame renderTarget;

    @BeforeEach
    void setUp() {
        configuration = new GraphicsConfiguration(RenderingApi.OPEN_GL);
        executor = Executors.newSingleThreadExecutor();
        DefaultRenderer renderer = new DefaultRenderer();
        BufferedImage image = ImageOperations.createImage(128, 128);
        renderTarget = Frame.fromImage(image);
        renderer.updateContext(image::createGraphics);
        final var canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 128, 128));
        var viewportManager = new ViewportManager(new DefaultViewport(canvas, new DefaultCamera(canvas)), new RenderPipeline(executor, configuration, Mockito.mock(Engine.class)));
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
        smoke.render(0.01);

        Vector resultVelocity = smoke.velocityAt($(20, 10));
        assertThat(resultVelocity.x()).isEqualTo(141.14, offset(0.01));
        assertThat(resultVelocity.y()).isEqualTo(41.92, offset(0.01));

        Color resultDensity = smoke.densityAt($(20, 10));
        assertThat(resultDensity.r()).isEqualTo(100);
        assertThat(resultDensity.g()).isZero();
        assertThat(resultDensity.b()).isZero();
        assertThat(resultDensity.opacity().value()).isEqualTo(0.39, offset(0.01));
    }

    @AfterEach
    void tearDown() {
        TestSupport.shutdown(executor);
    }
}
