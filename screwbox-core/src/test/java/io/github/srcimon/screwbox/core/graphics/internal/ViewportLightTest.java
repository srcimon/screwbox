package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.mockito.Mockito.when;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class ViewportLightTest {

    ExecutorService executor;

    @Mock
    Canvas canvas;

    @Mock
    Viewport viewport;

    @Mock
    LightPhysics lightPhysics;

    GraphicsConfiguration configuration;
    ViewportLight light;

    @BeforeEach
    void setUp() {
        when(viewport.canvas()).thenReturn(canvas);
        when(canvas.size()).thenReturn(Size.of(640, 480));
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        light = new ViewportLight(lightPhysics, configuration, executor, viewport, postFilter -> postFilter);
    }
    //TODO implement
}
