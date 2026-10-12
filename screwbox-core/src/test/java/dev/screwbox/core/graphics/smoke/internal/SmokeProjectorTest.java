package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.internal.renderer.FirewallRenderer;
import dev.screwbox.core.test.TestUtil;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class SmokeProjectorTest {


    SmokeProjector smokeProjector;

    ExecutorService executor;
    GraphicsConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new GraphicsConfiguration(RenderingApi.OPEN_GL);
        executor = Executors.newSingleThreadExecutor();
        smokeProjector = new SmokeProjector(executor, configuration);
    }

    @Test
    void densityAt_noSimulationRunning_isNull() {
        assertThat(smokeProjector.densityAt(Vector.zero())).isNull();
    }

    @Test
    void velocityAt_noSimulationRunning_isNull() {
        assertThat(smokeProjector.velocityAt(Vector.zero())).isNull();
    }

    @Test
    void applyDensityChanges_addRedDensity_addsRedDensityOnlyAtSpecifiedPosition() {

        var viewport = fakeViewport();
        smokeProjector.adaptToViewport(viewport);
        smokeProjector.applyDensityChanges(List.of(
            new SmokeProjector.DensityChange(Vector.zero(), 0.01, Color.RED)
        ));

        assertThat(smokeProjector.densityAt(Vector.zero())).isEqualTo(Color.RED.opacity(0.01));//TODO found bug?
    }

    //TODO move to test utils and check where to reuse
    private static DefaultViewport fakeViewport() {
        var image = ImageOperations.createImage(Size.square(30));
        final var render = new DefaultRenderer();
        render.updateContext(image::createGraphics);
        var canvas = new DefaultCanvas(new FirewallRenderer(render), new ScreenBounds(Size.square(30)));
        return new DefaultViewport(canvas, new DefaultCamera(canvas));
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
