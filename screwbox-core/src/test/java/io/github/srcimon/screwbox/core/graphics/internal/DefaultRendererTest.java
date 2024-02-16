package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
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

@ExtendWith(MockitoExtension.class)
class DefaultRendererTest {

    @Mock
    WindowFrame frame;

    @Mock
    Graphics2D graphics;

    @Mock
    Robot robot;

    @InjectMocks
    DefaultRenderer renderer;

    @Test
    void fillWith_newColor_changesColorAndFillsRect() {
        when(frame.getWidth()).thenReturn(640);
        when(frame.getHeight()).thenReturn(480);

        renderer.fillWith(Color.RED);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics).fillRect(0, 0, 640, 480);
    }

    @Test
    void fillWith_sameColor_changesColorOnlyOnce() {
        when(frame.getWidth()).thenReturn(640);
        when(frame.getHeight()).thenReturn(480);

        renderer.fillWith(Color.RED);
        renderer.fillWith(Color.RED);

        verify(graphics).setColor(new java.awt.Color(255, 0, 0));
        verify(graphics, times(2)).fillRect(0, 0, 640, 480);
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
        var result = renderer.takeScreenshot();

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

        var result = renderer.takeScreenshot();

        assertThat(result.image(now())).isEqualTo(screenshot);
    }
}
