package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultLightTest {

    ExecutorService executor;

    @Mock
    Canvas canvas;

    @Mock
    Viewport viewport;

    @Captor
    ArgumentCaptor<Asset<Sprite>> spriteCaptor;

    DefaultWorld world;
    GraphicsConfiguration configuration;
    DefaultLight light;

    @BeforeEach
    void beforeEach() {
        when(viewport.canvas()).thenReturn(canvas);
        when(canvas.size()).thenReturn(Size.of(640, 480));
        world = new DefaultWorld(canvas, viewport);
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        light = new DefaultLight(configuration, executor, viewport);
    }

    @Test
    void render_ambientLightIsMax_doenstRenderLightmap() {
        light.setAmbientLight(Percent.max());

        light.render();

        verify(canvas, never()).drawSprite(any(Sprite.class), any(), any());
    }

    @Test
    void render_renderAlreadyCalled_throwsException() {
        light.render();

        assertThatThrownBy(() -> light.render())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("rendering lights is already in progress");
    }

    @Test
    void setAmbientLight_validInput_setsAmbientLight() {
        light.setAmbientLight(Percent.of(0.4));

        assertThat(light.ambientLight()).isEqualTo(Percent.of(0.4));
    }

    @Test
    void setAmbientLight_ambientLightNull_throwsException() {
        assertThatThrownBy(() -> light.setAmbientLight(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ambient light must not be null");
    }

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }

}
