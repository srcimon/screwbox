package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static io.github.srcimon.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DefaultScreenTest {

    @InjectMocks
    DefaultScreen screen;

    @Mock
    WindowFrame frame;

    @Mock
    Renderer renderer;

    @Mock
    Robot robot;

    @Test
    void fillWith_callsRendererFillWith() {
        screen.fillWith(Color.BLUE);

        verify(renderer).fillWith(Color.BLUE);
    }
    @Test
    void position_returnsScreenPosition() {
        when(frame.getBounds()).thenReturn(new Rectangle(40, 30, 1024, 768));
        when(frame.canvasHeight()).thenReturn(600);

        assertThat(screen.position()).isEqualTo(Offset.at(40, 198));
    }

    @Test
    void isVisible_insideCanvas_isTrue() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(Offset.at(20, 40))).isTrue();
    }

    @Test
    void isVisible_outsideCanvas_isFalse() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(Offset.at(-20, 40))).isFalse();
    }

    @Test
    void takeScreenshot_noMenuBar_createsScreenshotFromWholeWindow() {
        Canvas canvas = mock(Canvas.class);
        when(canvas.getWidth()).thenReturn(640);
        var screenshot = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        when(frame.getX()).thenReturn(120);
        when(frame.getY()).thenReturn(200);
        when(frame.getInsets()).thenReturn(new Insets(40, 0, 0, 0));
        when(frame.getCanvas()).thenReturn(canvas);
        when(frame.canvasHeight()).thenReturn(480);
        when(robot.createScreenCapture(new Rectangle(120, 240, 640, 480))).thenReturn(screenshot);
        var result = screen.takeScreenshot();

        assertThat(result.image(now())).isEqualTo(screenshot);
    }

    @Test
    void takeScreenshot_withMenuBar_createsScreenshoWithoutMenuBar() {
        Canvas canvas = mock(Canvas.class);
        when(canvas.getWidth()).thenReturn(640);
        var screenshot = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        JMenuBar menuBar = mock(JMenuBar.class);
        when(menuBar.getHeight()).thenReturn(20);
        when(frame.getJMenuBar()).thenReturn(menuBar);
        when(frame.getX()).thenReturn(120);
        when(frame.getY()).thenReturn(200);
        when(frame.getInsets()).thenReturn(new Insets(40, 0, 0, 0));
        when(frame.getCanvas()).thenReturn(canvas);
        when(frame.canvasHeight()).thenReturn(480);
        when(robot.createScreenCapture(new Rectangle(120, 260, 640, 480))).thenReturn(screenshot);

        var result = screen.takeScreenshot();

        assertThat(result.image(now())).isEqualTo(screenshot);
    }
}
