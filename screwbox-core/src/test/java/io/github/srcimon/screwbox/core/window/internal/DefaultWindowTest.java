package io.github.srcimon.screwbox.core.window.internal;

import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultScreen;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultWindowTest {

    @Mock
    WindowFrame frame;

    @Mock
    GraphicsDevice graphicsDevice;

    @Mock
    GraphicsConfiguration configuration;

    @Mock
    DefaultScreen screen;

    @Mock
    Renderer renderer;

    @InjectMocks
    DefaultWindow window;

    @Test
    void position_returnsPositionOfFrame() {
        when(frame.getBounds()).thenReturn(new Rectangle(100, 20, 640, 480));

        assertThat(window.position()).isEqualTo(Offset.at(100, 20));
    }
}
