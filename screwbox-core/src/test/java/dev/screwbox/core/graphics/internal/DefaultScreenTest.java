package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static dev.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultScreenTest {

    @InjectMocks
    DefaultScreen screen;

    @Mock
    WindowFrame frame;

    @Mock
    Robot robot;

    @Mock
    ViewportManager viewportManager;

    @Test
    void setCanvasBounds_canvasNotOnScreen_throwsException() {
        when(frame.getCanvasSize()).thenReturn(Size.of(100, 100));

        var outOfBounds = new ScreenBounds(-100, -200, 40, 40);
        assertThatThrownBy(() -> screen.setCanvasBounds(outOfBounds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("bounds must be on screen");
    }

    @Test
    void setCanvasBounds_canvasNNull_throwsException() {
        assertThatThrownBy(() -> screen.setCanvasBounds(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("bounds must not be null");
    }

    @Test
    void position_returnsScreenPosition() {
        when(frame.getBounds()).thenReturn(new Rectangle(40, 30, 1024, 768));
        when(frame.canvasHeight()).thenReturn(600);

        assertThat(screen.position()).isEqualTo(Offset.at(40, 198));
    }

    @Test
    void takeScreenshot_windowNotOpened_throwsException() {
        assertThatThrownBy(() -> screen.takeScreenshot())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("window must be opened first to create screenshot");
    }

    @Test
    void takeScreenshot_noMenuBar_createsScreenshotFromWholeWindow() {
        var screenshot = ImageOperations.createImage(Size.square(30));
        when(frame.isVisible()).thenReturn(true);
        when(frame.getX()).thenReturn(120);
        when(frame.getY()).thenReturn(200);
        when(frame.getInsets()).thenReturn(new Insets(40, 0, 0, 0));
        when(robot.createScreenCapture(new Rectangle(120, 240, 640, 480))).thenReturn(screenshot);
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));

        var result = screen.takeScreenshot();

        assertThat(result.image(now())).isEqualTo(screenshot);
    }

    @Test
    void takeScreenshot_withMenuBar_createsScreenshotWithoutMenuBar() {
        when(frame.isVisible()).thenReturn(true);
        var screenshot = ImageOperations.createImage(Size.square(30));
        JMenuBar menuBar = mock(JMenuBar.class);
        when(menuBar.getHeight()).thenReturn(20);
        when(frame.getJMenuBar()).thenReturn(menuBar);
        when(frame.getX()).thenReturn(120);
        when(frame.getY()).thenReturn(200);
        when(frame.getInsets()).thenReturn(new Insets(40, 0, 0, 0));
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));
        when(robot.createScreenCapture(new Rectangle(120, 260, 640, 480))).thenReturn(screenshot);

        var result = screen.takeScreenshot();

        assertThat(result.image(now())).isEqualTo(screenshot);
    }

    @Test
    void setRotation_rotationNotNull_setsRotation() {
        screen.setRotation(Angle.degrees(20));

        assertThat(screen.rotation()).isEqualTo(Angle.degrees(20));
    }

    @Test
    void setRotation_rotationNull_throwsException() {
        assertThatThrownBy(() -> screen.setRotation(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rotation must not be null");
    }

    @Test
    void createCanvas_invalidSize_throwsException() {
        when(screen.size()).thenReturn(Size.of(640, 480));

        var offset = Offset.at(700, 500);
        var size = Size.of(5, 5);

        assertThatThrownBy(() -> screen.createCanvas(offset, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("bounds must be on screen");
    }

    @Test
    void update_multipleViewports_setsSwingToSumOfAllPlusDefaultViewport() {
        Viewport viewport = mock(Viewport.class);
        Camera camera = mock(Camera.class);
        when(viewport.camera()).thenReturn(camera);
        when(camera.swing()).thenReturn(Angle.degrees(10));

        when(viewportManager.defaultViewport()).thenReturn(viewport);

        Viewport viewport2 = mock(Viewport.class);
        Camera camera2 = mock(Camera.class);
        when(viewport2.camera()).thenReturn(camera2);
        when(camera2.swing()).thenReturn(Angle.degrees(5));

        Viewport viewport3 = mock(Viewport.class);
        Camera camera3 = mock(Camera.class);
        when(viewport3.camera()).thenReturn(camera3);
        when(camera3.swing()).thenReturn(Angle.degrees(-2));

        when(viewportManager.viewports()).thenReturn(List.of(viewport2, viewport3));

        screen.update();

        assertThat(screen.shake()).isEqualTo(Angle.degrees(13));
    }

    @Test
    void update_onlyDefaultViewport_setsSwingToDefaultViewport() {
        Viewport viewport = mock(Viewport.class);
        Camera camera = mock(Camera.class);
        when(viewport.camera()).thenReturn(camera);
        when(camera.swing()).thenReturn(Angle.degrees(10));

        when(viewportManager.defaultViewport()).thenReturn(viewport);

        screen.update();

        assertThat(screen.shake()).isEqualTo(Angle.degrees(10));
    }

}
