package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void isVisible_boundsPartiallyInsideCanvas_isTrue() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(new ScreenBounds(200, 0, 100, 200))).isTrue();
    }

    @Test
    void isVisible_boundsInsideCanvas_isTrue() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(new ScreenBounds(45, 45, 50, 50))).isTrue();
    }

    @Test
    void isVisible_boundsOutsideCanvas_isFalse() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(new ScreenBounds(450, 450, 50, 50))).isFalse();
    }

    @Test
    void isVisible_outsideCanvas_isFalse() {
        when(frame.getCanvasSize()).thenReturn(Size.of(300, 400));

        assertThat(screen.isVisible(Offset.at(-20, 40))).isFalse();
    }

    @Test
    void drawRectangle_callsRenderer() {
        screen.drawRectangle(Offset.at(4, 10), Size.of(20, 20), RectangleDrawOptions.outline(Color.RED));

        verify(renderer).drawRectangle(Offset.at(4, 10), Size.of(20, 20), RectangleDrawOptions.outline(Color.RED));
    }

    @Test
    void drawLine_callsRenderer() {
        screen.drawLine(Offset.at(10, 3), Offset.at(21, 9), LineDrawOptions.color(Color.BLUE));

        verify(renderer).drawLine(Offset.at(10, 3), Offset.at(21, 9), LineDrawOptions.color(Color.BLUE));
    }

    @Test
    void drawCircle_radiusPositive_callsRender() {
        screen.drawCircle(Offset.at(10, 20), 4, CircleDrawOptions.fading(Color.RED));

        verify(renderer).drawCircle(Offset.at(10, 20), 4, CircleDrawOptions.fading(Color.RED));
    }

    @Test
    void takeScreenshot_windowNotOpened_throwsException() {
        assertThatThrownBy(() -> screen.takeScreenshot())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("window must be opend first to create screenshot");
    }

    @Test
    void drawSpriteBatch_callsRenderer() {
        var spriteBatch = new SpriteBatch();

        screen.drawSpriteBatch(spriteBatch);

        verify(renderer).drawSpriteBatch(spriteBatch);
    }

    @Test
    void takeScreenshot_noMenuBar_createsScreenshotFromWholeWindow() {
        Canvas canvas = mock(Canvas.class);
        when(canvas.getWidth()).thenReturn(640);
        var screenshot = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        when(frame.isVisible()).thenReturn(true);
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
        when(frame.isVisible()).thenReturn(true);
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

    @Test
    void setRotation_rotationNotNull_setsRotation() {
        screen.setRotation(Rotation.degrees(20));

        assertThat(screen.rotation()).isEqualTo(Rotation.degrees(20));
    }

    @Test
    void setRotation_rotationNull_throwsException() {
        assertThatThrownBy(() -> screen.setRotation(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rotation must not be null");
    }

    @Test
    void setShake_shakeNotNull_setsShake() {
        screen.setShake(Rotation.degrees(4));

        assertThat(screen.shake()).isEqualTo(Rotation.degrees(4));
    }

    @Test
    void setShake_shakeNull_throwsException() {
        assertThatThrownBy(() -> screen.setShake(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shake must not be null");
    }
}
