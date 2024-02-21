package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphicsTest {

    @Spy
    Graphics graphics;

    @Test
    void moveCameraBy_movesCameraRelativeToCurrentPosition() {
        when(graphics.cameraPosition()).thenReturn($(10, 2));

        graphics.moveCameraBy($(30, 20));

        verify(graphics).moveCameraTo($(40, 22));
    }
}
