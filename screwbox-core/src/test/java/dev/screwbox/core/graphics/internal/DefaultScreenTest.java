package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.window.internal.WindowFrame;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

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
    void position_returnsCanvasOffset() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));

        assertThat(screen.position()).isEqualTo(Offset.at(40, 198));
    }

    @Test
    void takeScreenshot_windowNotOpened_throwsException() {
        assertThatThrownBy(() -> screen.takeScreenshot())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("window must be opened first to create screenshot");
    }

    @Test
    void takeScreenshot_windowIsNotAtZeroOffset_createsScreenshotFromWholeWindow() {
        var screenshot = ImageOperations.createImage(Size.square(30));
        when(frame.isVisible()).thenReturn(true);
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 90));
        when(robot.createScreenCapture(new Rectangle(40, 90, 640, 480))).thenReturn(screenshot);

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

    @Test
    void translateMonitorToScreen_pointOnScreen_returnsRelativePosition() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));

        var point = screen.translateMonitorToScreen(Offset.at(50, 250));

        assertThat(point).isEqualTo(Offset.at(10, 52));
    }

    @Test
    void translateMonitorToScreen_screenRotated_returnsRelativePosition() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));
        screen.setRotation(Angle.degrees(20));

        var point = screen.translateMonitorToScreen(Offset.at(180, 250));

        assertThat(point).isEqualTo(Offset.at(87, 125));
    }

    @Test
    void translateMonitorToScreen_screenFlippedHorizontal_returnsRelativePosition() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));

        screen.setFlipHorizontal(true);

        var point = screen.translateMonitorToScreen(Offset.at(50, 250));

        assertThat(point).isEqualTo(Offset.at(630, 52));
    }

    @Test
    void translateMonitorToScreen_screenFlippedVertical_returnsRelativePosition() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));

        screen.setFlipVertical(true);

        var point = screen.translateMonitorToScreen(Offset.at(50, 250));

        assertThat(point).isEqualTo(Offset.at(10, 428));
    }

    @Test
    void translateMonitorToScreen_screenFlippedInBothDirections_returnsRelativePosition() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));

        screen.setFlipVertical(true);
        screen.setFlipHorizontal(true);

        var point = screen.translateMonitorToScreen(Offset.at(50, 250));

        assertThat(point).isEqualTo(Offset.at(630, 428));
    }

    @Test
    void translateMonitorToScreen_screenFlippedInBothDirectionsAndRotated_returnsRelativePosition() {
        when(frame.getCanvasOffset()).thenReturn(Offset.at(40, 198));
        when(frame.getCanvasSize()).thenReturn(Size.of(640, 480));

        screen
            .setRotation(Angle.degrees(-4))
            .setFlipHorizontal(true)
            .setFlipVertical(true);

        var point = screen.translateMonitorToScreen(Offset.at(50, 250));

        assertThat(point).isEqualTo(Offset.at(616, 449));
    }

    @Test
    void isFlipHorizontal_isFlipHorizontal_isTrue() {
        screen.setFlipHorizontal(true);

        assertThat(screen.isFlipHorizontal()).isTrue();
    }

    @Test
    void isFlipVertical_isFlipVertical_isTrue() {
        screen.setFlipVertical(true);

        assertThat(screen.isFlipVertical()).isTrue();
    }

    @Test
    void isFlipHorizontal_noFlip_isFalse() {
        assertThat(screen.isFlipHorizontal()).isFalse();
    }

    @Test
    void isFlipVertical_noFlip_isFalse() {
        assertThat(screen.isFlipVertical()).isFalse();
    }
}
