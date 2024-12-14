package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Duration;
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

@MockitoSettings
class DefaultGraphicsTest {

    @InjectMocks
    DefaultGraphics graphics;

    @Mock
    GraphicsDevice graphicsDevice;

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
    void currentResolution_returnsResolutionFromGraphicsDevice() {
        when(graphicsDevice.getDisplayMode()).thenReturn(new DisplayMode(640, 480, 32, 60));
        
        assertThat(graphics.currentResolution()).isEqualTo(Size.of(640, 480));
    }

}
