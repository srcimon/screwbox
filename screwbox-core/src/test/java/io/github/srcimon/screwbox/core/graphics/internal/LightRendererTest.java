package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class LightRendererTest {

    ExecutorService executor;

    @Mock
    Canvas canvas;

    @Mock
    Viewport viewport;

    @Mock
    LightPhysics lightPhysics;

    GraphicsConfiguration configuration;
    LightRenderer lightRenderer;

    @BeforeEach
    void setUp() {
        when(viewport.canvas()).thenReturn(canvas);
        when(canvas.size()).thenReturn(Size.of(320, 240));
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        lightRenderer = new LightRenderer(lightPhysics, configuration, executor, viewport, postFilter -> postFilter);
    }

    @Test
    void renderLight_noLights_isBlack() {
        var lightmap = lightRenderer.renderLight();

        assertThat(lightmap.get().singleFrame().colors()).containsExactly(Color.BLACK);
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
