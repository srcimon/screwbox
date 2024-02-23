package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Size;
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
    DefaultGraphics defaultGraphics;

    @Mock
    GraphicsDevice graphicsDevice;

    @Test
    void supportedResolutions_threeDisplayModes_returnsReverseOrderedListOfDistinctModes() {
        when(graphicsDevice.getDisplayModes()).thenReturn(List.of(
                        new DisplayMode(800, 600, 16, 60),
                        new DisplayMode(800, 600, 32, 60),
                        new DisplayMode(1024, 768, 32, 60))
                .toArray(new DisplayMode[]{}));

        List<Size> supportedResolutions = defaultGraphics.supportedResolutions();

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

        List<Size> supportedResolutions = defaultGraphics.supportedResolutions(WIDESCREEN);

        assertThat(supportedResolutions).containsExactly(Size.of(1600, 900));
    }
}
