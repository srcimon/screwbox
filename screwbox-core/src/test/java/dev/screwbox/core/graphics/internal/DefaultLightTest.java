package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@MockitoSettings
class DefaultLightTest {

    @Mock
    Viewport viewport;

    @Mock
    RenderPipeline renderPipeline;

    GraphicsConfiguration configuration;

    ExecutorService executor;

    DefaultLight light;

    @BeforeEach
    void setUp() {
        configuration = new GraphicsConfiguration();
        executor = Executors.newCachedThreadPool();
        light = new DefaultLight(configuration, new ViewportManager(viewport, renderPipeline), executor);
    }

    @Test
    void setAmbientLight_ambientLightNull_throwsException() {
        assertThatThrownBy(() -> light.setAmbientLight(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ambient light must not be null");
    }

    @Test
    void addOrthographicWall_notNull_enablesLight() {
        light.addOrthographicWall($$(20, 40, 10, 20));

        assertThat(configuration.isLightEnabled()).isTrue();
    }

    @Test
    void addExpandedLight_notNull_enablesLight() {
        light.addExpandedLight($$(20, 40, 10, 20), Color.RED, 4, false);

        assertThat(configuration.isLightEnabled()).isTrue();
    }

    @Test
    void setAmbientLight_notNull_setsLight() {
        light.setAmbientLight(Percent.half());

        assertThat(light.ambientLight()).isEqualTo(Percent.half());
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
