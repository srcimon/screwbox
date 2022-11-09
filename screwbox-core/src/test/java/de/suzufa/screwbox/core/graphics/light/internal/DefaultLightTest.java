package de.suzufa.screwbox.core.graphics.light.internal;

import static de.suzufa.screwbox.core.Bounds.$$;
import static de.suzufa.screwbox.core.test.TestUtil.shutdown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.internal.DefaultLight;
import de.suzufa.screwbox.core.graphics.internal.DefaultWorld;

@Timeout(1)
@ExtendWith(MockitoExtension.class)
class DefaultLightTest {

    ExecutorService executor;

    @Mock
    Window window;

    @Captor
    ArgumentCaptor<Future<Sprite>> spriteCaptor;

    DefaultWorld world;
    GraphicsConfiguration configuration;
    DefaultLight light;

    @BeforeEach
    void beforeEach() {
        world = new DefaultWorld(window);
        configuration = new GraphicsConfiguration();
        executor = Executors.newSingleThreadExecutor();
        light = new DefaultLight(window, world, configuration, executor);
    }

    @Test
    void addShadowCasters_castersNull_throwsException() {
        assertThatThrownBy(() -> light.addShadowCasters(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shadowCasters must not be null");
    }

    @Test
    void updateShadowCasters_castersPresent_updatesObstacles() {
        Bounds obstacle = $$(0, 10, 20, 20);

        light.addShadowCasters(List.of(obstacle));

        assertThat(light.shadowCasters()).containsExactly(obstacle);
    }

    @Test
    void render_lightAndShadowPresent_createCorrectImage() throws Exception {
        when(window.size()).thenReturn(Dimension.of(640, 480));
        when(window.isVisible(any(WindowBounds.class))).thenReturn(true);
        light.addShadowCasters(List.of(Bounds.$$(30, 75, 6, 6)));
        light.addPointLight(Vector.$(40, 80), LightOptions.glowing(140).color(Color.RED));
        light.render();

        var offset = ArgumentCaptor.forClass(Offset.class);
        var resolution = ArgumentCaptor.forClass(Integer.class);
        var opacity = ArgumentCaptor.forClass(Percent.class);

        verify(window).drawSprite(
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
