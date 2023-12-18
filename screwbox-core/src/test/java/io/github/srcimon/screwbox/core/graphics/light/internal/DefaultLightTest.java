package io.github.srcimon.screwbox.core.graphics.light.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultLight;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultWorld;
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

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultLightTest {

    ExecutorService executor;

    @Mock
    Screen screen;

    @Captor
    ArgumentCaptor<Asset<Sprite>> spriteCaptor;

    DefaultWorld world;
    GraphicsConfiguration configuration;
    DefaultLight light;

    @BeforeEach
    void beforeEach() {
        when(screen.size()).thenReturn(Size.of(640, 480));
        world = new DefaultWorld(screen);
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        light = new DefaultLight(screen, world, configuration, executor);
    }

    @Test
    void addShadowCasters_castersNull_throwsException() {
        assertThatThrownBy(() -> light.addShadowCasters(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shadowCasters must not be null");
    }

    @Test
    void render_fullBrigthnessAreaPresent_rendersImage() {
        when(screen.isVisible(any(ScreenBounds.class))).thenReturn(true);
        light.addFullBrightnessArea($$(20, 20, 50, 50));

        light.render();

        verify(screen).drawSprite(spriteCaptor.capture(), any(Offset.class), anyDouble(), any(Percent.class));

        Frame resultImage = spriteCaptor.getValue().get().singleFrame();

        Color colorInShadow = resultImage.colorAt(25, 25);
        assertThat(colorInShadow).isEqualTo(Color.BLACK);

        Color colorInArea = resultImage.colorAt(92, 72);
        assertThat(colorInArea.opacity().value()).isLessThan(0.1);
    }

    @Test
    void render_pointLightAndShadowPresent_rendersImage() {
        when(screen.isVisible(any(ScreenBounds.class))).thenReturn(true);
        light.addShadowCaster($$(30, 75, 6, 6));
        light.addPointLight($(40, 80), LightOptions.glowing(140).color(Color.RED));

        light.render();
        var offset = ArgumentCaptor.forClass(Offset.class);
        var resolution = ArgumentCaptor.forClass(Double.class);
        var opacity = ArgumentCaptor.forClass(Percent.class);

        verify(screen).drawSprite(
                spriteCaptor.capture(),
                offset.capture(),
                resolution.capture(),
                opacity.capture());

        Frame resultImage = spriteCaptor.getValue().get().singleFrame();

        Color colorInShadow = resultImage.colorAt(78, 77);
        assertThat(colorInShadow).isEqualTo(Color.BLACK);

        Color colorInLight = resultImage.colorAt(93, 83);
        assertThat(colorInLight.r()).isEqualTo(248);
        assertThat(colorInLight.g()).isZero();
        assertThat(colorInLight.b()).isZero();
        assertThat(colorInLight.opacity().value()).isPositive();
    }

    @Test
    void render_renderAlreadyCalled_throwsException() {
        light.render();

        assertThatThrownBy(() -> light.render())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("rendering lights is already in progress");
    }

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }

}
