package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.Vector.$;
import static de.suzufa.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Screen;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.internal.DefaultLight;
import de.suzufa.screwbox.core.graphics.internal.DefaultWorld;

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
        when(screen.size()).thenReturn(Dimension.of(640, 480));
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
    void updateShadowCasters_castersPresent_updatesObstacles() {
        Bounds shadowCaster = $$(0, 10, 20, 20);

        light.addShadowCaster(shadowCaster);

        assertThat(light.shadowCasters()).containsExactly(shadowCaster);
    }

    @Test
    void render_fullBrigthnessAreaPresent_rendersImage() throws IOException {
        when(screen.isVisible(any(WindowBounds.class))).thenReturn(true);
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
    void render_pointLightAndShadowPresent_rendersImage() throws Exception {
        when(screen.isVisible(any(WindowBounds.class))).thenReturn(true);
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

    @AfterEach
    void afterEach() {
        shutdown(executor);
    }

}
