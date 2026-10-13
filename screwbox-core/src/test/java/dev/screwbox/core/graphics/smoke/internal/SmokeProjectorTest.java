package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Size;
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
        var viewport = TestSupport.createInMemoryViewport(Size.square(30));
        smokeProjector.adaptToViewport(viewport);
        smokeProjector.applyDensityChanges(List.of(
            new SmokeProjector.DensityChange(Vector.zero(), 0.01, Color.RED)
        ));

        assertThat(smokeProjector.densityAt(Vector.zero())).isEqualTo(Color.RED.opacity(0.01));
        assertThat(smokeProjector.densityAt(Vector.$(10, 0))).isEqualTo(Color.TRANSPARENT);
    }

    //TODO Reference ticket for more smoke tests

    @AfterEach
    void tearDown() {
        TestSupport.shutdown(executor);
    }
}
