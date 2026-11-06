package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.test.TestSupport;
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
        smokeProjector.adaptToViewport(TestSupport.createInMemoryViewport(Size.square(30)));
        smokeProjector.applyDensityChanges(List.of(
            new SmokeProjector.DensityChange(Vector.zero(), 0.01, Color.RED)
        ));

        assertThat(smokeProjector.densityAt(Vector.zero())).isEqualTo(Color.rgb(2, 0, 0, Percent.of(0.01)));
        assertThat(smokeProjector.densityAt(Vector.$(10, 0))).isEqualTo(Color.TRANSPARENT);
    }

    @Test
    void applyVelocityChanges_multipleChangesAtSameCell_changesAddUp() {
        smokeProjector.adaptToViewport(TestSupport.createInMemoryViewport(Size.square(30)));

        smokeProjector.applyVelocityChanges(List.of(
            new SmokeProjector.VelocityChange(Vector.zero(), Vector.y(14)),
            new SmokeProjector.VelocityChange(Vector.zero(), Vector.x(10))
        ));

        assertThat(smokeProjector.velocityAt(Vector.zero())).isEqualTo(Vector.$(10, 14));
    }

    @Test
    void applyAreaVelocityChanges_multipleChangesAtSameCell_changesAddUp() {
        smokeProjector.adaptToViewport(TestSupport.createInMemoryViewport(Size.square(30)));

        smokeProjector.applyAreaVelocityChanges(List.of(
            new SmokeProjector.AreaVelocityChange(Bounds.atPosition(0, 0, 32, 32), Vector.y(14)),
            new SmokeProjector.AreaVelocityChange(Bounds.atPosition(0, 0, 32, 32), Vector.x(10))
        ));

        assertThat(smokeProjector.velocityAt(Vector.zero())).isEqualTo(Vector.$(10, 14));
    }

    @Test
    void xxx() {
        //TODO TestSupport.createCanvas()?
        DefaultCanvas canvas = new DefaultCanvas(new DefaultRenderer(), new ScreenBounds(0, 0, 640, 480));
        DefaultViewport viewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
        smokeProjector.adaptToViewport(viewport);
        smokeProjector.render(viewport, SmokeOptions.noFade(), 0.01);
        //TODO remove executor from smokeprojector and let smokeProjector return asset
    }

    @AfterEach
    void tearDown() {
        TestSupport.shutdown(executor);
    }
}
