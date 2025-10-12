package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void addAreaLight_notNull_enablesLight() {
        light.addAreaLight($$(20, 40, 10, 20), Color.RED, 4, false);

        assertThat(configuration.isLightEnabled()).isTrue();
    }

    @Test
    void setAmbientLight_notNull_setsLight() {
        light.setAmbientLight(Percent.half());

        assertThat(light.ambientLight()).isEqualTo(Percent.half());
    }

    @Test
    void scale_defaultConfiguration_isFour() {
        assertThat(light.scale()).isEqualTo(4);
    }

    @Test
    void scale_highResolution_setsHigherScale() {
        configuration.setResolution(2560, 1440);

        assertThat(light.scale()).isEqualTo(8);
    }

    @Test
    void scale_ultraHighResolution_setsMaxScale() {
        configuration.setResolution(256000, 144000);

        assertThat(light.scale()).isEqualTo(64);
    }

    @Test
    void scale_ultraLowResolution_setsMinScale() {
        configuration.setResolution(64, 48);

        assertThat(light.scale()).isOne();
    }

    @Test
    void render_lightEnabledDueToAutoEnable_rendersSprite() {
        Canvas canvas = Mockito.mock(Canvas.class);
        when(canvas.size()).thenReturn(Size.of(400, 300));
        when(viewport.canvas()).thenReturn(canvas);
        light.update();
        light.addOccluder($$(30, 30, 10, 500), true);
        light.addPointLight($(32, 32), 40, Color.BLACK);

        light.render();

        verify(canvas).drawSprite(any(Supplier.class), any(), any());


    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
