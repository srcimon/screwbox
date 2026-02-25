package dev.screwbox.core.mouse.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.DefaultCamera;
import dev.screwbox.core.graphics.internal.DefaultCanvas;
import dev.screwbox.core.graphics.internal.DefaultScreen;
import dev.screwbox.core.graphics.internal.DefaultViewport;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.mouse.MouseButton;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultMouseTest {

    @Mock
    DefaultScreen screen;

    @Mock
    ViewportManager viewportManager;

    @InjectMocks
    DefaultMouse mouse;

    @Test
    void isPressed_mouseNotPressed_isFalse() {
        mockDefaultViewportAndRotation();
        var isPressed = mouse.isPressed(MouseButton.LEFT);

        mouse.update();

        assertThat(isPressed).isFalse();
    }

    @Test
    void isPressed_mousePressed_isTrue() {
        mockDefaultViewportAndRotation();

        mouse.mousePressed(rightMouseButtonEvent());
        mouse.update();

        var isPressed = mouse.isPressed(MouseButton.RIGHT);

        assertThat(isPressed).isTrue();
    }

    @Test
    void isDown_pressedButAlreadyReleased_isFalse() {
        mouse.mousePressed(rightMouseButtonEvent());
        mouse.mouseReleased(rightMouseButtonEvent());

        var isButtonDown = mouse.isDown(MouseButton.RIGHT);

        assertThat(isButtonDown).isFalse();
    }

    @Test
    void isCursorOnScreen_enteredAndExited_isFalse() {
        mouse.mouseEntered(null);
        mouse.mouseExited(null);

        assertThat(mouse.isCursorOnScreen()).isFalse();
    }

    @Test
    void isCursorOnScreen_entered_isTrue() {
        mouse.mouseEntered(null);

        assertThat(mouse.isCursorOnScreen()).isTrue();
    }

    @Test
    void unitsScrolled_noScrolling_isZero() {
        assertThat(mouse.unitsScrolled()).isZero();
    }

    @Test
    void unitsScrolled_scrollUpAndDown_returnsSum() {
        mockDefaultViewportAndRotation();

        MouseWheelEvent scrollUpEvent = mock(MouseWheelEvent.class);
        when(scrollUpEvent.getUnitsToScroll()).thenReturn(4);
        mouse.mouseWheelMoved(scrollUpEvent);

        MouseWheelEvent scrollDownEvent = mock(MouseWheelEvent.class);
        when(scrollDownEvent.getUnitsToScroll()).thenReturn(-7);
        mouse.mouseWheelMoved(scrollDownEvent);

        mouse.update();

        MouseWheelEvent ignoredScrolling = mock(MouseWheelEvent.class);
        when(ignoredScrolling.getUnitsToScroll()).thenReturn(40);
        mouse.mouseWheelMoved(ignoredScrolling);

        assertThat(mouse.unitsScrolled()).isEqualTo(-3);
    }

    @Test
    void hasScrolled_scrollUpAndDown_true() {
        mockDefaultViewportAndRotation();

        MouseWheelEvent scrollUpEvent = mock(MouseWheelEvent.class);
        when(scrollUpEvent.getUnitsToScroll()).thenReturn(4);
        mouse.mouseWheelMoved(scrollUpEvent);

        MouseWheelEvent scrollDownEvent = mock(MouseWheelEvent.class);
        when(scrollDownEvent.getUnitsToScroll()).thenReturn(-7);
        mouse.mouseWheelMoved(scrollDownEvent);

        mouse.update();

        assertThat(mouse.hasScrolled()).isTrue();
    }

    @Test
    void hasScrolled_snotScrolled_false() {
        mockDefaultViewportAndRotation();

        mouse.update();

        assertThat(mouse.hasScrolled()).isFalse();
    }

    @Test
    void isAnyButtonDown_noButtonDown_false() {
        mockDefaultViewportAndRotation();

        mouse.update();

        assertThat(mouse.isAnyButtonDown()).isFalse();
    }

    @Test
    void isAnyButtonDown_leftClicked_true() {
        mockDefaultViewportAndRotation();

        mouse.mousePressed(rightMouseButtonEvent());

        mouse.update();

        assertThat(mouse.isAnyButtonDown()).isTrue();
    }

    @Test
    void hoverViewport_noSplitScreen_returnsDefaultViewport() {
        mockDefaultViewportAndRotation();
        mouse.update();

        Viewport result = mouse.hoverViewport();

        assertThat(result.canvas().size()).isEqualTo(Size.of(640, 480));
    }

    @Test
    void mouseMoved_mouseOnWindow_updatesOffsetAndPosition() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.getXOnScreen()).thenReturn(440);
        when(event.getYOnScreen()).thenReturn(120);
        DefaultCanvas canvas = new DefaultCanvas(null, new ScreenBounds(0, 0, 640, 480));
        DefaultViewport viewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
        when(viewportManager.calculateHoverViewport(any())).thenReturn(viewport);
        when(screen.translateMonitorToScreen(Offset.at(440, 120))).thenReturn(Offset.at(140, 20));

        mouse.mouseMoved(event);

        assertThat(mouse.offset()).isEqualTo(Offset.at(140, 20));
        assertThat(mouse.position()).isEqualTo($(-180.00, -220));

    }

    private MouseEvent rightMouseButtonEvent() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.getButton()).thenReturn(3);
        return event;
    }

    private void mockDefaultViewportAndRotation() {
        var canvas = new DefaultCanvas(null, new ScreenBounds(0, 0, 640, 480));
        DefaultViewport viewport = new DefaultViewport(canvas, new DefaultCamera(canvas));
        when(viewportManager.calculateHoverViewport(any())).thenReturn(viewport);
    }
}
