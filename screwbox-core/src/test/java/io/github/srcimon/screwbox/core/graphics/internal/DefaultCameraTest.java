package io.github.srcimon.screwbox.core.graphics.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCameraTest {

    @Mock
    DefaultWorld world;

    @InjectMocks
    DefaultCamera camera;

    @Test
    void moveCameraWithinVisualBounds_cameraIsWithinBounds_updatesCameraPosition() {
        camera.updatePosition($(10, 10));
        when(world.visibleArea()).thenReturn($$(-50, -50, 100, 100));

        var result = camera.moveWithinVisualBounds($(20, 20), $$(-200, -20, 400, 400));

        verify(world).updateCameraPosition($(30, 30));
        assertThat(result).isEqualTo($(30, 30));
    }
}
