package dev.screwbox.core.window.internal;

import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void enableCursorLock_negativePadding_throwsException() {
        assertThatThrownBy(() -> window.enableCursorLock(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("padding must be in range 2 to 64 (actual value: -1)");
    }

    @Test
    void isCursorLockEnabled_cursorLockEnabled_returnsTrue() {
        window.enableCursorLock(10);

        assertThat(window.isCursorLockEnabled()).isTrue();
    }

    @Test
    void isCursorLockEnabled_cursorLockEnabledAndDisabled_returnsFalse() {
        window.enableCursorLock(4);
        window.disableCursorLock();

        assertThat(window.isCursorLockEnabled()).isTrue();
    }

    @Test
    void setTitle_titleValid_setsFrameTitle() {
        window.setTitle("Playground");
        verify(frame).setTitle("Playground");
    }

    @Test
    void moveTo_isInFullscreen_throwsException() {
        when(configuration.isFullscreen()).thenReturn(true);
        Offset position = Offset.at(0, 0);

        assertThatThrownBy(() -> window.moveTo(position))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("cannot move Window in fullscreen");
    }

    @Test
    void moveTo_isNotInFullscreen_movesFrame() {
        Offset position = Offset.at(20, 20);
        when(frame.getWidth()).thenReturn(512);
        when(frame.getHeight()).thenReturn(256);

        window.moveTo(position);

        verify(frame).setBounds(20, 20, 512, 256);
    }

    @Test
    void title_returnsFrameTitle() {
        when(frame.getTitle()).thenReturn("Playground");
        assertThat(window.title()).isEqualTo("Playground");
    }

    @Test
    void droppedFiles_noFilesDropped_isEmpty() {
      assertThat(window.droppedFiles()).isEmpty();
    }


}
