package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultWindowTest {

    @InjectMocks
    DefaultWindow window;

    @Mock
    WindowFrame frame;

    @Mock
    GraphicsConfiguration configuration;

    @Mock
    GraphicsDevice graphicsDevice;

    @Mock
    RenderPipeline renderPipeline;

    @Mock
    CursorLockInSupport cursorLockInSuppor;

    @Test
    void close_noSavedDisplayMode_doesNotSwitchBackToLastDisplayMode() {
        when(frame.getBounds()).thenReturn(new Rectangle(0, 0, 100, 100));

        window.close();

        verify(renderPipeline).toggleOnOff();
        verifyNoInteractions(graphicsDevice);
    }

}
