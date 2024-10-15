package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.AsyncRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.AspectRatio.WIDESCREEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGraphicsTest {

    @InjectMocks
    DefaultGraphics graphics;

    @Mock
    GraphicsDevice graphicsDevice;

    @Mock
    DefaultWorld world;

    @Mock
    AsyncRenderer asyncRenderer;

    @Test
    void render_renderDuration_returnsRenderDurationOfAsyncRenderer() {
        when(asyncRenderer.renderDuration()).thenReturn(Duration.ofMicros(20));
        assertThat(graphics.renderDuration()).isEqualTo(Duration.ofMicros(20));
    }

    @Test
    void supportedResolutions_threeDisplayModes_returnsReverseOrderedListOfDistinctModes() {
        when(graphicsDevice.getDisplayModes()).thenReturn(List.of(
                        new DisplayMode(800, 600, 16, 60),
                        new DisplayMode(800, 600, 32, 60),
                        new DisplayMode(1024, 768, 32, 60))
                .toArray(new DisplayMode[]{}));

        List<Size> supportedResolutions = graphics.supportedResolutions();

        assertThat(supportedResolutions).containsExactly(Size.of(1024, 768), Size.of(800, 600));
    }

    @Test
    void supportedResolutions_onlyWidescreen_returnsOnlyWidescreenResolutions() {
        when(graphicsDevice.getDisplayModes()).thenReturn(List.of(
                        new DisplayMode(800, 600, 16, 60),
                        new DisplayMode(800, 600, 32, 60),
                        new DisplayMode(1600, 900, 32, 60),
                        new DisplayMode(1024, 768, 32, 60))
                .toArray(new DisplayMode[]{}));

        List<Size> supportedResolutions = graphics.supportedResolutions(WIDESCREEN);

        assertThat(supportedResolutions).containsExactly(Size.of(1600, 900));
    }

    @Test
    void toViewport_returnsTranslatedBounds() {
        when(world.toScreen(Bounds.$$(20, 20, 10, 2))).thenReturn(new ScreenBounds(4, 1, 10, 10));

        var result = graphics.toViewport(Bounds.$$(20, 20, 10, 2));

        assertThat(result).isEqualTo(new ScreenBounds(4, 1, 10, 10));
    }

}
