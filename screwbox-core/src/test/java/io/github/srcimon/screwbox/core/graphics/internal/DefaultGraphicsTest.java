package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.List;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.graphics.AspectRatio.WIDESCREEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultGraphicsTest {

    @InjectMocks
    DefaultGraphics graphics;

    @Mock
    DefaultWorld world;

    @Mock
    GraphicsDevice graphicsDevice;

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
    void updateCameraPositionWithinBounds_targetPositionIsWithinBounds_updatesPosition() {
        when(world.cameraPosition()).thenReturn($(40, 50));

        var result = graphics.updateCameraPositionWithinBounds($(40, 50), $$(20, 30, 100, 100));

        verify(world).updateCameraPosition($(40, 50));
        assertThat(result).isEqualTo($(40, 50));
    }
}
